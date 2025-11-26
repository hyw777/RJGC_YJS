package com.sgrej.service;

import com.sgrej.domain.dto.TipDTO;

public interface TipService {

    /**
     * 增加提示
     * @param tipDTO
     */
    void addTip(TipDTO tipDTO);

    /**
     * 删除提示
     * @param tipId
     */
    void delete(int tipId);
}
