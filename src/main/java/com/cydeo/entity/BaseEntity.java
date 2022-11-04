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

    /*
nullable = false means in SQL "NOT NULL" Constraint
updatable = false means, once you assign something into that field, you can not update it later. Like using final in Java.

We put nullable = false on all those fields, because we want to keep track of who did some change in database, and when was it.
So for that, we need to keep track of who inserted something in database(insertUserId), we need to keep track of when something is inserted into database (insertDateTime), we need to keep track of who updated(changed) something in database(lastUpdateUserId), we need to keep track of when something updated(changed) in database(lastUpdateDateTime)

With nullable = false, we are making sure that those records will be saved into database.
With updatable false, we are making sure that insertUserId and insertDateTime are not changeable/ updateable. Because, if something is inserted by someone, someone else can not insert the same thing again later, only thing they can do is to update it.
     */


}
