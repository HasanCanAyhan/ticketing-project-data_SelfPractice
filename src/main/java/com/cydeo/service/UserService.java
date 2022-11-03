package com.cydeo.service;


import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService  {

    List<UserDTO> listAllUsers(); // controller will call theis method

    UserDTO findByUserName(String username); // username is unique : for update business logic
    void save(UserDTO userDTO);
    void deleteByUserName(String username);
    UserDTO update(UserDTO user);


}
