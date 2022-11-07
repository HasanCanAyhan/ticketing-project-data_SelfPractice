package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.hibernate.annotations.LazyCollection;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    //service impl ->> repo -->> DB

    private  final  UserRepository userRepository;

    private final UserMapper userMapper;

    private final ProjectService projectService;

    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @Override
    public List<UserDTO> listAllUsers() {


        List<User> userList = userRepository.findAll(Sort.by("firstName"));

        return userList.stream().map(user -> userMapper.convertToDto(user) ).collect(Collectors.toList());


    }

    @Override
    public UserDTO findByUserName(String username) {

        return userMapper.convertToDto(userRepository.findByUserName(username));

    }

    @Override
    public void save(UserDTO user) { // it is coming from UI-Part : save button
       userRepository.save( userMapper.convertToEntity(user) );
    }

    @Override
    public void deleteByUserName(String username) { // deletes everything from DB related to user

                userRepository.deleteByUserName(username);


    }

    @Override
    public UserDTO update(UserDTO user) { // updated user

        //find current user for id

        User user1 = userRepository.findByUserName(user.getUserName()); // has id

        //map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user); // has no id

        //set id to the converted object

        convertedUser.setId(user1.getId());

        //save the updated user in the db

        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());



    }

    @Override
    public void delete(String username) { // delete method will delete user only from UI-Part, nor from DB

        //find user from db with userName
        //change the isDeleted field to true
        //save the object in the db

        User user = userRepository.findByUserName(username);

        if (checkIfUserCanBeDeleted(user)){
            user.setIsDeleted(true); // just give info for DB Part
            userRepository.save(user);
        }



        //we will delete from UI-Part , not from DB

        //We only want to get the ones that is not deleted,so it means is_deleted needs to be false.
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);

        return users.stream()
                .map(managers -> userMapper.convertToDto(managers))
                .collect(Collectors.toList());

    }

    private boolean checkIfUserCanBeDeleted(User user){

        switch (user.getRole().getDescription()){

            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                return taskDTOList.size() == 0;
            default:
                return true;
        }

    }


}
