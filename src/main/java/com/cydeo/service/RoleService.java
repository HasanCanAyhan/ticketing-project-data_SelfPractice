package com.cydeo.service;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;

import java.util.List;

public interface RoleService {

    //List<Role> listAllRoles();
    List<RoleDTO>listAllRoles(); // service works with dto object

    RoleDTO findById(Long id);

}
