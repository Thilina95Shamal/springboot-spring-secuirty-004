package com.example.proj.service.user;

import com.example.proj.dto.role.RoleDTO;
import com.example.proj.dto.user.UserDTO;
import com.example.proj.dto.user_role.UserRoleDTO;
import com.example.proj.model.roles.Role;
import com.example.proj.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(UserDTO userDTO);

    Role saveRole(RoleDTO roleDTO);

    String addRoleToUser(UserRoleDTO userRoleDTO);

   Optional<User> getUser(String username);

    List<User> getAllUsers();

    void saveUserList(List<UserDTO> userDTO);

    void saveRoleList(List<RoleDTO> roleDTOS);
}
