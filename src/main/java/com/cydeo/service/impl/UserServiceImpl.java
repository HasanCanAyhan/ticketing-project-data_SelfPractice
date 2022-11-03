package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    //service impl ->> repo -->> DB

    private  final  UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
    public void deleteByUserName(String username) {

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
}
