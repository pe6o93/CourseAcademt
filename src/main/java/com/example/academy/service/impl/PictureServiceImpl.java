package com.example.academy.service.impl;

import com.example.academy.model.entity.PictureEntity;
import com.example.academy.repository.PictureRepository;
import com.example.academy.service.PictureService;
import com.example.academy.service.cloudinary.CloudinaryImage;
import com.example.academy.service.cloudinary.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;


    @Override
    public PictureEntity savePictureFromMultipartFile(MultipartFile img) throws IOException {
        final CloudinaryImage uploaded = cloudinaryService.upload(img);
        PictureEntity picture=new PictureEntity();
        picture.setUrl(uploaded.getUrl());
        picture.setPublicId(uploaded.getPublicId());
        return this.pictureRepository.save(picture);
    }

    @Override
    public void initPictures() {
        String URL = "https://res.cloudinary.com/dcw3srdlg/image/upload/v1660846730/auwjil2itvsrrjkpg3vo.jpg";
        PictureEntity pictureEntity1=new PictureEntity();
        PictureEntity pictureEntity2=new PictureEntity();
        PictureEntity pictureEntity3=new PictureEntity();
        pictureEntity1.setUrl(URL);
        pictureEntity2.setUrl(URL);
        pictureEntity3.setUrl(URL);
        this.pictureRepository.saveAll(List.of(pictureEntity1,pictureEntity2,pictureEntity3));
    }
}
