/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.mongodb.repo;

import gift.goblin.jdiscussion.mongodb.model.Argument;
import gift.goblin.jdiscussion.mongodb.model.UserGroup;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author andre
 */
public interface UserGroupRepository extends MongoRepository<UserGroup, Long> {
    Optional<UserGroup> findByNumber(int number);
}
