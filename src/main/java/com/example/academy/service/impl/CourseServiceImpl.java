package com.example.academy.service.impl;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.repository.CourseRepository;
import com.example.academy.repository.LessonRepository;
import com.example.academy.service.CourseService;
import com.example.academy.service.UserService;
import com.example.academy.web.exeption.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final LessonRepository lessonRepository;


    @Override
    public List<CourseDTO> mapCoursesToDTO(List<CourseEntity> courses) {
        return courses.stream().map(courseEntity -> this.modelMapper.map(courseEntity, CourseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO, String name) {
        CourseEntity course = this.modelMapper.map(courseDTO, CourseEntity.class);
        course.setAuthor(this.userService.findEntityByUsername(name));
        if (course.getVideo() != null) {
            course.setVideo(videoURLConvertForIframe(course.getVideo()));
        }
        this.courseRepository.save(course);
        courseDTO.setId(course.getId());
        return courseDTO;
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
        createdLast3.forEach(c->{
            if (c.getDescription().length() > 50) {
                c.setDescription(c.getDescription().substring(0, 50)+"...");
            }
            return;
        });
        return mapCoursesToDTO(createdLast3);
    }

    @Override
    public Page<CourseDTO> findPaginated(PageRequest pageable) {
        List<CourseDTO> dtos= this.courseRepository.findAll().stream().map(courseEntity -> {
            CourseDTO c = this.modelMapper.map(courseEntity, CourseDTO.class);
            if (c.getDescription().length() > 80) {
                c.setDescription(c.getDescription().substring(0, 80)+"...");
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
        }        course.setPoints(courseDTO.getPoints());
        course.setPicture(courseDTO.getPicture());
        course.setDescription(courseDTO.getDescription());
        this.courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        this.courseRepository.deleteById(courseId);
    }

    @Override
    public LessonDTO addLessonToCourse(LessonDTO lessonDTO, Integer courseId) {
        //add lesson to course
        LessonEntity lesson = this.modelMapper.map(lessonDTO, LessonEntity.class);
        CourseEntity course = this.courseRepository.findById(courseId).orElseThrow();
        this.lessonRepository.save(lesson);
        List<LessonEntity> lessons = course.getLessons();
        lessons.add(lesson);

        course.setLessons(lessons);
        this.courseRepository.save(course);
        lessonDTO.setId(lesson.getId());
        return lessonDTO;
    }

    @Override
    public CourseDTO findByLessonId(Integer id) {
        LessonEntity lesson = this.lessonRepository.findById(id).orElseThrow();
        CourseEntity course = this.courseRepository.findAll().stream().filter(c -> c.getLessons().contains(lesson)).findAny().orElseThrow();
        return this.modelMapper.map(course,CourseDTO.class);
    }

    @Override
    public void initCourses() {
        String loremText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vel faucibus magna. Suspendisse sollicitudin, ipsum ut vestibulum interdum, diam purus mollis leo, non fermentum est neque quis dolor. Sed tincidunt est non est pharetra, id fermentum purus porta. Cras bibendum metus non mollis porttitor. Ut maximus mi vel justo sagittis ullamcorper. Quisque eu velit et arcu pretium interdum a eu mi. Sed eget ultrices lacus. Etiam pulvinar, ante vel dignissim consectetur, metus nisi consequat leo, sit amet posuere sem nibh non est. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed lacinia diam molestie, gravida lacus nec, porta purus. Donec in velit vitae ipsum porttitor tincidunt. Quisque bibendum libero mi, id luctus purus rhoncus ac. Nam id augue lacus. Ut tellus purus, viverra eget ultrices in, fringilla eget dui. Praesent tempor ante ac purus malesuada, ac porta leo auctor.\n" +
                "\n" +
                "Etiam lobortis lacinia varius. Proin maximus suscipit tortor, in gravida nisi. Aenean molestie, velit nec efficitur auctor, urna erat pretium ante, nec egestas eros odio vitae augue. Nulla condimentum a augue id viverra. Suspendisse interdum urna odio, at ornare magna pharetra ac. Sed non lobortis augue. Nunc quis velit vel mauris malesuada porta vestibulum porttitor leo. Praesent faucibus metus ut arcu lobortis, ac condimentum orci rutrum. Maecenas ac eros nec sapien efficitur porttitor. Fusce ac semper turpis. Duis ac justo lectus. Sed convallis eros eget augue molestie consequat. Vestibulum ac risus at risus dapibus vehicula in tempor nisi. Donec bibendum, neque a molestie hendrerit, sapien neque venenatis velit, et dictum metus tortor in odio. Nullam blandit auctor justo sit amet ultrices.";
        if (this.courseRepository.findAll().isEmpty()) {
            CourseEntity course1 = new CourseEntity();
            course1.setTitle("Course 1");
            course1.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course1.setPoints(new BigDecimal(100));
            course1.setDescription(loremText);

            CourseEntity course2 = new CourseEntity();
            course2.setTitle("Course 2");
            course2.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course2.setPoints(new BigDecimal(200));
            course2.setDescription(loremText);

            CourseEntity course3 = new CourseEntity();
            course3.setTitle("Course 3");
            course3.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course3.setPoints(new BigDecimal(300));
            course3.setDescription(loremText);

            CourseEntity course4 = new CourseEntity();
            course4.setTitle("Course 4");
            course4.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course4.setPoints(new BigDecimal(400));
            course4.setDescription(loremText);

            CourseEntity course5 = new CourseEntity();
            course5.setTitle("Course 5");
            course5.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course5.setPoints(new BigDecimal(500));
            course5.setDescription(loremText);

            CourseEntity course6 = new CourseEntity();
            course6.setTitle("Course 6");
            course6.setVideo("https://www.youtube.com/watch?v=her_7pa0vrg&t=1823s");
            course6.setPoints(new BigDecimal(600));
            course6.setDescription(loremText);

            List<CourseEntity> course11 = List.of(
                    course1, course2, course3, course4, course5, course6
            );
            this.courseRepository.save(course1);
            this.courseRepository.save(course2);
            this.courseRepository.save(course3);
            this.courseRepository.save(course4);
            this.courseRepository.save(course5);
            this.courseRepository.save(course6);


        }
    }

    private String videoURLConvertForIframe(String video) {
        Matcher m = Pattern.compile("(?<=v=).+").matcher(video);
        String lastPartFromUrl = "";
        if (m.find()) {
            lastPartFromUrl = m.group();
        }
        return lastPartFromUrl;
    }

}
