/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.controller;

import gift.goblin.jdiscussion.dto.ArgumentWithLikes;
import gift.goblin.jdiscussion.mongodb.model.ArgumentLikes;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentLikesRepository;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/argument")
public class ArgumentRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ArgumentRepository argumentRepository;
    
    @Autowired
    private ArgumentLikesRepository argumentLikesRepository;

    
    @GetMapping(value = "/argument-likes")
    public List<ArgumentWithLikes> getArgumentLikes() {

        List<ArgumentWithLikes> returnValue = new ArrayList<>();

        List<ArgumentLikes> allArgumentLikes = argumentLikesRepository.findAll();
        Map<Long, List<ArgumentLikes>> mappedByArgumentId = allArgumentLikes.stream().collect(Collectors.groupingBy(a -> a.getArgumentId()));

        mappedByArgumentId.forEach((Long argumentId, List<ArgumentLikes> likes) -> {
            returnValue.add(new ArgumentWithLikes(argumentId, likes.size()));
        });

        logger.info("Found {} arguments with likes, return em!", returnValue.size());
        return returnValue;
    }

}
