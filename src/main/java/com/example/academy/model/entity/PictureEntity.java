package com.example.academy.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter @Setter
@Table(name = "pictures")
public class PictureEntity extends BaseEntity{

    private String title;
    private String url;
    private String publicId;
}
