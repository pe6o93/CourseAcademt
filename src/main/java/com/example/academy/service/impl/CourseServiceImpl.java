package com.example.academy.service.impl;

import com.example.academy.model.dto.AddCourseDTO;
import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.model.entity.PictureEntity;
import com.example.academy.model.entity.UserEntity;
import com.example.academy.repository.CourseRepository;
import com.example.academy.repository.LessonRepository;
import com.example.academy.repository.PictureRepository;
import com.example.academy.repository.UserRepository;
import com.example.academy.service.CourseService;
import com.example.academy.service.PictureService;
import com.example.academy.service.UserService;
import com.example.academy.web.exeption.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    public static final String VIDEO_REGEX = "(?<=v=).+";
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final LessonRepository lessonRepository;
    private final PictureService pictureService;
    private final PictureRepository pictureRepository;

    @Override
    public List<CourseDTO> mapCoursesToDTO(List<CourseEntity> courses) {
        return courses.stream().map(courseEntity -> {
            CourseDTO map = this.modelMapper.map(courseEntity, CourseDTO.class);
            map.setImageURL(courseEntity.getPicture().getUrl());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public CourseDTO addCourse(AddCourseDTO addCourseDTO, String username) throws IOException {
        CourseEntity course = this.modelMapper.map(addCourseDTO, CourseEntity.class);
        PictureEntity pictureEntity = this.pictureService.savePictureFromMultipartFile(addCourseDTO.getPicture());
        course.setPicture(pictureEntity);
        UserEntity user = this.userService.findEntityByUsername(username);
        course.setAuthor(user);
        if (course.getVideo() != null) {
            course.setVideo(videoURLConvertForIframe(course.getVideo()));
        }
        List<CourseEntity> courses = user.getCourses();
        courses.add(course);
        user.setCourses(courses);
        this.userRepository.save(user);
        this.courseRepository.save(course);
        addCourseDTO.setId(course.getId());
        return mapAddCourseToCourseDTO(addCourseDTO, pictureEntity.getUrl());
    }

    private CourseDTO mapAddCourseToCourseDTO(AddCourseDTO addCourseDTO, String url) {
        CourseDTO map = this.modelMapper.map(addCourseDTO, CourseDTO.class);
        map.setImageURL(url);
        return map;
    }

    @Override
    public boolean checkIfCourseExist(Integer id) {
        return this.courseRepository.existsById(id);
    }

    @Override
    public CourseDTO findById(Integer id) {
        CourseEntity course = this.courseRepository.findById(id).orElseThrow();
        CourseDTO courseDTO = this.modelMapper.map(course, CourseDTO.class);
        courseDTO.setCreated(course.getCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        courseDTO.setAuthorUsername(course.getAuthor().getUsername());
        return courseDTO;
    }

    @Override
    public List<CourseDTO> getLastCoursesToDTO() {
        List<CourseEntity> createdLast3 = this.courseRepository.getCoursesOrderedByCreatedLast3();
        createdLast3.forEach(c -> {
            if (c.getDescription().length() > 50) {
                c.setDescription(c.getDescription().substring(0, 50) + "...");
            }
        });
        return mapCoursesToDTO(createdLast3);
    }

    @Override
    public Page<CourseDTO> findPaginated(PageRequest pageable) {
        List<CourseDTO> dtos = this.courseRepository.findAll().stream().map(courseEntity -> {
            CourseDTO c = this.modelMapper.map(courseEntity, CourseDTO.class);
            c.setImageURL(courseEntity.getPicture().getUrl());
            if (c.getDescription().length() > 80) {
                c.setDescription(c.getDescription().substring(0, 80) + "...");
            }
            return c;
        }).collect(Collectors.toList());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<CourseDTO> list;

        if (dtos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dtos.size());
            list = dtos.subList(startItem, toIndex);
        }
        return new PageImpl<CourseDTO>(list, PageRequest.of(currentPage, pageSize), dtos.size());
    }

    @Override
    public void update(CourseDTO courseDTO) {
        CourseEntity course = this.courseRepository.findById(courseDTO.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Course not found!"));
        course.setTitle(courseDTO.getTitle());
        if (!Objects.equals(course.getVideo(), courseDTO.getVideo())) {
            course.setVideo(videoURLConvertForIframe(course.getVideo()));
        }
        course.setPoints(courseDTO.getPoints());
        //TODO
        //  course.setPicture(courseDTO.getPicture());
        course.setDescription(courseDTO.getDescription());
        this.courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        this.courseRepository.deleteById(courseId);
    }

    @Override
    public void addLessonToCourse(LessonDTO lessonDTO, Integer courseId) {
        //add lesson to course
        LessonEntity lesson = this.modelMapper.map(lessonDTO, LessonEntity.class);
        CourseEntity course = this.courseRepository.findById(courseId).orElseThrow();
        lesson.setCourse(course);

        this.courseRepository.save(course);
        this.lessonRepository.save(lesson);
        List<LessonEntity> lessons = this.lessonRepository.findAllByCourseId(courseId);
        lessons.add(lesson);
        lessonDTO.setId(lesson.getId());
    }

    @Override
    public BigDecimal getCoursePointById(Integer id) {
       return this.courseRepository.getCoursePointsById(id);
    }

    @Override
    public void addCourseToUser(Integer courseId, String username) {
        final UserDTO userDTO = this.userService.findByUsername(username);
        BigDecimal coursePoints = this.courseRepository.getCoursePointsById(courseId);
        if (userDTO.getPoints().compareTo(coursePoints) > 0) {
            this.userService.addUserAddCourse(userDTO.getId(), courseId);
        }
    }

    @Override
    public List<CourseDTO> getCourseByUsername(String username) {
        UserDTO userDTO = this.userService.findByUsername(username);
        return userDTO.getCourses();
    }

    private String videoURLConvertForIframe(String video) {
        Matcher m = Pattern.compile(VIDEO_REGEX).matcher(video);
        String lastPartFromUrl = "";
        if (m.find()) {
            lastPartFromUrl = m.group();
        }
        return lastPartFromUrl;
    }

}
