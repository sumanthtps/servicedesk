# REST

## Adding 201 created response with location

- Use ServletUriComponentsBuilder

```declarative
   URI location = ServletUriComponentsBuilder
.fromCurrentRequest()
.path("/{id}")
.buildAndExpand(createdIssue.getId())
.toUri();

// Return 201 Created with Location header
return ResponseEntity.created(location).body(createdIssue);
```

### Key Benefits

- Proper HTTP Status Code: 201 Created instead of 200 OK
- Location Header: Clients can easily find the newly created resource
- REST Compliance: Follows standard REST conventions
- Discoverability: Clients can use the Location header to navigate to the new resource
- Idempotency: If clients need to retry, they can check the Location first
