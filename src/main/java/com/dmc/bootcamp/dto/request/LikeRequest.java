package com.dmc.bootcamp.dto.request;

public class LikeRequest {
    private Long recommendId;
    private boolean likeStatus;

    // Getter and Setter
    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public boolean isLike() {
        return likeStatus;
    }

    public void setLike(boolean like) {
        this.likeStatus = likeStatus;
    }
}