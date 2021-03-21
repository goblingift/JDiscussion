/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author andre
 */
@Document
public class ArgumentLikes {
    
    @Id
    private Long id;
    
    private Long argumentId;
    
    private String sessionId;

    public ArgumentLikes() {
    }

    public ArgumentLikes(Long id, Long argumentId, String sessionId) {
        this.id = id;
        this.argumentId = argumentId;
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArgumentId() {
        return argumentId;
    }

    public void setArgumentId(Long argumentId) {
        this.argumentId = argumentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ArgumentLikes{" + "id=" + id + ", argumentId=" + argumentId + ", sessionId=" + sessionId + '}';
    }
}
