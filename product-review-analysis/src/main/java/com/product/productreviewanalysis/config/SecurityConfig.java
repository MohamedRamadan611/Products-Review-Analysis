package com.product.productreviewanalysis.config;

import com.product.productreviewanalysis.config.Admin.AdminAndUserInfoDetailsService;
import com.product.productreviewanalysis.security.JwtAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final List<String> AUTHORIZED_URLS = List.of(
            "/product/welcome",
            "/admin/welcome",
            "/admin/register",
            "/admin/authenticate",
            "/admin/verifyRegistration",
            "/admin/resetPassword",
            "/admin/resendVerification",
            "/admin/savePassword",
            "/user/welcome",
            "/user/register",
            "/user/authenticate",
            "/user/verifyRegistration",
            "/user/resetPassword",
            "/user/resendVerification",
            "/user/savePassword"
    );


    @Autowired
    private JwtAuth jwtAuth;

    @Bean
    public PasswordEncoder passwordEncoder ()
    {
        return new BCryptPasswordEncoder(7);
    }
    @Bean
    public UserDetailsService AdminDetailsService ()
    {
        return new AdminAndUserInfoDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
    {
        return http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(AUTHORIZED_URLS.stream().toArray(String[]::new))
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/product/**" , "/admin/**" , "/user/**")
                .authenticated()
                .and()/*
                .formLogin()
                .and()
                .httpBasic()*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(adminAuthenticationProvider())
                .addFilterBefore(jwtAuth , UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Qualifier("AuthenticationProvider")
    public AuthenticationProvider adminAuthenticationProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(AdminDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Qualifier("authenticationManager")
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
