/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.mongodb.repo;

import gift.goblin.jdiscussion.mongodb.model.Argument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author andre
 */
public interface ArgumentRepository extends MongoRepository<Argument, String>{
    
}
