package com.example.academy.service;

import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.dto.RegisterDto;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    boolean isUserNameFree(String username);

    boolean isEmailFree(String email);

    void registerAndLoginUser(RegisterDto registerDto);

    UserDTO findByUsername(String name);
    UserEntity findEntityByUsername(String name);

    void initUsers();

    String getBiggestRole(UserDTO userDTO);

    UserDTO findByIdAndMapToDTO(Integer id);

    void update(UserDTO userDTO);

    UserDTO findUserByCourseId(Integer id);

    List<UserDTO> getLastTeachersToDTO();

    Page<UserDTO> findPaginatedTeachers(PageRequest pageable);

    Boolean checkIfUserHaveThisCourse(Integer courseId, String username);

    void addUserAddCourse(Integer userId, Integer courseId);

    void addMorePoints(String points, String username);

    List<LessonDTO> findLessonsByCourse(Integer courseId);

    Boolean checkIfUserIsAuthor(UserDTO authorDto, String username);
}
