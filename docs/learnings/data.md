# 1) What are JPA Specifications (and why use them)?

Think of Specifications as composable query predicates. Instead of writing a different repository method for every filter combo (findByProjectIdAndStatus…, findByStatusAndPriority…, etc.), you:

Define tiny, single-purpose predicates (e.g., “status = ?”, “priority = ?”, “title contains ?”, “projectId = ?”, “orgId = ?”).

Compose only the ones you need at runtime, based on which query params were provided.

Execute a single findAll(spec, pageable) and you’re done.

## Benefits

Scales cleanly as filters grow (no explosion of findBy… methods).

Testable in isolation (each predicate).

Performance-friendly when paired with the right indexes (you already added them).

## Structure (no code)

Create an IssueSpecifications helper with one method per filter (e.g., byOrganization(UUID orgId), byProject(UUID projectId), withStatus(Status s), withPriority(Priority p), titleContains(String q)).

In the service layer, start with byOrganization(orgId) and conditionally chain the others only if the param is present.

Pass that composed spec + a PageRequest (with validated sort) to the repo.

## Gotchas

Always include orgId in the spec first (tenant safety).

For titleContains, ensure the predicate uses LOWER(title) so Postgres can use your LOWER(title) index.

Don’t overdo “like” filters on unindexed columns.