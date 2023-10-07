package com.example.academy.config;

import com.example.academy.service.UserService;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;


public class SecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private  ApplicationContext applicationContext;
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                              MethodInvocation invocation) {

        OwnerSecurityExpressionRoot root = new OwnerSecurityExpressionRoot(authentication);
        root.setTrustResolver(this.trustResolver);
        root.setPermissionEvaluator(getPermissionEvaluator());
        // root.setRoleHierarchy(getRoleHierarchy()); uncomment this one if you defined a RoleHierarchy bean

        //set the UserService in the CustomMethodSecurityExpressionRoot instance to be able to use it
        root.setUserService(this.applicationContext.getBean(UserService.class));
        return root;
    }

    //This setter method will be called from the config class
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext=applicationContext;
    }
}
