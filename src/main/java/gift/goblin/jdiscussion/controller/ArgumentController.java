/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.WebSecurityConfig;
import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.dto.ArgumentWithLikes;
import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.model.ArgumentLikes;
import gift.goblin.jdiscussion.mongodb.model.UserGroup;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentLikesRepository;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import gift.goblin.jdiscussion.mongodb.repo.UserGroupRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author andre
 */
@Controller
@RequestMapping("/argument")
public class ArgumentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArgumentRepository argumentRepository;
    
    @Autowired
    private ArgumentLikesRepository argumentLikesRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping(value = {"/new"})
    public String renderAddArgumentView(HttpSession session, Model model) {

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
    public String addArgument(@ModelAttribute("newArgument") Argument newArgument, BindingResult bindingResult, Model model, HttpSession session) {

        if (newArgument.getId() == null || newArgument.getId() == 0) {
            newArgument.setId(System.currentTimeMillis());
        }

        if (newArgument.getGroupId() == null || newArgument.getGroupId() == 0) {
            Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
            if (optGroupNumber.isPresent()) {
                Long groupId = sessionManager.getGroupId(optGroupNumber.get());
                newArgument.setGroupId(groupId);
            }
        }

        logger.info("Create new argument in database (UUID was created randomized): {}", newArgument);

        argumentRepository.save(newArgument);

        model.addAttribute("display_success", true);

        return "redirect:/argument/new";
    }

    /**
     * Will try to delete an argument by its id.
     *
     * @param id id of the argument.
     * @param authentication contains user informations.
     * @param model
     * @param session
     * @return true if successful deleted, false if not found, no rights or
     * something went wrong.
     */
    @GetMapping(value = "/delete/{id}")
    public String removeArgument(@PathVariable("id") String id, Authentication authentication, Model model, HttpSession session) {
        logger.info("Called removeArgument for id: {}", id);
        long parsedId = Long.parseLong(id);
        // First, check if this argument exists and identify which group it has created
        Optional<Argument> optArgument = argumentRepository.findById(parsedId);

        if (optArgument.isPresent()) {
            Long groupIdArgument = optArgument.get().getGroupId();
            Long groupId = 0L;

            Optional<Integer> optGroupNumberUser = sessionManager.tryToGetGroupNumber(session);
            if (optGroupNumberUser.isPresent()) {
                Optional<UserGroup> optUserGroup = userGroupRepository.findByNumber(optGroupNumberUser.get());
                if (optUserGroup.isPresent()) {
                    groupId = optUserGroup.get().getId();
                }
            }

            logger.info("groupIdArgument " + groupIdArgument);
            logger.info("optGroupNumberUser " + optGroupNumberUser);

            if (isUserAdmin(authentication)) {
                logger.info("Will remove argument (By admin-user) with id {}", parsedId);
                argumentRepository.deleteById(parsedId);
                //return true;
            } else if (groupIdArgument.equals(groupId)) {
                // If user is part of the group which created argument, delete em
                logger.info("Will delete argument {} by user {}", parsedId, authentication.getName());
                argumentRepository.deleteById(parsedId);
                //return true;
            } else {
                //return false;
            }
        } else {
            logger.warn("Couldnt find any arguments with id: {}", parsedId);
            //return false;
        }

        return "redirect:/argument/new";
    }
    
    @GetMapping(value = "/like/{id}")
    public String likeArgument(@PathVariable("id") String id) {
        
        logger.info("User liked argument: " + id);
        
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<ArgumentLikes> optEntry = argumentLikesRepository.findBySessionIdAndArgumentId(sessionId, Long.parseLong(id));
        if (optEntry.isEmpty()) {
            ArgumentLikes argumentLike = new ArgumentLikes(System.currentTimeMillis(), Long.parseLong(id), sessionId);
            argumentLikesRepository.save(argumentLike);
            logger.info("Successful saved like for argument: " + id);
        } else {
            logger.info("User has already liked argument {}, skip like.", id);
        }
        
        return "redirect:/argument/new";
    }
    
    @GetMapping(value = "/dislike/{id}")
    public String dislikeArgument(@PathVariable("id") String id) {
        
        logger.info("User disliked argument: " + id);
        
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<ArgumentLikes> optEntry = argumentLikesRepository.findBySessionIdAndArgumentId(sessionId, Long.parseLong(id));
        if (!optEntry.isEmpty()) {
            argumentLikesRepository.delete(optEntry.get());
            logger.info("Successful disliked argument: " + id);
        } else {
            logger.info("No entry found for argument-id {} and session-id {}, skip like.", id, sessionId);
        }
        
        return "redirect:/argument/new";
    }

    @GetMapping(value = "/changeWeight/{id}/{newWeight}")
    public String changeWeight(@PathVariable("id") String id, @PathVariable("newWeight") String newWeight, Authentication authentication, Model model, HttpSession session) {
        logger.info("Called changeWeight for id: {}", id);
        long parsedId = Long.parseLong(id);
        // First, check if this argument exists and identify which group it has created
        Optional<Argument> optArgument = argumentRepository.findById(parsedId);

        if (optArgument.isPresent()) {
            Argument argument = optArgument.get();
            argument.setWeight(Byte.parseByte(newWeight));
            argumentRepository.save(argument);
            logger.info("Successful updated argument ({}) with new weight: {}", id, newWeight);
        } else {
            logger.warn("Couldnt find any arguments with id: {}", parsedId);
        }

        return "redirect:/argument/new";
    }
    
    private boolean isUserAdmin(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.ROLE_ADMIN))) {
            return true;
        } else {
            return false;
        }
    }
}
