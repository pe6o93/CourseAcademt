package com.example.academy.repository;

import com.example.academy.model.entity.RoleEntity;
import com.example.academy.model.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository  extends JpaRepository<RoleEntity, Integer> {

    RoleEntity getRoleEntityByRole(RolesEnum role);
}
