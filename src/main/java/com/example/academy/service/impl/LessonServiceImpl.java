package com.example.academy.service.impl;

import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.LessonEntity;
import com.example.academy.repository.LessonRepository;
import com.example.academy.service.LessonService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Override
    public LessonDTO findById(Integer id) {
        LessonEntity lessonEntity = this.lessonRepository.findById(id).orElseThrow();
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
}
