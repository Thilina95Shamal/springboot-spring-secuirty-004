package com.example.proj.config.security;

import com.example.proj.config.filter.JWTFilter;
import com.example.proj.service.userdetail.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    private final JWTFilter jwtFilter;

    public SecurityConfig(CustomUserDetailService customUserDetailService, JWTFilter jwtFilter) {
        this.customUserDetailService = customUserDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /** NOTE
         * Specific Matchers First (/user/saveUser)
         * General Matchers Last  (/user/**)
         **/

        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requestMatcherRegistry
                        -> requestMatcherRegistry
                        .requestMatchers("/login","/refresh_token").permitAll()
                        .requestMatchers("/user/saveUser").hasAnyAuthority("ROLE_SUPER_ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER","ROLE_SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}









/** OLDER VERSION **/



//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final CustomUserDetailService customUserDetailService;
//    private final PasswordEncoder passwordEncoder;
//
//    public SecurityConfig(CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder) {
//        this.customUserDetailService = customUserDetailService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override

//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .authorizeRequests().anyRequest().permitAll()
//                .addFilter(new UsernamePasswordAuthenticationFilter(getAuthenticationManagerBean()))
//                .build()
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
//    }

//    @Bean
//    @Override
//    public AuthenticationManager getAuthenticationManagerBean() throws Exception {
//        return super.getAuthenticationManagerBean();
//    }
//}


//@Configuration
//class PasswordEncoders {
//    // Todo: Here need to set the bean inside password save time (regi or save User)
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(12);
//    }
//}

