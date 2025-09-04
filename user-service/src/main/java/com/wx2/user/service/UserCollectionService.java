package com.wx2.user.service;


public interface UserCollectionService {
    /**
     * 收藏院校
     */
    void addCollectionCollege(Long collegeId);

    /**
     * 取消收藏院校
     */
    void removeCollectionCollege(Long collegeId);
}
