package com.example.academy.config;

import com.cloudinary.Cloudinary;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class ApplicationBeanConfiguration {

    private final CloudinaryConfig config;

    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}

    @Bean
    public PasswordEncoder passwordEncoder() {return new Pbkdf2PasswordEncoder();}

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                Map.of(
                        "cloud_name", config.getCloudName(),
                        "api_key", config.getApiKey(),
                        "api_secret", config.getApiSecret()
                )
        );
    }
}
