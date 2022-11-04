package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    //get User based on username

    User findByUserName(String username);

    @Transactional // we can put it top of method or top of class
    void deleteByUserName(String username); // @Transactional with derived query : @Modifiying using with JPQL and Native query


    List<User> findAllByRoleDescriptionIgnoreCase(String description);
}
