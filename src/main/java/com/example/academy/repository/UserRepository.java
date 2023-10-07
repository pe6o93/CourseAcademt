package com.example.academy.repository;

import com.example.academy.model.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserEntityByUsername(String username);
    Optional<UserEntity> findUserEntitiesByEmail(String email);

    UserEntity findByUsername(String username);

@Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE  r.role='TEACHER'")
    List<UserEntity> findLast3Teachers(Pageable pageable);

@Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE  r.role='TEACHER'")
List<UserEntity> findUserEntitiesByRolesContainingTeacher();

}
