/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.mongodb.repo;

import gift.goblin.jdiscussion.mongodb.model.ArgumentLikes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author andre
 */
public interface ArgumentLikesRepository extends MongoRepository<ArgumentLikes, Long> {
    public List<ArgumentLikes> findByArgumentId(Long argumentId);
    public Optional<ArgumentLikes> findBySessionId(String sessionId);
    public Optional<ArgumentLikes> findBySessionIdAndArgumentId(String sessionId, Long argumentId);
}
