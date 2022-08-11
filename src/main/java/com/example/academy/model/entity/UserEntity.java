package com.example.academy.model.entity;

import com.example.academy.model.enums.GenderEnum;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private Byte age;

    private BigDecimal points=BigDecimal.ZERO;

    @Enumerated(EnumType.ORDINAL)
    private GenderEnum gender;


   @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE})
   private List<CourseEntity> courses=new ArrayList<>();

   @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private List<RoleEntity> roles;
}
