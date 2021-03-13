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
public class Argument {
    
    @Id
    private String id;
    
    /**
     * true if pro-argument, false if contra-argument.
     */
    private Boolean proArgument;
    
    /**
     * How important the user see this argument.
     */
    private Byte weight;
    
    /**
     * Which category this card is used for.
     * 1= economy,
     * 2= ecology,
     * 3= social.
     */
    private Byte category;
    
    private String argument;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getProArgument() {
        return proArgument;
    }

    public void setProArgument(Boolean proArgument) {
        this.proArgument = proArgument;
    }

    public Byte getWeight() {
        return weight;
    }

    public void setWeight(Byte weight) {
        this.weight = weight;
    }

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    

    @Override
    public String toString() {
        return "Argument{" + "id=" + id + ", proArgument=" + proArgument + ", weight=" + weight + ", category=" + category + ", argument=" + argument + '}';
    }
    
}