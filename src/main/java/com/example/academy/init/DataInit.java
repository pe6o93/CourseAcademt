package com.example.academy.init;

import com.example.academy.model.entity.CourseEntity;
import com.example.academy.model.entity.PictureEntity;
import com.example.academy.model.entity.UserEntity;
import com.example.academy.repository.CourseRepository;
import com.example.academy.repository.PictureRepository;
import com.example.academy.service.CourseService;
import com.example.academy.service.LessonService;
import com.example.academy.service.PictureService;
import com.example.academy.service.RoleService;
import com.example.academy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInit implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final LessonService lessonService;
    private final PictureService pictureService;
    private final PictureRepository pictureRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        this.pictureService.initPictures();
        this.roleService.initRoles();
        this.userService.initUsers();
        this.lessonService.initLessons();
        initCourses();
    }

    @Transactional
    public void initCourses() {
        String loremText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vel faucibus magna. Suspendisse sollicitudin, ipsum ut vestibulum interdum, diam purus mollis leo, non fermentum est neque quis dolor. Sed tincidunt est non est pharetra, id fermentum purus porta. Cras bibendum metus non mollis porttitor. Ut maximus mi vel justo sagittis ullamcorper. Quisque eu velit et arcu pretium interdum a eu mi. Sed eget ultrices lacus. Etiam pulvinar, ante vel dignissim consectetur, metus nisi consequat leo, sit amet posuere sem nibh non est. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed lacinia diam molestie, gravida lacus nec, porta purus. Donec in velit vitae ipsum porttitor tincidunt. Quisque bibendum libero mi, id luctus purus rhoncus ac. Nam id augue lacus. Ut tellus purus, viverra eget ultrices in, fringilla eget dui. Praesent tempor ante ac purus malesuada, ac porta leo auctor.\n" +
                "\n" +
                "Etiam lobortis lacinia varius. Proin maximus suscipit tortor, in gravida nisi. Aenean molestie, velit nec efficitur auctor, urna erat pretium ante, nec egestas eros odio vitae augue. Nulla condimentum a augue id viverra. Suspendisse interdum urna odio, at ornare magna pharetra ac. Sed non lobortis augue. Nunc quis velit vel mauris malesuada porta vestibulum porttitor leo. Praesent faucibus metus ut arcu lobortis, ac condimentum orci rutrum. Maecenas ac eros nec sapien efficitur porttitor. Fusce ac semper turpis. Duis ac justo lectus. Sed convallis eros eget augue molestie consequat. Vestibulum ac risus at risus dapibus vehicula in tempor nisi. Donec bibendum, neque a molestie hendrerit, sapien neque venenatis velit, et dictum metus tortor in odio. Nullam blandit auctor justo sit amet ultrices.";
        if (this.courseRepository.findAll().isEmpty()) {

            PictureEntity pictureEntity = this.pictureRepository.findAll().get(0);
            String videoUrl = "GZvSYJDk-us";
            UserEntity teacher = this.userService.findEntityByUsername("potrebitelka");
            CourseEntity course1 = new CourseEntity();
            course1.setTitle("Course 1");
            course1.setVideo(videoUrl);
            course1.setPoints(new BigDecimal(100));
            course1.setDescription(loremText);
//            course1.setLessons(this.lessonRepository.findAll());
            course1.setAuthor(teacher);

            CourseEntity course2 = new CourseEntity();
            course2.setTitle("Course 2");
            course2.setVideo(videoUrl);
            course2.setPoints(new BigDecimal(200));
            course2.setDescription(loremText);
            course2.setAuthor(teacher);

            CourseEntity course3 = new CourseEntity();
            course3.setTitle("Course 3");
            course3.setVideo(videoUrl);
            course3.setPoints(new BigDecimal(300));
            course3.setDescription(loremText);
            course3.setAuthor(teacher);

            CourseEntity course4 = new CourseEntity();
            course4.setTitle("Course 4");
            course4.setVideo(videoUrl);
            course4.setPoints(new BigDecimal(400));
            course4.setDescription(loremText);
            course4.setAuthor(teacher);

            CourseEntity course5 = new CourseEntity();
            course5.setTitle("Course 5");
            course5.setVideo(videoUrl);
            course5.setPoints(new BigDecimal(500));
            course5.setDescription(loremText);
            course5.setAuthor(teacher);

            CourseEntity course6 = new CourseEntity();
            course6.setTitle("Course 6");
            course6.setVideo(videoUrl);
            course6.setPoints(new BigDecimal(600));
            course6.setDescription(loremText);
            course6.setAuthor(teacher);

            List<CourseEntity> courses = List.of(course1, course2, course3, course4, course5, course6);
            courses.forEach(courseEntity -> courseEntity.setPicture(pictureEntity));
            this.courseRepository.saveAll(courses);
            teacher.setCourses(courses);
        }
    }
}
