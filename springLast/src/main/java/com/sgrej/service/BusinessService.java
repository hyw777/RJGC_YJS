package com.sgrej.service;

import com.sgrej.domain.dto.BusinessPageQueryDTO;
import com.sgrej.domain.dto.WorkBenchDTO;
import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.BusinessVO2;
import com.sgrej.domain.vo.WorkBenchVO;

import java.util.List;

public interface BusinessService {
    /**
     * 搜索商铺
     * @param businessPageQueryDTO
     * @return
     */
    PageResult pageQuery(BusinessPageQueryDTO businessPageQueryDTO);

    /**
     * 获取前5个星级为5的商家
     * @return
     */
    List<BusinessVO1> getTop5BusinessWith5Stars();

    /**
     * 商铺详情
     * @param bId
     * @return
     */
    BusinessVO2 getBusinessDetail(int bId);


    /**
     * 根据户主id查询对应的商铺信息
     */
    BusinessVO2 getBusinessDetailByUid();

    /**
     * 更新基本信息
     * @param business
     */
    void changeInfo(Business business);

    /**
     * 获取附近商家信息
     *
     * @return
     */
    List<BusinessVO1> nearBy();

    /**
     * 工作台
     * @param workBenchDTO
     * @return
     */
    WorkBenchVO workBench(WorkBenchDTO workBenchDTO);

    /**
     * 添加图片
     * @param path
     * @param businessId
     */
    void addImage(String path, String businessId);

    /**
     * 删除图片
     * @param fileName
     * @param businessId
     */
    void deleteImage(String fileName, String businessId);

    /**
     * 注册商家
     * @param business
     */
    void register(Business business);
}
