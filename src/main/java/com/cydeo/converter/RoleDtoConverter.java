package com.cydeo.converter;

import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class RoleDtoConverter implements Converter<String, RoleDTO> {


    RoleService roleService;

    public RoleDtoConverter(@Lazy RoleService roleService) { //we do not need it right away, inject it when time comes, when we select one of roles
        this.roleService = roleService;
    }

    @Override
    public RoleDTO convert(String source) { // source is ex : "2"

        if (source == null || source.equals("")) {  //  Select  -> ""
            return null;
        }

        return roleService.findById(Long.parseLong(source)); //convert string to first Long id, so we can find it with indById

    }



}
