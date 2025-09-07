package com.servicedesk.controller;

import com.servicedesk.api.dto.project.ProjectCreateRequest;
import com.servicedesk.api.dto.project.ProjectResponse;
import com.servicedesk.api.dto.project.ProjectUpdateRequest;
import com.servicedesk.common.web.PaginationUtils;
import com.servicedesk.common.web.SortAllowList;
import com.servicedesk.common.web.SortUtils;
import com.servicedesk.context.OrganizationContext;
import com.servicedesk.service.ProjectService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public Page<ProjectResponse> getProjects(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "name") String sort
    ) {
        UUID orgId = OrganizationContext.getOrgId();
        Sort sortBy = Sort.by(Sort.Direction.ASC, sort);
        SortUtils.validateSortFields(sortBy, SortAllowList.PROJECT_SORT_FIELDS);

        Pageable rawPageable = PageRequest.of(pageNumber, limit, sortBy);

        Pageable sanitizedPageable = PaginationUtils.sanitizePageable(rawPageable);

        return projectService.getProjects(orgId, sanitizedPageable);
    }

    @GetMapping(path = "/{id}")
    public ProjectResponse getProjectById(@PathVariable String id) {
        return projectService.getProjectById(UUID.fromString(id));
    }

    @PostMapping
    public ProjectResponse createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.createProject(request);
    }

    @PatchMapping(path = "/{id}")
    public ProjectResponse updateProject(@PathVariable String id, @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.updateProject(UUID.fromString(id), request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(UUID.fromString(id));
        return ResponseEntity.noContent()
                .build();
    }
}
