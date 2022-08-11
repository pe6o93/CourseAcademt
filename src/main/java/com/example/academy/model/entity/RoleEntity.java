package com.example.academy.model.entity;

import com.example.academy.model.enums.RolesEnum;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private RolesEnum role;


}
