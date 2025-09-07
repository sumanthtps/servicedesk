package com.servicedesk.service.impl;

import com.servicedesk.api.dto.issue.IssueCreateRequest;
import com.servicedesk.api.dto.issue.IssueFilter;
import com.servicedesk.api.dto.issue.IssueResponse;
import com.servicedesk.api.dto.issue.IssueUpdateRequest;
import com.servicedesk.common.enums.Constants;
import com.servicedesk.common.enums.Priority;
import com.servicedesk.common.enums.Status;
import com.servicedesk.common.error.ConflictException;
import com.servicedesk.common.error.InvalidOrganizationException;
import com.servicedesk.common.error.ResourceNotFoundException;
import com.servicedesk.common.web.StringUtils;
import com.servicedesk.context.OrganizationContext;
import com.servicedesk.domain.model.Issue;
import com.servicedesk.domain.repositories.IssueRepository;
import com.servicedesk.domain.repositories.ProjectRepository;
import com.servicedesk.domain.repositories.UserRepository;
import com.servicedesk.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public IssueServiceImpl(IssueRepository issueRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Page<IssueResponse> getIssues(UUID orgId, IssueFilter filter, Pageable pageable) {
        Specification<Issue> organizationSpec = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("organizationId"), orgId);
        };
        final List<Specification<Issue>> specifications = getSpecificationList(filter);

        Specification<Issue> filterSpecs = specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(((root, query, criteriaBuilder) -> criteriaBuilder.conjunction()));
        Specification<Issue> combinedSpecs = organizationSpec.and(filterSpecs);

        return issueRepository.findAll(combinedSpecs, pageable)
                .map(IssueResponse::fromEntity);

    }

    private List<Specification<Issue>> getSpecificationList(IssueFilter filter) {
        List<Specification<Issue>> specifications = new ArrayList<>();
        specifications.add(createEqualSpecification(filter.getProjectId(), "projectId"));
        specifications.add(createEqualSpecification(filter.getStatus(), "status"));
        specifications.add(createEqualSpecification(filter.getPriority(), "priority"));
        specifications.add(createEqualSpecification(filter.getAssigneeId(), "assigneeId"));
        if (StringUtils.hasText(filter.getTitleContains())) {
            String pattern = "%" + filter.getTitleContains()
                    .toLowerCase() + "%";
            specifications.add(((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern)));
        }
        return specifications;
    }

    private <T> Specification<Issue> createEqualSpecification(T value, String fieldName) {
        if (value == null) {
            return null;
        }
        return ((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(fieldName), value));
    }

    @Override
    public IssueResponse getIssuesById(UUID orgId, UUID issueId) {
        Issue issue = issueRepository.findByIdAndOrganizationId(issueId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue with id: " + issueId + " not found"));
        return IssueResponse.fromEntity(issue);
    }

    @Override
    public IssueResponse createIssue(UUID orgId, IssueCreateRequest issueCreateRequest) {
        if (!projectRepository.existsByIdAndOrganizationId(
                orgId, UUID.fromString(issueCreateRequest.getProjectId()))
        ) {
            throw new InvalidOrganizationException(
                    "Project " + issueCreateRequest.getProjectId() + " is not found in the current organization " + orgId);
        }
        Issue issue = new Issue();
        issue.setProjectId(UUID.fromString(issueCreateRequest.getProjectId()));
        if (StringUtils.hasText(issueCreateRequest.getTitle())) {
            issue.setTitle(issueCreateRequest.getTitle()
                    .trim());
        }
        if (StringUtils.hasText(issueCreateRequest.getDescription())) {
            issue.setDescription(issueCreateRequest.getDescription()
                    .trim());
        }
        if (issueCreateRequest.getAssigneeId() != null) {
            final boolean assigneeExists = userRepository.existsByIdAndOrganizationId(
                    UUID.fromString(issueCreateRequest.getAssigneeId()), orgId
            );
            if (!assigneeExists) {
                throw new ConflictException("Assignee not found or not in the same organization");
            }
            issue.setAssigneeId(UUID.fromString(issueCreateRequest.getAssigneeId()));
        }

        issue.setPriority(Optional.ofNullable(issueCreateRequest.getPriority())
                .map(String::trim)
                .map(String::toUpperCase)
                .map(Priority::valueOf)
                .orElse(Priority.MEDIUM));
        issue.setStatus(Status.OPEN);
        issue.setOrganizationId(orgId);

        Issue savedIssue = issueRepository.save(issue);
        return IssueResponse.fromEntity(savedIssue);
    }

    @Override
    public IssueResponse updateIssue(UUID orgId, UUID issueId, IssueUpdateRequest issueUpdateRequest) {
        Issue existingIssue = issueRepository.findByIdAndOrganizationId(issueId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found or not accessible"));
        update(issueUpdateRequest, existingIssue, orgId);
        Issue updatedIssue = issueRepository.save(existingIssue);
        return IssueResponse.fromEntity(updatedIssue);
    }

    private void update(IssueUpdateRequest issueUpdateRequest, Issue existingIssue, UUID orgId) {
        if (StringUtils.hasText(issueUpdateRequest.getTitle())) {
            existingIssue.setTitle(issueUpdateRequest.getTitle()
                    .trim());
        }
        if (StringUtils.hasText(issueUpdateRequest.getDescription())) {
            existingIssue.setDescription(issueUpdateRequest.getDescription()
                    .trim());
        }
        if (issueUpdateRequest.getAssigneeId() != null) {
            final boolean assigneeExists = userRepository.existsByIdAndOrganizationId(
                    UUID.fromString(issueUpdateRequest.getAssigneeId()), orgId
            );
            if (!assigneeExists) {
                throw new ConflictException("Assignee not found or not in the same organization");
            }
            existingIssue.setAssigneeId(UUID.fromString(issueUpdateRequest.getAssigneeId()));
        }
        if (issueUpdateRequest.getPriority() != null) {
            existingIssue.setPriority(Priority.valueOf(issueUpdateRequest.getPriority()
                    .trim()
                    .toUpperCase()));
        }
        if (issueUpdateRequest.getStatus() != null) {
            Status currentStatus = existingIssue.getStatus();
            Status newStatus = currentStatus;
            if (StringUtils.hasText(issueUpdateRequest.getStatus())) {
                newStatus = Status.valueOf(issueUpdateRequest.getStatus()
                        .trim()
                        .toUpperCase());
            }
            Set<Status> allowedTransitions = Constants.ALLOWED_TRANSITIONS.get(currentStatus);

            if (allowedTransitions == null || !allowedTransitions.contains(newStatus)) {
                throw new ConflictException(
                        String.format("Invalid status transition %s to %s", currentStatus, newStatus)
                );
            }
            existingIssue.setStatus(newStatus);
        }

    }

    @Override
    public void deleteIssue(UUID issueId) {
        final UUID orgId = OrganizationContext.getOrgId();
        Issue issue = issueRepository.findByIdAndOrganizationId(issueId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue with id " + issueId + " not found in organization " + orgId
                ));
        issueRepository.delete(issue);
    }
}
