package com.example.academy.service.impl;

import com.example.academy.model.entity.RoleEntity;
import com.example.academy.model.enums.RolesEnum;
import com.example.academy.repository.RolesRepository;
import com.example.academy.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RolesRepository rolesRepository;

    @Override
    public void initRoles() {
        if (this.rolesRepository.findAll().isEmpty()) {
            this.rolesRepository.saveAll(List.of(
                            new RoleEntity(RolesEnum.USER),
                            new RoleEntity(RolesEnum.TEACHER),
                            new RoleEntity(RolesEnum.ADMIN)));
        }
    }
}
