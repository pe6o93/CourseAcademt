package com.example.academy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    public static String ENTITY_NAME = "emailDetails";
    private String recipient;
    private String msgBody;
    private String name;
    private String phone;
    private String attachment;
}
