package com.nd2k.auth.services;

import com.nd2k.auth.models.domain.Role;
import com.nd2k.auth.models.domain.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    Role createRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
}
