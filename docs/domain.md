# docs/domain.md

# ServiceDesk — Domain Model (Baseline)

## Purpose

Define the core entities, relationships, constraints, and invariants for the ServiceDesk backend. This is the source of truth for later API and database design.

## Conventions

* Identifiers: UUID (string at API boundary).
* Timestamps: ISO-8601 in UTC (`createdAt`, `updatedAt`).
* Soft delete: not supported in v1.
* Text limits: names ≤ 100 chars; titles ≤ 140; descriptions/comments ≤ 10,000.

## Entities

### User

* `id` (UUID, required)
* `username` (string, 3–50, required, unique)
* `displayName` (string, 1–100, required)
* `email` (string, required, unique, RFC-compliant)
* `active` (boolean, default true)
* `createdAt`, `updatedAt` (timestamps)

**Notes**

* Email is returned only to authenticated callers (future step).

---

### Project

* `id` (UUID, required)
* `name` (string, 1–100, required, unique per tenant/system)
* `key` (string, 2–10, required, uppercase A-Z, unique)  // e.g., “SD”
* `description` (string, optional, ≤ 10000)
* `createdAt`, `updatedAt` (timestamps)

**Invariants**

* `key` is immutable after creation in v1.

---

### Issue

* `id` (UUID, required)
* `projectId` (UUID, required)
* `assigneeId` (UUID, optional)
* `title` (string, 1–140, required)
* `description` (string, optional, ≤ 10000)
* `status` (enum, required) → `OPEN | IN_PROGRESS | RESOLVED | CLOSED`
* `priority` (enum, required) → `LOW | MEDIUM | HIGH`
* `createdAt`, `updatedAt` (timestamps)

**Invariants**

* `projectId` must reference an existing `Project`.
* `assigneeId` must reference an active `User` if present.
* `status` transitions follow the table below.

**Status transitions (v1)**

| From         | To           | Notes                     |
| ------------ | ------------ | ------------------------- |
| OPEN         | IN\_PROGRESS | Allowed                   |
| OPEN         | RESOLVED     | Allowed                   |
| IN\_PROGRESS | RESOLVED     | Allowed                   |
| RESOLVED     | CLOSED       | Allowed                   |
| IN\_PROGRESS | OPEN         | Allowed (revert)          |
| RESOLVED     | IN\_PROGRESS | Allowed (reopen for work) |
| CLOSED       | (none)       | Terminal in v1            |

---

### Comment

* `id` (UUID, required)
* `issueId` (UUID, required)
* `authorId` (UUID, required)
* `body` (string, 1–10000, required)
* `createdAt`, `updatedAt` (timestamps)

**Invariants**

* `issueId` must reference an existing `Issue`.
* `authorId` must reference an active `User`.

---

## Relationships

* Project 1—N Issue
* Issue 1—N Comment
* User 1—N Issue (as assignee)
* User 1—N Comment (as author)

---

## Indexing plan (anticipating queries)

* `issues(project_id, status, priority, created_at)`
* `issues(assignee_id, status)`
* `comments(issue_id, created_at)`
* `projects(key)` unique
* `users(username)` unique, `users(email)` unique

---

## Validation summary

* Strings trimmed; disallow all-whitespace values.
* Max lengths enforced as above.
* Email regex validation plus normalized to lowercase at write time.
* UUIDs validated for correct format at the API boundary.

---

## Out of scope (deferred)

* Labels, attachments, watchers, audit trail.
* SLA timers/metrics.
* Multi-tenant boundary and roles (handled in Auth step).

---

