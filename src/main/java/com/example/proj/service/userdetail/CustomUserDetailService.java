package com.example.proj.service.userdetail;

import com.example.proj.model.user.User;
import com.example.proj.repository.user.UserRepository;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            /** FROM PREVIOUS PROJECTS :
             * instead of using return new UserPrinciple(user.get()); only use AccountStatusUserDetailsChecker */
            //return new UserPrinciple(user.get());
            /** This AccountStatusUserDetailsChecker Provides extra layer of security like : account is locked, expired, or disabled  */
            UserDetails userDetails = new UserPrinciple(user.get());
            new AccountStatusUserDetailsChecker().check(userDetails);
            return userDetails;
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }
    }







    /** OLDER VERSION **/

//    private final UserRepository userRepository;
//
//    public CustomUserDetailService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findUserByUsername(username);
//        if (user.isPresent()) {
//            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
//
//            return new org.springframework.security.core.userdetails.User(user.get().getUsername(),user.get().getPassword(),authorities);
//        } else {
//            throw new UsernameNotFoundException("User Not Found");
//        }
//    }
}
