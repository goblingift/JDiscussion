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
     * Will be true, if the analyse phase starts and the visualization of the arguments can be accessed.
     */
    boolean analyseArguments;
    
    /**
     * Will be true, if users are allowed to vote the arguments (give their upvote).
     */
    boolean voteArguments;
    
    public GameStatus() {
    }

    public GameStatus(boolean createArguments, boolean analyseArguments, boolean voteArguments) {
        this.createArguments = createArguments;
        this.analyseArguments = analyseArguments;
        this.voteArguments = voteArguments;
    }

    public boolean isCreateArguments() {
        return createArguments;
    }

    public void setCreateArguments(boolean createArguments) {
        this.createArguments = createArguments;
    }

    public boolean isAnalyseArguments() {
        return analyseArguments;
    }

    public void setAnalyseArguments(boolean analyseArguments) {
        this.analyseArguments = analyseArguments;
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
