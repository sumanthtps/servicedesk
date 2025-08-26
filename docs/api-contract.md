# ServiceDesk — API Contract (Baseline, v1)

Base path: `/api/v1`
Format: JSON
Auth: to be added later (JWT). For now, assume endpoints require authentication except health.

## Cross-cutting

### Pagination

* Query params: `page` (0-based, default `0`), `size` (default `20`, max `100`).
* Responses include: `content[]`, `page`, `size`, `totalElements`, `totalPages`, `sort`.

### Sorting

* Query param: `sort=field,asc|desc`. Multiple `sort` allowed.
* Allowed fields per resource only (see endpoints). If invalid, return 400 with Problem Details.

### Filtering

* Explicit query params; no free-form search in v1.

### Error model (Problem Details, RFC 7807)

```
{
  "type": "https://api.servicedesk/errors/validation-error",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more fields are invalid.",
  "instance": "/api/v1/issues",
  "errors": [
    { "field": "title", "message": "must not be blank" },
    { "field": "projectId", "message": "must reference existing Project" }
  ]
}
```

### IDs & timestamps

* IDs are UUID strings.
* Timestamps are ISO-8601 UTC, fields: `createdAt`, `updatedAt`.

---

## Projects

### Create Project

* `POST /projects`
* Request:

    * `name` (required, ≤100)
    * `key` (required, 2–10, uppercase A-Z, immutable)
    * `description` (optional, ≤10000)
* Responses:

    * 201 with created project
    * 400 on validation error
    * 409 if `key` already exists
* Sort allowlist (for list): `name`, `createdAt`.

### Get Project by ID

* `GET /projects/{id}`
* 200 with project or 404

### List Projects

* `GET /projects`
* Filters: none in v1
* Pagination + sorting as defined

### Update Project

* Choose one for v1: **PUT** (full) or **PATCH** (partial). Baseline: **PATCH**.
* `PATCH /projects/{id}`
* Updatable: `name`, `description` (not `key`)
* 200 with updated, 404 not found, 400 invalid

### Delete Project (optional in v1)

* `DELETE /projects/{id}`
* 204 or 404
* Consider admin-only later

---

## Issues

### Create Issue

* `POST /issues`
* Request:

    * `projectId` (required)
    * `title` (required, ≤140)
    * `description` (optional, ≤10000)
    * `assigneeId` (optional)
    * `priority` (required: `LOW|MEDIUM|HIGH`)
* Response:

    * 201 with issue
    * 400 validation
    * 404 if `projectId` or `assigneeId` not found (or inactive)

### Get Issue by ID

* `GET /issues/{id}`
* 200 or 404

### List Issues

* `GET /issues`
* Filters (all optional):

    * `projectId` (UUID)
    * `status` (enum)
    * `priority` (enum)
    * `assigneeId` (UUID)
    * `titleContains` (string; case-insensitive substring on title only)
* Pagination + sorting
* Sort allowlist: `createdAt`, `priority`, `status`, `title`

### Update Issue (fields & transitions)

* `PATCH /issues/{id}`
* Updatable:

    * `title`, `description`, `assigneeId`, `priority`, `status`
* Rules:

    * `status` must follow the transition table in domain doc
    * If setting `assigneeId`, user must be active
* 200 updated, 400 invalid, 404 not found, 409 illegal transition

### Delete Issue (optional in v1)

* `DELETE /issues/{id}`
* 204 or 404
* Consider disallow if `status=CLOSED` in v1

---

## Issue Comments

### Add Comment

* `POST /issues/{id}/comments`
* Request:

    * `body` (required, 1–10000)
* Author is the authenticated user (server-side), for now accept `authorId` as input if auth not implemented yet.
* Responses:

    * 201 with comment
    * 404 if issue not found
    * 400 validation

### List Comments

* `GET /issues/{id}/comments`
* Pagination + sort by `createdAt` only (asc|desc)
* 200 or 404 if issue not found

---

## Users (minimal for v1; full user management comes with Auth)

* `GET /users` — list users (pagination, sort by `username` or `displayName`)
* `GET /users/{id}` — get user
* `POST /users` — create user (for now, to bootstrap data)

    * `username`, `displayName`, `email`, `active` (default true)
* Optional updates:

    * `PATCH /users/{id}` — update `displayName`, `active`
* Privacy:

    * Email may be omitted from responses in unauthenticated contexts (future step)

---

## Response field baselines

### Project (response)

```
{
  "id": "uuid",
  "name": "string",
  "key": "string",
  "description": "string|null",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Issue (response)

```
{
  "id": "uuid",
  "projectId": "uuid",
  "assigneeId": "uuid|null",
  "title": "string",
  "description": "string|null",
  "status": "OPEN|IN_PROGRESS|RESOLVED|CLOSED",
  "priority": "LOW|MEDIUM|HIGH",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Comment (response)

```
{
  "id": "uuid",
  "issueId": "uuid",
  "authorId": "uuid",
  "body": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### User (response)

```
{
  "id": "uuid",
  "username": "string",
  "displayName": "string",
  "email": "string",
  "active": true,
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

---

## Non-functional notes

* Default `size=20`, hard cap `size<=100`.
* Reject unknown sort fields with 400.
* Consistent `Content-Type: application/json`.
* Versioning via path `/api/v1`; only additive changes within v1.

---

## Open questions to finalize (fill these before coding)

1. Will `Project.key` be globally unique or per organization/tenant?
2. Do we allow deleting a `Project` that still has `Issues`? If yes, what happens to issues?
3. Should `Issue.CLOSED` be terminal in v1 (no reopen) or allow `CLOSED→IN_PROGRESS`?
4. Should `titleContains` be case-insensitive only, or also support full-text later?
5. What fields are returned/hidden for `User` when auth is added?

---