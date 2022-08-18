package com.example.academy.service;

import com.example.academy.model.entity.PictureEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {
    PictureEntity savePictureFromMultipartFile(MultipartFile picture) throws IOException;

    void initPictures();
}
