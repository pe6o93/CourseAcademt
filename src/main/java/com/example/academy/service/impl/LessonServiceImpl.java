package com.example.academy.service.impl;

import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.repository.LessonRepository;
import com.example.academy.service.LessonService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public LessonDTO findById(Integer id) {

        LessonEntity lessonEntity = this.lessonRepository.findById(id).orElse(null);
        LessonDTO lessonDTO = this.modelMapper.map(lessonEntity, LessonDTO.class);
        lessonDTO.setCreated(lessonEntity.getCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return lessonDTO;
    }

    @Override
    public void deleteById(Integer id) {
        this.lessonRepository.deleteById(id);
    }

    @Override
    public void updateLesson(LessonDTO lessonDTO) {
        LessonEntity lessonEntity = this.lessonRepository.findById(lessonDTO.getId()).orElseThrow();
        lessonEntity.setTitle(lessonDTO.getTitle());
        lessonEntity.setDescription(lessonDTO.getDescription());
        this.lessonRepository.save(lessonEntity);
    }

    @Override
    public void initLessons() {
        if(this.lessonRepository.findAll().isEmpty()) {
            String loremText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vel faucibus magna. Suspendisse sollicitudin, ipsum ut vestibulum interdum, diam purus mollis leo, non fermentum est neque quis dolor. Sed tincidunt est non est pharetra, id fermentum purus porta. Cras bibendum metus non mollis porttitor. Ut maximus mi vel justo sagittis ullamcorper. Quisque eu velit et arcu pretium interdum a eu mi. Sed eget ultrices lacus. Etiam pulvinar, ante vel dignissim consectetur, metus nisi consequat leo, sit amet posuere sem nibh non est. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed lacinia diam molestie, gravida lacus nec, porta purus. Donec in velit vitae ipsum porttitor tincidunt. Quisque bibendum libero mi, id luctus purus rhoncus ac. Nam id augue lacus. Ut tellus purus, viverra eget ultrices in, fringilla eget dui. Praesent tempor ante ac purus malesuada, ac porta leo auctor.\n" +
                    "\n" +
                    "Etiam lobortis lacinia varius. Proin maximus suscipit tortor, in gravida nisi. Aenean molestie, velit nec efficitur auctor, urna erat pretium ante, nec egestas eros odio vitae augue. Nulla condimentum a augue id viverra. Suspendisse interdum urna odio, at ornare magna pharetra ac. Sed non lobortis augue. Nunc quis velit vel mauris malesuada porta vestibulum porttitor leo. Praesent faucibus metus ut arcu lobortis, ac condimentum orci rutrum. Maecenas ac eros nec sapien efficitur porttitor. Fusce ac semper turpis. Duis ac justo lectus. Sed convallis eros eget augue molestie consequat. Vestibulum ac risus at risus dapibus vehicula in tempor nisi. Donec bibendum, neque a molestie hendrerit, sapien neque venenatis velit, et dictum metus tortor in odio. Nullam blandit auctor justo sit amet ultrices.";

            LessonEntity lesson1 = new LessonEntity();
            lesson1.setTitle("Lesson 1");
            lesson1.setDescription(loremText);

            LessonEntity lesson2 = new LessonEntity();
            lesson2.setTitle("Lesson 2");
            lesson2.setDescription(loremText);

            LessonEntity lesson3 = new LessonEntity();
            lesson3.setTitle("Lesson 3");
            lesson3.setDescription(loremText);

            this.lessonRepository.saveAll(List.of(lesson1, lesson2, lesson3));

        }
    }
}
