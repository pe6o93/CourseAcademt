package com.example.academy.service;

import com.example.academy.model.dto.LessonDTO;

public interface LessonService {
    LessonDTO findById(Integer id);

    void deleteById(Integer id);

    void updateLesson(LessonDTO lessonDTO);
}
