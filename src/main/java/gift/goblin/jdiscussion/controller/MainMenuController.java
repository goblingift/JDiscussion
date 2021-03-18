/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import static gift.goblin.jdiscussion.WebSecurityConfig.SESSION_FIELD_GROUPNUMBER;
import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.dto.GameStatus;
import java.text.ParseException;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author andre
 */
@Controller
public class MainMenuController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private GameStatus gameStatus;

    @Autowired
    private SessionManager sessionManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = {"/home", "/"})
    public String renderMainMenu(HttpSession session, Model model, Authentication authentication) {

        logger.info("User opened main-menu. Game status is: {}", gameStatus.isCreateArguments());

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("User has admin-role, display more options for em.");
            model.addAttribute("isAdmin", true);
        }

        Optional<Integer> optGroupNumber = sessionManager.tryToGetGroupNumber(session);
        if (optGroupNumber.isPresent()) {
            model.addAttribute("groupNumber", optGroupNumber.get());
            model.addAttribute("groupName", sessionManager.getGroupName(optGroupNumber.get()));
        }

        model.addAttribute("createArguments", gameStatus.isCreateArguments());
        model.addAttribute("analysePhase", gameStatus.isAnalyseArguments());
        model.addAttribute("build_artifact", buildProperties.getArtifact());
        model.addAttribute("build_version", buildProperties.getVersion());
        return "main_menu";
    }

    @GetMapping(value = "/start-game")
    public String startGame(Model model, Authentication authentication) {

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("Game starts now!");
            gameStatus.setCreateArguments(true);
        } else {
            logger.warn("User is no admin- wont start game now.");
        }

        return "redirect:/home";
    }

    @GetMapping(value = "/stop-game")
    public String stopGame(Model model, Authentication authentication) {

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("Game stops now!");
            gameStatus.setCreateArguments(false);
        } else {
            logger.warn("User is no admin- wont stop game now.");
        }

        return "redirect:/home";
    }

    @GetMapping(value = "/start-analyse")
    public String startAnalysePhase(Model model, Authentication authentication) {

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("Analyse phase enabled!");
            gameStatus.setAnalyseArguments(true);
        } else {
            logger.warn("User is no admin- wont start analysis phase now.");
        }

        return "redirect:/home";
    }

    @GetMapping(value = "/stop-analyse")
    public String stopAnalysePhase(Model model, Authentication authentication) {

        if (sessionManager.isUserAdmin(authentication)) {
            logger.info("Analyse phase disabled!");
            gameStatus.setAnalyseArguments(false);
        } else {
            logger.warn("User is no admin- wont stop analysis phase now.");
        }

        return "redirect:/home";
    }

}
