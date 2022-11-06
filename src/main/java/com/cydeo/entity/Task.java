package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@Where(clause = "is_deleted=false")
public class Task extends BaseEntity{


    private String taskSubject;

    private String taskDetail;

    @Enumerated(EnumType.STRING)
    private Status taskStatus;

    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;

    private Boolean isDeleted = false;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private User assignedEmployee;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    /*Error : bcs projectDto did not have id
    TransientPropertyValueException: object references an unsaved transient instance -
    save the transient instance before flushing :
    com.cydeo.entity.Task.project -> com.cydeo.entity.Project
    nested exception is java.lang.IllegalStateException: org.hibernate.TransientPropertyValueException:
    object references an unsaved transient instance - save the transient instance before flushing :
    com.cydeo.entity.Task.project -> com.cydeo.entity.Project
     */













}
