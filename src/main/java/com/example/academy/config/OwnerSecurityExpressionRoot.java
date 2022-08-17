package com.example.academy.config;

import com.example.academy.model.dto.UserDTO;
import com.example.academy.service.UserService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class OwnerSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;
    private HttpServletRequest request;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public  OwnerSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }
    /**
     checks if the authenticated user have access to the user detail with the id
     Only a user have access to his own user details or an admin
     */
    public boolean hasUser(Integer id){
        UserDTO user =  this.userService.findByIdAndMapToDTO(id);
        return authentication.getName().equals(user.getUsername());
    }

    //We need this setter method to set the UserService from another class because this one dosen't have access to Application Context.

    public void setUserService(UserService userService){
        this.userService=userService;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }
}