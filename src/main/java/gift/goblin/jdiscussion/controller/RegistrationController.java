/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.ApplicationInitializer;
import gift.goblin.jdiscussion.WebSecurityConfig;
import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.dto.GameStatus;
import gift.goblin.jdiscussion.dto.UserCredentials;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author andre
 */
@Controller
@RequestMapping({"/register", "/login"})
public class RegistrationController {

    @Autowired
    GameStatus gameStatus;
    
    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private UserDetailsManager userDetailsManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public String renderRegistrationForm(HttpSession session, Model model, @RequestParam(name = "grp") Optional<String> groupId) {

        boolean loggedInAsUser = false;
        if (groupId.isPresent()) {
            logger.info("Found grp-id in request: {}", groupId);
            loggedInAsUser = tryToLoginAsGroupMember(session, groupId.get());
        } else if (gameStatus.isAnalyseArguments()) {
            loggedInAsUser = tryToLoginAsDefaultUser();
        }

        model.addAttribute("userForm", new UserCredentials());
        model.addAttribute("build_artifact", buildProperties.getArtifact());
        model.addAttribute("build_version", buildProperties.getVersion());

        if (loggedInAsUser) {
            if (gameStatus.isAnalyseArguments()) {
                return "redirect:/analyse";
            } else {
                return "redirect:/home";
            }
        } else {
            return "registration";
        }
    }

    private boolean tryToLoginAsGroupMember(HttpSession session, String groupId) {
        boolean success = false;

        if (groupId != null && !groupId.isEmpty()) {
            try {
                Long parseLong = Long.parseLong(groupId);
                if (SessionManager.GROUP_IDS.contains(parseLong)) {
                    if (SessionManager.GROUP_ID_1 == parseLong) {
                        session.setAttribute(WebSecurityConfig.SESSION_FIELD_GROUPNUMBER, 1);
                    } else if (SessionManager.GROUP_ID_2 == parseLong) {
                        session.setAttribute(WebSecurityConfig.SESSION_FIELD_GROUPNUMBER, 2);
                    } else if (SessionManager.GROUP_ID_3 == parseLong) {
                        session.setAttribute(WebSecurityConfig.SESSION_FIELD_GROUPNUMBER, 3);
                    } else if (SessionManager.GROUP_ID_4 == parseLong) {
                        session.setAttribute(WebSecurityConfig.SESSION_FIELD_GROUPNUMBER, 4);
                    }
                    UserDetails userDetails = userDetailsManager.loadUserByUsername("user");
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    success = true;
                } else {
                    logger.warn("The parameter grp ({}) is not included in the defined list of allowed ids: {}", groupId, SessionManager.GROUP_IDS);
                }

            } catch (NumberFormatException e) {
                logger.warn("The parameter grp doesnt contain a Long-value: {}", groupId);
            }
        }

        return success;
    }

    private boolean tryToLoginAsDefaultUser() {
        UserDetails userDetails = userDetailsManager.loadUserByUsername("user");
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        boolean success = true;
        return success;
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
