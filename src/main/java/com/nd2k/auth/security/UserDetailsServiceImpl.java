package com.nd2k.auth.security;

import com.google.common.collect.Lists;
import com.nd2k.auth.models.domain.Role;
import com.nd2k.auth.models.domain.User;
import com.nd2k.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            List<GrantedAuthority> authorityList = Lists.newArrayList();
            Collection<Role> roles = existingUser.get().getRoles();
            for (Role role: roles) {
                authorityList.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
            }
            return new org.springframework.security.core.userdetails.User(existingUser.get().getEmail(), existingUser.get().getPassword(), authorityList);
        }
        return null;
    }
}
