package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {

    //get User based on username

    User findByUserName(String username);

    @Transactional
    void deleteByUserName(String username); // derived query : @Modifiying using with JPQL and Native query

}
