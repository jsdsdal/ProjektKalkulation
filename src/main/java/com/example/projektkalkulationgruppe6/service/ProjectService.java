package com.example.projektkalkulationgruppe6.service;

import com.example.projektkalkulationgruppe6.exception.DatabaseOperationException;
import com.example.projektkalkulationgruppe6.exception.InvalidProjectException;
import com.example.projektkalkulationgruppe6.model.Employee;
import com.example.projektkalkulationgruppe6.model.Project;
import com.example.projektkalkulationgruppe6.repository.ProjectRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(Project project) {
        validateProject(project);

        //OBS Skal denne kunne håndtere et project med subproject?

        try {
            return projectRepository.insert(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error: The project was not created.", ex);
        }
    }

    public List<String> findAllStatusList() {
        List<String> statusList = projectRepository.findAllStatusList();

        if (statusList.isEmpty()) {
            throw new DatabaseOperationException(
                    "Error: Status list could not be loaded.",
                    new Throwable("List result is empty"));
        }
        return statusList;
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



}
