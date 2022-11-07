package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final UserService userService;
    private final UserMapper userMapper;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {

        Project project = projectRepository.findByProjectCode(code);

        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {

        List<Project> projectList = projectRepository.findAll(Sort.by("projectCode"));

        List<ProjectDTO> projectDTOList = projectList.stream()
                .map(project -> projectMapper.convertToDto(project))
                .collect(Collectors.toList());

        return projectDTOList;

    }

    @Override
    public void save(ProjectDTO dto) {

        dto.setProjectStatus(Status.OPEN);

        projectRepository.save(projectMapper.convertToEntity(dto));

    }

    @Override
    public void update(ProjectDTO dto) {

        Project project = projectRepository.findByProjectCode(dto.getProjectCode()); //has id

        Project convertedProject = projectMapper.convertToEntity(dto);

        convertedProject.setId(project.getId());

        convertedProject.setProjectStatus(project.getProjectStatus());//bcs it is not inside the form

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {


        //find user from db with projectCode
        //change the isDeleted field to true
        //save the object in the db

        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true); // just give info for DB Part

        //                                                      another unique thing
        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); // SPOO-1 : with that, I can use same projectCode again while creating new project

        projectRepository.save(project);


        taskService.deleteByProject(projectMapper.convertToDto(project));

    }

    @Override
    public void complete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDto(project));

    }


    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        /*
        We can do it, but it is not a good practice to call some another repository from some Service.
        For example it is ok to call UserRepository from UserServiceImpl.
        Or it is ok to call ProjectRepository from PprojectServiceImpl.
        But it is not ok to call UserRepository from ProjectServiceImpl etc.

        It is because you will see in the microservices later, we will not be able to reach the UserRepository from ProjectServiceImpl at all.
        Also we won't be able to reach the ProjectRepository from UserServiceImpl etc.
         */

        UserDTO currentUserDto = userService.findByUserName("harold@manager.com"); // it will come with Security -lesson

        User user = userMapper.convertToEntity(currentUserDto);

        //go to DB, give me all the projects assigned zo manager login in the system

        List<Project> list = projectRepository.findAllByAssignedManager(user); // projectList belongs to this manager
        // but project entity does not have those 2 fields : unfinished and completed tasks
        //plus + we should have 2 fields : project view in the UI Part need these fields.

        return list.stream().map(project -> {

            ProjectDTO obj = projectMapper.convertToDto(project); // convert it to dto bcs projectDto has 2 fields

            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));

            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

            return obj;

        }).collect(Collectors.toList());


    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {

        List<Project> projects = projectRepository
                .findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, userMapper.convertToEntity(assignedManager));

        return projects.stream().map(project -> projectMapper.convertToDto(project))
                .collect(Collectors.toList());

    }


}
