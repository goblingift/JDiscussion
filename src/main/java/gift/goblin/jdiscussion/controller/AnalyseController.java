/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.dto.GameStatus;
import gift.goblin.jdiscussion.mongodb.model.Argument;
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

    @GetMapping(value = "/analyse")
    public String renderAnalyseView(HttpSession session, Model model, Authentication authentication) {

        logger.info("User tries to open analyse. Analyse-phase enabled:{}", gameStatus.isAnalyseArguments());

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("User has admin-role, display more options for em.");
            model.addAttribute("isAdmin", true);
        } else {
            logger.warn("Non admin-user tried to open analyse- redirect to home!");
            return "redirect:/home";
        }

        Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
        if (optGroupNumber.isPresent()) {
            model.addAttribute("groupNumber", optGroupNumber.get());
            model.addAttribute("groupName", sessionManager.getGroupName(optGroupNumber.get()));
        }
        
        for (int i = 1; i <= 4; i++) {
            List<Argument> args = argumentRepository.findByGroupId(sessionManager.getGroupId(i));
            model.addAttribute("arguments_" + i, args);
            System.out.println("Added arguments:" + i + " : " + args);
        }

        model.addAttribute("createArguments", gameStatus.isCreateArguments());
        model.addAttribute("analysePhase", gameStatus.isAnalyseArguments());
        return "analyse";
    }

}
