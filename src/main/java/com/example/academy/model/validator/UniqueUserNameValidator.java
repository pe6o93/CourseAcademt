package com.example.academy.model.validator;

import com.example.academy.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    private final UserService userService;

    public UniqueUserNameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(username==null) {
            return true;
        }
        return this.userService.isUserNameFree(username);
    }
}
