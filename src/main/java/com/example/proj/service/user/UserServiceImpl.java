package com.example.proj.service.user;

import com.example.proj.dto.role.RoleDTO;
import com.example.proj.dto.user.UserDTO;
import com.example.proj.dto.user_role.UserRoleDTO;
import com.example.proj.model.roles.Role;
import com.example.proj.model.user.User;
import com.example.proj.repository.role.RoleRepository;
import com.example.proj.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return roleRepository.save(role);
    }

    @Override
    public String addRoleToUser(UserRoleDTO userRoleDTO) {
        Optional<User> user = userRepository.findUserByUsername(userRoleDTO.getUsername());
        if(user.isPresent()){
            Optional<Role> role = roleRepository.findRoleByName(userRoleDTO.getRoleName());
            if(role.isPresent()){
                if(user.get().getRoles()!=null){
                    user.get().getRoles().add(role.get());
                    userRepository.save(user.get());
                }else{
                    List<Role> list = new ArrayList<>();
                    list.add(role.get());
                    user.get().setRoles(list);
                    userRepository.save(user.get());
                }
                return "";
            }else{
                return "No Role Found";
            }
        }else{
            return "No User Found";
        }
    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUserList(List<UserDTO> userDTO) {
        List<User> list = userDTO.stream().map(userDTO1 -> {
            User user = new User();
            user.setName(userDTO1.getName());
            user.setPassword(userDTO1.getPassword());
            user.setUsername(userDTO1.getUsername());
            return user;
        }).toList();
        userRepository.saveAll(list);
    }

    @Override
    public void saveRoleList(List<RoleDTO> roleDTOS) {
        List<Role> list = roleDTOS.stream().map(roleDTOS1 -> {
            Role role = new Role();
            role.setName(roleDTOS1.getName());
            return role;
        }).toList();
        roleRepository.saveAll(list);
    }
}
