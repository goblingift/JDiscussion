/* 
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

/**
 * Defines which paths are public, and which are private (login required).
 *
 * @author andre
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserDetailsService userDetailsService;

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String USER_USERNAME = "user";
    public static final String ADMIN_USERNAME = "superadmin_master";
    public static final String SESSION_FIELD_GAMEPROGRESS = "gameprogress";
    public static final String SESSION_FIELD_USERNAME = "username";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeRequests()
                .antMatchers("/resources/**", "/css/**", "/js/**", "/webfonts/**", "/images/**", "/static/**", "/register/**").permitAll()
                .antMatchers("/", "/**").access("isAuthenticated()")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/register")
                .successHandler(this::loginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/register");

        // security config for using h2-console
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    private void loginSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        logger.info("Login successful! User logged in as {}.", authentication.getName());

        redirectStrategy.sendRedirect(request, response, "/home");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(USER_USERNAME).password(passwordEncoder().encode("geheim123!")).roles(ROLE_USER)
                .and()
                .withUser(ADMIN_USERNAME).password(passwordEncoder().encode("supersecret123!")).roles(ROLE_ADMIN, ROLE_USER);
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(User.withUsername(USER_USERNAME).password(passwordEncoder().encode("geheim123!"))
                .roles(ROLE_USER).build());
        userDetailsList.add(User.withUsername(ADMIN_USERNAME).password(passwordEncoder().encode("supersecret123!"))
                .roles(ROLE_ADMIN, ROLE_USER).build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }
}
