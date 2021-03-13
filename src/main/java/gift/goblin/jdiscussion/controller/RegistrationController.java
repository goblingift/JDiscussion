/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.WebSecurityConfig;
import static gift.goblin.jdiscussion.WebSecurityConfig.SESSION_FIELD_GAMEPROGRESS;
import gift.goblin.jdiscussion.dto.UserCredentials;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author andre
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private UserDetailsManager userDetailsManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public String renderRegistrationForm(Model model) {
        
        model.addAttribute("userForm", new UserCredentials());
        model.addAttribute("build_artifact", buildProperties.getArtifact());
        model.addAttribute("build_version", buildProperties.getVersion());
        return "registration";
    }

    @PostMapping(path = "/submit")
    public String registration(HttpSession session, @ModelAttribute("userForm") UserCredentials userForm, BindingResult bindingResult, Model model) {

        session.setAttribute(WebSecurityConfig.SESSION_FIELD_USERNAME, userForm.getUsername());
        logger.info("Successful set username to session: {}", userForm.getUsername());
        
        UserDetails userDetails;
        if (userForm.getUsername().equalsIgnoreCase(WebSecurityConfig.ADMIN_USERNAME)) {
            userDetails = userDetailsManager.loadUserByUsername(WebSecurityConfig.ADMIN_USERNAME);
        } else {
            userDetails = userDetailsManager.loadUserByUsername("user");
        }
        
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        return "redirect:/home";
    }

}
