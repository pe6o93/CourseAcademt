package com.example.academy.service.impl;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.dto.RegisterDto;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.model.entity.PictureEntity;
import com.example.academy.model.entity.UserEntity;
import com.example.academy.model.enums.GenderEnum;
import com.example.academy.model.enums.RolesEnum;
import com.example.academy.repository.CourseRepository;
import com.example.academy.repository.LessonRepository;
import com.example.academy.repository.PictureRepository;
import com.example.academy.repository.RolesRepository;
import com.example.academy.repository.UserRepository;
import com.example.academy.service.PictureService;
import com.example.academy.service.UserService;
import com.example.academy.util.DateUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private final PictureService pictureService;
    private final PictureRepository pictureRepository;

    @Override
    public boolean isUserNameFree(String username) {
        return this.userRepository.findUserEntityByUsername(username).isEmpty();

    }

    @Override
    public boolean isEmailFree(String email) {
        return this.userRepository.findUserEntitiesByEmail(email).isEmpty();
    }

    @Transactional
    @Override
    public void registerAndLoginUser(RegisterDto registerDto) throws IOException {

        UserEntity user = this.modelMapper.map(registerDto, UserEntity.class);
        if (!registerDto.getPicture().isEmpty()) {
            user.setPicture(this.pictureService.savePictureFromMultipartFile(registerDto.getPicture()));
        } else {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setUrl("data:image/png;base64,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");
            pictureEntity.setTitle("Example");
            user.setPicture(pictureRepository.findById(1).get());
        }
        user.setRoles(List.of(this.rolesRepository.getRoleEntityByRole(registerDto.getRole())));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        this.userRepository.save(user);
        UserDetails principal = this.userDetailService.loadUserByUsername(user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                principal.getAuthorities()
        );
        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
    }

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
                .map(t -> {
                    UserDTO map = this.modelMapper.map(t, UserDTO.class);
                    map.setImageURL(t.getPicture().getUrl());
                    return map;
                }).toList();
    }

    @Override
    public Page<UserDTO> findPaginatedTeachers(PageRequest pageable) {
        List<UserEntity> usersList = this.userRepository.findUserEntitiesByRolesContainingTeacher();
        List<UserDTO> userDTOS = usersList.stream()
                .filter(t -> t.getRoles().stream().noneMatch(r -> r.getRole() == RolesEnum.ADMIN))
                .map(this::userToUserDTO).toList();
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
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Няма обект с такъв Username."));
        userEntity.setPoints(userEntity.getPoints().add(new BigDecimal(points)));
        this.userRepository.save(userEntity);
    }

    @Override
    public List<LessonDTO> findLessonsByCourse(Integer courseId) {
        List<LessonEntity> lessons = this.lessonRepository.findAllByCourseId(courseId);
        return lessons.stream().map(l -> {
            LessonDTO lessonDTO = this.modelMapper.map(l, LessonDTO.class);
            lessonDTO.setCourse(this.modelMapper.map(l.getCourse(), CourseDTO.class));
            String formatDate = DateUtil.formatDate(l.getCreated());
            lessonDTO.setCreated(formatDate);
            return lessonDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIfUserIsAuthor(UserDTO authorDto, String username) {
        return Objects.equals(this.userRepository.findUserEntityByUsername(username).orElseThrow().getId(), authorDto.getId());
    }

    @Transactional
    @Override
    public void initUsers() {
        if (this.userRepository.findAll().isEmpty()) {
            PictureEntity pictureEntity = this.pictureRepository.findAll().get(0);
            UserEntity user = new UserEntity();
            user.setFirstName("Petar");
            user.setLastName("Petrov");
            user.setPassword(passwordEncoder.encode("parola"));
            user.setUsername("petar1");
            user.setAge((byte) 23);
            user.setEmail("petar@abv.bg");
            user.setGender(GenderEnum.MALE);
            user.setRoles(List.of(this.rolesRepository.getRoleEntityByRole(RolesEnum.USER)));
            user.setPicture(pictureEntity);

            UserEntity teacher = new UserEntity();
            teacher.setFirstName("Maria");
            teacher.setLastName("Petrova");
            teacher.setPassword(passwordEncoder.encode("parola"));
            teacher.setUsername("potrebitelka");
            teacher.setAge((byte) 21);
            teacher.setEmail("emailAdress@abv.bg");
            teacher.setGender(GenderEnum.FEMALE);
            teacher.setRoles(List.of(
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.USER),
                    this.rolesRepository.getRoleEntityByRole(RolesEnum.TEACHER)));
            teacher.setPicture(pictureEntity);


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
            admin.setPicture(pictureEntity);
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
                    map.setCreated(DateUtil.formatDate(c.getCreated()));
                    return map;
                }
        ).toList();
    }

    private UserDTO userToUserDTO(UserEntity userEntity) {
        UserDTO userDTO = this.modelMapper.map(userEntity, UserDTO.class);
        userDTO.setCourses(mapCoursesToDTO(userEntity));
        userDTO.setImageURL(userEntity.getPicture().getUrl());
        return userDTO;
    }
}
