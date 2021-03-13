/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.dto;

/**
 *
 * @author andre
 */
public class GameStatus {

    /**
     * Will be true, if users are allowed to create pro/contra argument-cards.
     */
    boolean createArguments;
    
    /**
     * Will be true, if users are allowed to vote the arguments (give their upvote).
     */
    boolean voteArguments;

    public GameStatus() {
    }

    public GameStatus(boolean createArguments, boolean voteArguments) {
        this.createArguments = createArguments;
        this.voteArguments = voteArguments;
    }
    
    public boolean isCreateArguments() {
        return createArguments;
    }

    public void setCreateArguments(boolean createArguments) {
        this.createArguments = createArguments;
    }

    public boolean isVoteArguments() {
        return voteArguments;
    }

    public void setVoteArguments(boolean voteArguments) {
        this.voteArguments = voteArguments;
    }

    @Override
    public String toString() {
        return "GameStatus{" + "createArguments=" + createArguments + ", voteArguments=" + voteArguments + '}';
    }
    
}
