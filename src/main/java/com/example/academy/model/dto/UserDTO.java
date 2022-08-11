package com.example.academy.model.dto;

import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.RoleEntity;
import com.example.academy.model.enums.GenderEnum;
import com.example.academy.model.enums.RolesEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Byte age;
    private GenderEnum gender;
    private BigDecimal points;
    private List<CourseDTO> courses=new ArrayList<>();
    private List<RoleEntity>  roles;
}
