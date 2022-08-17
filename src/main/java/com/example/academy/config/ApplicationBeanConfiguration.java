package com.example.academy.config;

import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper(){
//        ModelMapper mapper=new ModelMapper();
//
//        TypeMap<UserEntity, UserDTO> propertyMapper=mapper.createTypeMap(UserEntity.class,UserDTO.class);
//
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new Pbkdf2PasswordEncoder();
    }

}
