/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.dto.GameStatus;
import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.model.ArgumentLikes;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentLikesRepository;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author andre
 */
@Controller
public class AnalyseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GameStatus gameStatus;
    
    @Autowired
    private SessionManager sessionManager;
    
    @Autowired
    private ArgumentRepository argumentRepository;
    
    @Autowired
    private ArgumentLikesRepository argumentLikesRepository;

    @GetMapping(value = "/analyse")
    public String renderAnalyseView(HttpSession session, Model model, Authentication authentication) {

        logger.info("User tries to open analyse. Analyse-phase enabled:{}", gameStatus.isAnalyseArguments());

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("User has admin-role, display more options for em.");
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isAdmin", false);
        }

        Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
        if (optGroupNumber.isPresent()) {
            model.addAttribute("groupNumber", optGroupNumber.get());
            model.addAttribute("groupName", sessionManager.getGroupName(optGroupNumber.get()));
        }
        
        for (int i = 1; i <= 4; i++) {
            List<Argument> args = argumentRepository.findByGroupId(sessionManager.getGroupId(i));
            enrichArgumentsWithLikes(args);
            model.addAttribute("arguments_" + i, args);
            logger.info("Added arguments to view:" + i + " : " + args);
        }
        List<Argument> adminArguments = argumentRepository.findByGroupId(SessionManager.GROUP_ID_ADMIN);
        if (!adminArguments.isEmpty()) {
            enrichArgumentsWithLikes(adminArguments);
            model.addAttribute("arguments_admin", adminArguments);
            logger.info("Added admin-arguments to view: " + adminArguments);
        }
        
        String likedArgument = "";
        Optional<ArgumentLikes> optArgumentLikes = argumentLikesRepository.findBySessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
        if (optArgumentLikes.isPresent()) {
            likedArgument = optArgumentLikes.get().getArgumentId().toString();
        }
        
        model.addAttribute("likedArgument", likedArgument);
        model.addAttribute("sessionId", RequestContextHolder.currentRequestAttributes().getSessionId());
        model.addAttribute("newArgument", new Argument());
        model.addAttribute("createArguments", gameStatus.isCreateArguments());
        model.addAttribute("analysePhase", gameStatus.isAnalyseArguments());
        return "analyse";
    }
    
    private void enrichArgumentsWithLikes(List<Argument> arguments) {
        
        for (Argument actArgument : arguments) {
            List<ArgumentLikes> argumentLikes = argumentLikesRepository.findByArgumentId(actArgument.getId());
            if (!argumentLikes.isEmpty()) {
                actArgument.setLikes(argumentLikes.size());
            }
        }
    }

}
