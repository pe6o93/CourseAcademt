package com.example.academy.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PictureDTO {

    private String title;
    private MultipartFile picture;
}
