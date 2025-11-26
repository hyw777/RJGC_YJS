package com.sgrej.service;

import com.sgrej.domain.vo.CollectionVO;

import java.util.List;

public interface CollectService {
    /**
     * 收藏商铺
     * @param businessId
     */
    void save(int businessId);

    /**
     * 我的收藏
     * @return
     */
    List<CollectionVO> getCollect();

    /**
     * 取消收藏
     * @param businessId
     */
    void cancel(int businessId);
}
