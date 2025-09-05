package com.servicedesk.controller;

import com.servicedesk.api.dto.issue.IssueCreateRequest;
import com.servicedesk.api.dto.issue.IssueFilter;
import com.servicedesk.api.dto.issue.IssueResponse;
import com.servicedesk.api.dto.issue.IssueUpdateRequest;
import com.servicedesk.common.enums.Priority;
import com.servicedesk.common.enums.Status;
import com.servicedesk.common.web.PaginationUtils;
import com.servicedesk.common.web.SortAllowList;
import com.servicedesk.common.web.SortUtils;
import com.servicedesk.context.OrganizationContext;
import com.servicedesk.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public Page<IssueResponse> getIssues(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int limit, // page size
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) UUID assigneeId,
            @RequestParam(required = false) String title

    ) {
        UUID orgId = OrganizationContext.getOrgId();

        Sort sortBy = Sort.by(Sort.Direction.fromString(direction), sort);
        SortUtils.validateSortFields(sortBy, SortAllowList.ISSUE_SORT_FIELDS);

        Pageable sanitizedPageable = PaginationUtils.sanitizePageable(PageRequest.of(pageNumber, limit, sortBy));

        IssueFilter filter = new IssueFilter();
        filter.setProjectId(projectId);
        if (status != null) filter.setStatus(Enum.valueOf(Status.class, status.toUpperCase()));
        if (priority != null) filter.setPriority(Enum.valueOf(Priority.class, priority.toUpperCase()));
        filter.setAssigneeId(assigneeId);
        filter.setTitleContains(title);

        return issueService.getIssues(orgId, filter, sanitizedPageable);
    }

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(@Valid @RequestBody IssueCreateRequest createRequest) {
        UUID orgId = OrganizationContext.getOrgId();
        final IssueResponse createdIssue = issueService.createIssue(orgId, createRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdIssue.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdIssue);
    }

    @GetMapping("/{id}")
    public IssueResponse getIssueById(@PathVariable String id) {
        final UUID issueId = UUID.fromString(id);
        UUID orgId = OrganizationContext.getOrgId();
        return issueService.getIssuesById(orgId, issueId);
    }

    @PatchMapping("/{id}")
    public IssueResponse updateIssueById(@PathVariable String id, @RequestBody IssueUpdateRequest updateRequest) {
        final UUID issueId = UUID.fromString(id);
        UUID orgId = OrganizationContext.getOrgId();
        return issueService.updateIssue(orgId, issueId, updateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssueById(@PathVariable String id) {
        final UUID issueId = UUID.fromString(id);
        issueService.deleteIssue(issueId);
        return ResponseEntity.noContent()
                .build();
    }
}
