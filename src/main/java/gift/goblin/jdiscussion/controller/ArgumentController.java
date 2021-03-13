/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.WebSecurityConfig;
import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author andre
 */
@Controller
@RequestMapping("/argument")
public class ArgumentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ArgumentRepository argumentRepository;
    
    @Autowired
    SessionManager sessionManager;
    
    

    @GetMapping(value = {"/new"})
    public String renderMainMenu(HttpSession session, Model model) {

        logger.info("User opened argument-creation overview.");
        
        Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
        if (optGroupNumber.isPresent()) {
            Long groupId = sessionManager.getGroupId(optGroupNumber.get());
            
            List<Argument> arguments = argumentRepository.findByGroupId(groupId);
            List<Argument> economicArguments = arguments.stream().filter(a -> a.getCategory() == 1).collect(Collectors.toList());
            List<Argument> ecologicArguments = arguments.stream().filter(a -> a.getCategory() == 2).collect(Collectors.toList());
            List<Argument> socialArguments = arguments.stream().filter(a -> a.getCategory() == 3).collect(Collectors.toList());
            
            model.addAttribute("economicArguments", economicArguments);
            model.addAttribute("ecologicArguments", ecologicArguments);
            model.addAttribute("socialArguments", socialArguments);
            
            model.addAttribute("groupNumber", optGroupNumber.get());
            model.addAttribute("groupName", sessionManager.getGroupName(optGroupNumber.get()));
        }

        model.addAttribute("newArgument", new Argument());
        return "add_argument";
    }
    

    @PostMapping(value = {"/add"})
    public String addQuizcard(@ModelAttribute("newArgument") Argument newArgument, BindingResult bindingResult, Model model, HttpSession session) {

        UUID randomUUID = UUID.randomUUID();
        newArgument.setId(randomUUID.toString());
        
        Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
        if (optGroupNumber.isPresent()) {
            Long groupId = sessionManager.getGroupId(optGroupNumber.get());
            newArgument.setGroupId(groupId);
        }
        
        logger.info("Create new argument in database (UUID was created randomized): {}", newArgument);
        
        argumentRepository.save(newArgument);

        model.addAttribute("display_success", true);

        return "redirect:/argument/new";
    }

    @GetMapping(value = "/delete/{id}")
    public String removeArgument(@PathVariable("id") String id, Authentication authentication, Model model) {
        if (!isUserAdmin(authentication)) {
            logger.info("User tried to delete quizcards, without the required rights- redirect to startpage.");
            return "redirect:/home";
        } else {
            logger.info("Will remove argument with id {}", id);
            argumentRepository.deleteById(id);
            return "redirect:/argument/new";
        }
    }

    private boolean isUserAdmin(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.ROLE_ADMIN))) {
            return true;
        } else {
            return false;
        }
    }
}
