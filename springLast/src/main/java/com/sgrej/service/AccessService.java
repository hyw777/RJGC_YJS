package com.sgrej.service;

import com.sgrej.domain.dto.AccessDTO;

public interface AccessService {

    /**
     * 用户查看商户详情时增加对应的访问记录
     * @param accessDTO
     */
    void addRecords(AccessDTO accessDTO);
}
