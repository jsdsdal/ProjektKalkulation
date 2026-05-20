package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.exception.InvalidTaskException;
import com.example.projektkalkulationgruppe6.exception.ProjectNotFoundException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        try {
            return projectRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Projects could not be loaded.", ex);
        }
    }

    public Project create(Project project) {
        validateProject(project);

        try {
            return projectRepository.insert(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The project was not created.", ex);
        }
    }

    private Project getTreeById(int id) {
        validateId(id);

        List<Project> projects = new ArrayList<>();

        try {
            projects = projectRepository.findParentAndChildrenById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The Project could not be loaded.", ex);
        }

        if (projects.isEmpty()) {
            throw new ProjectNotFoundException(id);
        }

        Project parentProject = projects.getFirst();

        projects.removeFirst();

        parentProject.setSubProjects(projects);

        return parentProject;

    }

    public List<String> findAllStatusList() {

        List<String> statusList;

        try {
            statusList = projectRepository.findAllStatusList();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Status list could not be loaded.", ex);
        }

        if (statusList.isEmpty()) {
            throw new DatabaseOperationException(
                    "Error: Status list could not be loaded.",
                    new Throwable("List result is empty"));
        }

        return statusList;
    }

    private void validateId(int id) {
        if (id <= 0) {
                throw new InvalidProjectException("ID must be a positive number");
            }

    }

    private void validateProject(Project project) {
        if (project == null) {
            throw new InvalidProjectException("Project is mandatory");
        }

        String title = project.getTitle();
        if (title == null || title.isBlank()) {
            throw new InvalidProjectException("Project Title is a mandatory field");
        }

        String status = project.getStatus();
        if (status == null || status.isBlank() ) {
            throw new InvalidProjectException("Project Status is a mandatory field");
        }

        if (project.getProjectOwner().getEmployeeId() == null) {
            throw new InvalidProjectException("Project Owner is a mandatory field");
        }

        //OBS: Mangler validering af project Owner

    }

    public Project createSubproject(Project subproject, int parentProjectId) {
        Project parent = projectRepository.findById(parentProjectId);

        // tjek om projekt findes
        if (parent == null) {
            throw new InvalidProjectException("Parent project not found: " + parentProjectId);
        }

        // ingen børnebørn
        if (parent.getParentProjectId() != null) {
            throw new InvalidProjectException("Cannot create subproject under existing subproject.");
        }

        // må ikke starte før parent project
        if (parent.getStartDate() != null && subproject.getStartDate() != null
        && subproject.getStartDate().isBefore(parent.getStartDate())) {
            throw new InvalidProjectException("Subproject cannot start before the parent project.");
        }

        // må ikke slutte efter parent project
        if (parent.getEndDate() != null && subproject.getEndDate() != null
            && subproject.getEndDate().isAfter(parent.getEndDate())) {
            throw new InvalidProjectException("Subproject cannot end after the parent project.");
        }

        subproject.setParentProjectId(parentProjectId);
        return create(subproject);
    }

    public void delete(int projectId){
        if(projectId<=0){
            throw new InvalidProjectException("ProjectID needs to be a positive number");
        }

        boolean deleted = projectRepository.deleteById(projectId);

        if(!deleted){
            throw new ProjectNotFoundException(projectId);
        }

    }


}
