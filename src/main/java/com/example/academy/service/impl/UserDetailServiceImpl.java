package com.example.academy.service.impl;

import com.example.academy.model.entity.UserEntity;
import com.example.academy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = this.userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found."));
        return mapUserToUserDetails(userEntity);
    }

    private static UserDetails mapUserToUserDetails(UserEntity userEntity){

       List<GrantedAuthority> authorities= userEntity.getRoles()
               .stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name())).collect(Collectors.toList());
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                authorities);
    }
}
