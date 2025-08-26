# ServiceDesk — API Contract (Baseline, v1)

Base path: `/api/v1`
Format: JSON
Auth: to be added later (JWT). For now, assume endpoints require authentication except health.
Tenanting: every request is organization-scoped. For v1, the client must provide an `X-Org-Id` header; in the future, this will come from JWT claims.

---

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
  * 409 if `key` already exists *within the same organization*
* Sort allowlist: `name`, `createdAt`.

### Get Project by ID

* `GET /projects/{id}`
* 200 with project or 404

### List Projects

* `GET /projects`
* Filters: none in v1
* Pagination + sorting as defined

### Update Project

* `PATCH /projects/{id}`
* Updatable: `name`, `description` (not `key`)
* Responses:

  * 200 with updated project
  * 404 not found
  * 400 invalid request

### Delete Project

* `DELETE /projects/{id}`
* Responses:

  * 204 success
  * 404 not found
* **Side-effect:** all Issues in the Project are marked `CLOSED` transactionally; Comments remain.
* Consider admin-only later.

---

## Issues

### Create Issue

* `POST /issues`
* Request:

  * `projectId` (required, must belong to same organization)
  * `title` (required, ≤140)
  * `description` (optional, ≤10000)
  * `assigneeId` (optional, must be active user in same organization)
  * `priority` (required: `LOW|MEDIUM|HIGH`)
* Responses:

  * 201 created issue
  * 400 validation errors
  * 404 project or assignee not found/inactive

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
  * `titleContains` (string; case-insensitive substring search on `title`)
* Pagination + sorting
* Sort allowlist: `createdAt`, `priority`, `status`, `title`

### Update Issue

* `PATCH /issues/{id}`
* Updatable:

  * `title`, `description`, `assigneeId`, `priority`, `status`
* Rules:

  * `status` must follow the transition table (CLOSED is terminal)
  * If setting `assigneeId`, user must be active
* Responses:

  * 200 updated
  * 400 invalid request
  * 404 not found
  * 409 if illegal status transition

### Delete Issue

* `DELETE /issues/{id}`
* 204 or 404
* For v1, deletion may be disabled if `status=CLOSED`.

---

## Issue Comments

### Add Comment

* `POST /issues/{id}/comments`
* Request:

  * `body` (required, 1–10000)
* Author is the authenticated user (future); for now, `authorId` may be accepted as input.
* Responses:

  * 201 created comment
  * 400 validation error
  * 404 if issue not found

### List Comments

* `GET /issues/{id}/comments`
* Pagination + sort by `createdAt` (asc|desc)
* 200 or 404 if issue not found

---

## Users (minimal for v1; full management comes with Auth)

* `GET /users` — list users (pagination, sort by `username`, `displayName`)
* `GET /users/{id}` — get user
* `POST /users` — create user (bootstrap only)

  * `username`, `displayName`, `email`, `active` (default true)
* `PATCH /users/{id}` — update `displayName`, `active`

**Privacy**

* `password` never exposed.
* `email` returned in v1 responses but may be hidden when auth is added.

---

## Response field baselines

### Project

```
{
  "id": "uuid",
  "organizationId": "uuid",
  "name": "string",
  "key": "string",
  "description": "string|null",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Issue

```
{
  "id": "uuid",
  "organizationId": "uuid",
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

### Comment

```
{
  "id": "uuid",
  "organizationId": "uuid",
  "issueId": "uuid",
  "authorId": "uuid",
  "body": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### User

```
{
  "id": "uuid",
  "organizationId": "uuid",
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