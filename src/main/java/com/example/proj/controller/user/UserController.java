package com.example.proj.controller.user;

import com.example.proj.dto.role.RoleDTO;
import com.example.proj.dto.user.UserDTO;
import com.example.proj.dto.user_role.UserRoleDTO;
import com.example.proj.model.roles.Role;
import com.example.proj.model.user.User;
import com.example.proj.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO){
        User user = userService.saveUser(userDTO);
        if(user!=null){
            return new ResponseEntity<>("Successfully User saved", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Error Saving User", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/role/saveRole")
    public ResponseEntity<?> saveRole(@RequestBody RoleDTO roleDTO){
        Role role = userService.saveRole(roleDTO);
        if(role!=null){
            return new ResponseEntity<>("Successfully Role saved", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Error Saving Role", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/role/saveRoleForUser")
    public ResponseEntity<?> saveRoleForUser(@RequestBody UserRoleDTO userRoleDTO){
        String result = userService.addRoleToUser(userRoleDTO);
        if(result.isBlank()){
            return new ResponseEntity<>("Successfully Roles for User saved", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/getUser")
    public ResponseEntity<?> getUser(@RequestParam String username){
        Optional<User> user = userService.getUser(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);

        }else{
            return new ResponseEntity<>("", HttpStatus.OK);
        }
    }

    @GetMapping("/user/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


    // Just for adding Multiple data
    @PostMapping("/user/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<UserDTO> userDTO){
        userService.saveUserList(userDTO);
        return new ResponseEntity<>("Successfully Users saved", HttpStatus.CREATED);
    }

    @PostMapping("/role/saveAll")
    public ResponseEntity<?> saveAllRole(@RequestBody List<RoleDTO> roleDTOS){
        userService.saveRoleList(roleDTOS);
        return new ResponseEntity<>("Successfully Roles saved", HttpStatus.CREATED);
    }


    /** SAMPLE DATA
     *[ {"name": "John Doe", "username": "johndoe", "password": "password1"}, {"name": "Jane Smith", "username": "janesmith", "password": "password2"}, {"name": "Alice Johnson", "username": "alicejohnson", "password": "password3"}, {"name": "Bob Brown", "username": "bobbrown", "password": "password4"}]
     *
     *[ {"name": "ROLE_USER"}, {"name": "ROLE_MANAGER"}, {"name": "ROLE_ADMIN"}, {"name": "ROLE_SUPER_ADMIN"} ]
     *
     *[ {"username": "johndoe", "roleName": "ROLE_USER"}, {"username": "janesmith", "roleName": "ROLE_MANAGER"}, {"username": "alicejohnson", "roleName": "ROLE_ADMIN"}, {"username": "bobbrown", "roleName": "ROLE_SUPER_ADMIN"}, {"username": "carolwhite", "roleName": "ROLE_USER"}, {"username": "davidblack", "roleName": "ROLE_MANAGER"}, {"username": "emmagreen", "roleName": "ROLE_ADMIN"}, {"username": "frankblue", "roleName": "ROLE_SUPER_ADMIN"}, {"username": "gracepurple", "roleName": "ROLE_USER"}, {"username": "hankred", "roleName": "ROLE_MANAGER"} ]
     * */


}
