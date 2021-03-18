/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.bean;

import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.repo.ArgumentRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class ArgumentService {
    
    @Autowired
    ArgumentRepository argumentRepository;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
}
