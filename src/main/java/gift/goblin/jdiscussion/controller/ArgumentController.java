/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.WebSecurityConfig;
import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/argument")
public class ArgumentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ArgumentRepository argumentRepository;

    @GetMapping(value = {"/new"})
    public String renderMainMenu(Model model) {

        logger.info("User opened add-argument.");

        model.addAttribute("newArgument", new Argument());
        return "add_argument";
    }

    @PostMapping(value = {"/add"})
    public String addQuizcard(@ModelAttribute("newArgument") Argument newArgument, BindingResult bindingResult, Model model) {

        UUID randomUUID = UUID.randomUUID();
        newArgument.setId(randomUUID.toString());
        logger.info("Create new argument in database (UUID was created randomized): {}", newArgument);
        
        argumentRepository.save(newArgument);

        model.addAttribute("display_success", true);

        return renderMainMenu(model);
    }

//    @GetMapping(value = {"/edit"})
//    public String renderEditQuizcards(Model model, Authentication authentication) {
//        if (!isUserAdmin(authentication)) {
//            logger.info("User opened edit-quizcards, without the required rights- redirect to startpage.");
//            return "redirect:/home";
//        } else {
//            List<Quizcard> allQuizcards = quizcardRepository.findAll();
//            logger.debug("Found {} quizcards.", allQuizcards.size());
//
//            model.addAttribute("allQuizcards", allQuizcards);
//            return "edit_quizcards";
//        }
//    }
//
//    @GetMapping(value = "/delete/{id}")
//    public String removeQuizcard(@PathVariable("id") String id, Authentication authentication, Model model) {
//        if (!isUserAdmin(authentication)) {
//            logger.info("User tried to delete quizcards, without the required rights- redirect to startpage.");
//            return "redirect:/home";
//        } else {
//            logger.info("Will remove quizcard with id {}", id);
//            quizcardRepository.deleteById(Integer.parseInt(id));
//            return "redirect:/quizcard/edit";
//        }
//    }

    private boolean isUserAdmin(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.ROLE_ADMIN))) {
            return true;
        } else {
            return false;
        }
    }
}
