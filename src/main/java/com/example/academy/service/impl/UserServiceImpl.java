package com.example.academy.service.impl;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.dto.RegisterDto;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.model.entity.RoleEntity;
import com.example.academy.model.entity.UserEntity;
import com.example.academy.model.enums.GenderEnum;
import com.example.academy.model.enums.RolesEnum;
import com.example.academy.repository.CourseRepository;
import com.example.academy.repository.LessonRepository;
import com.example.academy.repository.RolesRepository;
import com.example.academy.repository.UserRepository;
import com.example.academy.service.UserService;
import com.example.academy.web.exeption.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    private final LessonRepository lessonRepository;

    @Override
    public boolean isUserNameFree(String username) {
        return this.userRepository.findUserEntityByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailFree(String email) {
        return this.userRepository.findUserEntitiesByEmail(email).isEmpty();
    }

    @Override
    public void registerAndLoginUser(RegisterDto registerDto) {

        UserEntity user = this.modelMapper.map(registerDto, UserEntity.class);
        user.setRoles(List.of(this.rolesRepository.getRoleEntityByRole(registerDto.getRole())));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        this.userRepository.save(user);
        UserDetails principal = userDetailService.loadUserByUsername(user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                principal.getAuthorities()
        );
        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
    }

    @Transactional
    @Override
    public UserDTO findByUsername(String name) {
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(name).orElseThrow();
        return userToUserDTO(userEntity);
    }

    @Override
    public UserEntity findEntityByUsername(String name) {
        return this.userRepository.findUserEntityByUsername(name).orElseThrow();
    }

    @Override
    public void update(UserDTO userDTO) {
        UserEntity userEntity = this.userRepository.findById(userDTO.getId()).orElseThrow();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setAge(userDTO.getAge());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        this.userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserDTO findUserByCourseId(Integer id) {
        Integer authorId = this.courseRepository.findById(id).orElseThrow().getAuthor().getId();
        UserEntity user = this.userRepository.findById(authorId).orElseThrow();
        return userToUserDTO(user);
    }

    @Override
    public List<UserDTO> getLastTeachersToDTO() {
        List<UserEntity> teachers = this.userRepository.findLast3Teachers(Pageable.ofSize(3));
        return teachers.stream()
                .filter(t -> t.getRoles().stream().noneMatch(r -> r.getRole() == RolesEnum.ADMIN))
                .map(t -> this.modelMapper.map(t, UserDTO.class)).toList();
    }

    @Override
    public Page<UserDTO> findPaginatedTeachers(PageRequest pageable) {
        List<UserEntity> usersList = this.userRepository.findUserEntitiesByRolesContainingTeacher();
        List<UserDTO> userDTOS = usersList.stream().filter(t -> t.getRoles().stream().noneMatch(r -> r.getRole() == RolesEnum.ADMIN)).map(this::userToUserDTO).toList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<UserDTO> list;
        if (userDTOS.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, userDTOS.size());
            list = userDTOS.subList(startItem, toIndex);
        }
        return new PageImpl<UserDTO>(list, PageRequest.of(currentPage, pageSize), userDTOS.size());
    }

    @Override
    public Boolean checkIfUserHaveThisCourse(Integer courseId, String username) {
        UserEntity user = this.userRepository.findUserEntityByUsername(username).orElseThrow();
        return user.getCourses().stream().anyMatch(c -> Objects.equals(c.getId(), courseId));
    }

    @Override
    public void addUserAddCourse(Integer userId, Integer courseId) {
        //TODO
        UserEntity user = this.userRepository.findById(userId).orElseThrow();
        CourseEntity course = this.courseRepository.findById(courseId).orElseThrow();
        user.setPoints(user.getPoints().subtract(course.getPoints()));
        if (user.getCourses().isEmpty()) {
            user.setCourses(List.of(course));
        } else {
            user.getCourses().add(course);
        }
        this.userRepository.save(user);
    }

    @Override
    public void addMorePoints(String points, String username) {
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Няма обект с такъв Username."));
        userEntity.setPoints(userEntity.getPoints().add(new BigDecimal(points)));
        this.userRepository.save(userEntity);
    }

    @Override
    public List<LessonDTO> findLessonsByCourse(Integer courseId) {
        List<LessonEntity> lessons = this.lessonRepository.findAllByCourseId(courseId);
        return lessons.stream().map(l -> {
            LessonDTO lessonDTO = this.modelMapper.map(l, LessonDTO.class);
            lessonDTO.setCourse(this.modelMapper.map(l.getCourse(),CourseDTO.class));
            lessonDTO.setCreated(l.getCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            return lessonDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIfUserIsAuthor(UserDTO authorDto, String username) {
        return this.userRepository.findUserEntityByUsername(username).orElseThrow().getId() == authorDto.getId();
    }

    @Transactional
    @Override
    public void initUsers() {
        if (this.userRepository.findAll().isEmpty()) {
            UserEntity user = new UserEntity();
            user.setFirstName("Petar");
            user.setLastName("Petrov");
            user.setPassword(passwordEncoder.encode("parola"));
            user.setUsername("petar1");
            user.setAge((byte) 23);
            user.setEmail("petar@abv.bg");
            user.setGender(GenderEnum.MALE);
            user.setRoles(List.of(this.rolesRepository.getRoleEntityByRole(RolesEnum.USER)));

            UserEntity teacher = new UserEntity();
            teacher.setFirstName("Desimira");
            teacher.setLastName("Petrova");
            teacher.setPassword(passwordEncoder.encode("parola"));
            teacher.setUsername("desimira");
            teacher.setAge((byte) 21);
            teacher.setEmail("desimira@abv.bg");
            teacher.setGender(GenderEnum.FEMALE);
            teacher.setRoles(List.of(
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.USER),
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.TEACHER)));


            UserEntity admin = new UserEntity();
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setPassword(passwordEncoder.encode("parola"));
            admin.setUsername("administrator");
            admin.setAge((byte) 22);
            admin.setEmail("admin@abv.bg");
            admin.setGender(GenderEnum.MALE);
            admin.setRoles(List.of(
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.USER),
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.TEACHER),
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.ADMIN)));



            this.userRepository.save(user);
            this.userRepository.save(teacher);
            this.userRepository.save(admin);
        }

    }

    @Override
    public String getBiggestRole(UserDTO userDTO) {
        String s = userDTO.getRoles().get(userDTO.getRoles().size() - 1).getRole().toString();
        if (s.equals("USER")) {
            s = "Курсист";
        } else {
            s = "Преподавател";
        }
        return s;
    }

    @Override
    public UserDTO findByIdAndMapToDTO(Integer id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow();
        return userToUserDTO(userEntity);
    }


    private List<CourseDTO> mapCoursesToDTO(UserEntity userEntity) {
        List<CourseEntity> courses = userEntity.getCourses();
        return courses.stream().map(c -> {
                    CourseDTO map = this.modelMapper.map(c, CourseDTO.class);
                    map.setCreated(localDateTimeString(c.getCreated()));
                    return map;
                }
        ).toList();
    }

    public static String localDateTimeString(LocalDateTime c) {
        return c.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private UserDTO userToUserDTO(UserEntity userEntity) {
        UserDTO userDTO = this.modelMapper.map(userEntity, UserDTO.class);
        userDTO.setCourses(mapCoursesToDTO(userEntity));
        return userDTO;
    }
}
