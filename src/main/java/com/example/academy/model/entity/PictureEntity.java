package com.example.academy.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "pictures")
public class PictureEntity extends BaseEntity{

    private String title;
    private String url;
    private String publicId;
}
