/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.dto;

/**
 * Contains the argument-id and the count of likes.
 * @author andre
 */
public class ArgumentWithLikes {
    private Long argumentId;
    private int likeCount;

    public ArgumentWithLikes() {
    }

    public ArgumentWithLikes(Long argumentId, int likeCount) {
        this.argumentId = argumentId;
        this.likeCount = likeCount;
    }

    public Long getArgumentId() {
        return argumentId;
    }

    public void setArgumentId(Long argumentId) {
        this.argumentId = argumentId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "ArgumentWithLikes{" + "argumentId=" + argumentId + ", likeCount=" + likeCount + '}';
    }
    
}
