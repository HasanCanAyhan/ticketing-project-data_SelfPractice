package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    private Boolean isDeleted = false;

    @Column(nullable = false,updatable = false)
    private LocalDateTime insertDateTime;
    @Column(nullable = false,updatable = false)
    private Long insertUserId;

    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    private Long lastUpdateUserId;


    /*
    We mentioned that we will be keeping track of the actions happening in the application.
    So, this is how we do it. But there are also other ways, but this one is very simple and nice.
     */
    @PrePersist// save
    private void onPrePersist(){ // for setting
        this.insertDateTime = LocalDateTime.now();
        this.lastUpdateDateTime = LocalDateTime.now();
        this.insertUserId =1L; // who login in
        this.lastUpdateUserId=1L;

    }

    @PreUpdate // update
    private void onPreUpdate(){
        this.lastUpdateDateTime = LocalDateTime.now();
        this.lastUpdateUserId=1L;
    }

}
