package com.servicedesk.service;

import com.servicedesk.api.dto.project.ProjectCreateRequest;
import com.servicedesk.api.dto.project.ProjectResponse;
import com.servicedesk.api.dto.project.ProjectUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProjectService {
    Page<ProjectResponse> getProjects(UUID organizationId, Pageable pageable);

    Page<ProjectResponse> getAllProjects(Pageable pageable);

    ProjectResponse getProjectById(UUID projectId);

    ProjectResponse createProject(ProjectCreateRequest projectCreateRequest);

    ProjectResponse updateProject(UUID projectId, ProjectUpdateRequest projectUpdateRequest);

    void deleteProject(UUID projectId);
}
