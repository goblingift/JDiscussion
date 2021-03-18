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
public class UserGroup {

    public UserGroup() {
    }

    public UserGroup(Long id, Integer number, String description) {
        this.id = id;
        this.number = number;
        this.description = description;
    }

    @Id
    private Long id;
    
    /**
     * The group-number is an internal counter for the application.
     */
    private Integer number;
    
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "UserGroup{" + "id=" + id + ", number=" + number + ", description=" + description + '}';
    }
    
}
