package com.sgrej.service;

import com.sgrej.domain.vo.BusinessApplyVO;

import java.util.List;
import java.util.Map;

public interface BusinessManageService {

    /**
     * 商户注册通知
     * @return
     */
    List<BusinessApplyVO> getApplys();

    /**
     * 同意or拒绝申请？
     * @param businessId
     * @param result
     */
    void isAgree(int businessId, int result);

    Map<String, Object> listBusinesses(Integer page, Integer pageSize);

    void changeBusinessStatus(Integer businessId, Integer isOpen);
}
