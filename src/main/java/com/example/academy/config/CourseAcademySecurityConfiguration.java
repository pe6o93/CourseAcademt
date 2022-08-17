package com.example.academy.config;

import com.example.academy.config.SecurityExpressionHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class CourseAcademySecurityConfiguration extends GlobalMethodSecurityConfiguration {

    /**
     * RoleHierarchyImpl & RoleVoter are 2 beans implemented in another config class to have a role Hierarchy as needed in the app
     in this case uncomment block N°1, N°2and N°3
     */

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        var expressionHandler = new SecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        //Block N°2  expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }
    /**
     * AccessDecisionManager makes a final access control (authorization) decision.
     * Overriding this method is necessary for role hierarchy, customized security methods..
     */
    @Override
    protected AccessDecisionManager accessDecisionManager() {

        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();

        var expresionAdvice= new ExpressionBasedPreInvocationAdvice();
        expresionAdvice.setExpressionHandler(getExpressionHandler());

        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expresionAdvice));
        decisionVoters.add(new AuthenticatedVoter()); //It is necessary to add this one when we override the default AccessDecisionManager
        /*Block N°3  Add the customized RoleVoter Bean if you have one
          decisionVoters.add(roleVoter);
          */
        return new AffirmativeBased(decisionVoters);
    }
}
