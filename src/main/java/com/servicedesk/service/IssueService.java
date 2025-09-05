package com.servicedesk.service;

import com.servicedesk.api.dto.issue.IssueCreateRequest;
import com.servicedesk.api.dto.issue.IssueFilter;
import com.servicedesk.api.dto.issue.IssueResponse;
import com.servicedesk.api.dto.issue.IssueUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IssueService {
    Page<IssueResponse> getIssues(UUID orgId, IssueFilter filter, Pageable pageable);

    IssueResponse getIssuesById(UUID orgId, UUID issueId);

    IssueResponse createIssue(UUID orgId, IssueCreateRequest issueCreateRequest);

    IssueResponse updateIssue(UUID orgId, UUID issueId, IssueUpdateRequest issueUpdateRequest);

    void deleteIssue(UUID issueId);
}
