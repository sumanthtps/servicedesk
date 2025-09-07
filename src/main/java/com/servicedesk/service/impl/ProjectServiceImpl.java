package com.servicedesk.service.impl;

import com.servicedesk.api.dto.project.ProjectCreateRequest;
import com.servicedesk.api.dto.project.ProjectResponse;
import com.servicedesk.api.dto.project.ProjectUpdateRequest;
import com.servicedesk.common.error.ConflictException;
import com.servicedesk.common.error.InvalidOrganizationException;
import com.servicedesk.common.error.ResourceNotFoundException;
import com.servicedesk.context.OrganizationContext;
import com.servicedesk.domain.model.Project;
import com.servicedesk.domain.repositories.ProjectRepository;
import com.servicedesk.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Page<ProjectResponse> getProjects(UUID organizationId, Pageable pageable) {
        return projectRepository.findByOrganizationId(organizationId, pageable)
                .map(ProjectResponse::fromEntity);
    }

    @Override
    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectResponse::fromEntity);
    }

    @Override
    public ProjectResponse getProjectById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + projectId + " not found"));
        validateOrganization(project);
        return ProjectResponse.fromEntity(project);
    }

    private void validateOrganization(Project project) {
        UUID orgId = OrganizationContext.getOrgId();
        if (!project.getOrganizationId()
                .equals(orgId)) {
            throw new InvalidOrganizationException("The organization with id " + orgId + " doesn't have access to the project");
        }
    }

    @Override
    public ProjectResponse createProject(ProjectCreateRequest projectCreateRequest) {
        Project newProject = new Project();
        newProject.setName(projectCreateRequest.getName()
                .trim());
        newProject.setKey(projectCreateRequest.getKey()
                .toUpperCase()
                .trim());
        if (projectCreateRequest.getDescription() != null) {
            newProject.setDescription(projectCreateRequest.getDescription()
                    .trim());
        }


        newProject.setOrganizationId(OrganizationContext.getOrgId());

        if (projectRepository.existsByOrganizationIdAndKey(newProject.getOrganizationId(), newProject.getKey())) {
            throw new ConflictException("The project with the key already exists " + newProject.getKey());
        }
        if (projectRepository.existsByOrganizationIdAndName(newProject.getOrganizationId(), newProject.getName())) {
            throw new ConflictException("The project with this name already exists " + newProject.getName());
        }
        Project savedProject = projectRepository.save(newProject);
        return ProjectResponse.fromEntity(savedProject);
    }

    @Override
    public ProjectResponse updateProject(UUID projectId, ProjectUpdateRequest projectUpdateRequest) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with " + projectId + " not found"));
        validateOrganization(existingProject);
        if (projectUpdateRequest.getName() != null) {
            if (projectRepository.existsByOrganizationIdAndNameAndIdNot(
                    existingProject.getOrganizationId(),
                    projectUpdateRequest.getName(),
                    existingProject.getId())) {
                throw new ConflictException("The project with this name already exists " + projectUpdateRequest.getName());
            } else {
                existingProject.setName(projectUpdateRequest.getName()
                        .trim());
            }
        }
        if (projectUpdateRequest.getDescription() != null) {
            existingProject.setDescription(projectUpdateRequest.getDescription()
                    .trim());
        }


        Project updatedProject = projectRepository.save(existingProject);

        return ProjectResponse.fromEntity(updatedProject);
    }

    @Override
    public void deleteProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with " + projectId + " not found to delete"));
        validateOrganization(project);
        projectRepository.delete(project);
    }
}
