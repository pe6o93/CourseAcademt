package com.example.academy.model.entity;

import com.example.academy.model.enums.MailStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@Table(name = "contact_mail")
@Entity
public class ContactMailEntity extends BaseEntity {
    @Size(max = 60)
    private String fullName;
    @Size(max = 50)
    @Email
    private String email;
    @Size(max = 15)
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String text;
    @Enumerated(EnumType.STRING)
    private MailStatusEnum status;
}
