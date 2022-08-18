package com.example.academy.model.entity;

import com.example.academy.model.enums.GenderEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Size(max = 30)
    private String firstName;
    @Size(max = 30)
    private String lastName;
    @Column(name = "username",length = 30,unique = true,nullable = false)
    private String username;

    private String password;
    @Column(length = 50)
    private String email;
    private String picture;

    private Byte age;

    private BigDecimal points=BigDecimal.ZERO;

    @Enumerated(EnumType.ORDINAL)
    private GenderEnum gender;


  // @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
   private List<CourseEntity> courses=new ArrayList<>();

  // @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<RoleEntity> roles;
}
