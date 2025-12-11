package com.sgrej.controller.user;

import com.sgrej.domain.dto.AccessDTO;
import com.sgrej.domain.dto.BusinessPageQueryDTO;
import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.BusinessVO2;
import com.sgrej.service.AccessService;
import com.sgrej.service.BusinessService;
import com.sgrej.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController("businessController1")
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;


    @Autowired
    private AccessService accessService;
    
    /**
     * 根据商家名称进行模糊搜索
     * @param name 商家名称关键词
     * @return 匹配的商家列表
     */
    @GetMapping("/searchByName")
    public ResponseResult searchByName(@RequestParam(required = false) String name) {
        log.info("根据名称搜索商铺信息:{}", name);
        // 创建查询条件对象，默认第一页，每页10条记录
        BusinessPageQueryDTO queryDTO = new BusinessPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        queryDTO.setBusinessName(name);
        
        // 执行分页查询
        PageResult pageResult = businessService.pageQuery(queryDTO);
        return ResponseResult.success(pageResult);
    }

    /**
     * 搜索商铺，考虑到模糊查询可能会出现很多记录，使用分页查询
     * @param
     * @return
     */
    @PostMapping("/search")
    public ResponseResult searchBusiness(@RequestBody BusinessPageQueryDTO businessPageQueryDTO){
        log.info("搜索商铺信息:{}",businessPageQueryDTO);
        //首页能看到的商铺信息,
        PageResult pageResult=businessService.pageQuery(businessPageQueryDTO);
        return ResponseResult.success(pageResult);
    }

    /**
     * 分类查找商品
     * @param businessPageQueryDTO
     * @return
     */
    @PostMapping("/category")
    public  ResponseResult list(@RequestBody BusinessPageQueryDTO businessPageQueryDTO){
       log.info("分类查找商品：{}",businessPageQueryDTO);
        //首页能看到的商铺信息,
        PageResult pageResult=businessService.pageQuery(businessPageQueryDTO);
        return ResponseResult.success(pageResult);
    }
    /**
     * 获取前5个星级为5的商家
     * @return
     */
    @GetMapping("/top5")
    public ResponseResult getTop5BusinessWith5Stars(){
        log.info("获取前5个星级为5的商家");
        List<BusinessVO1> businessList = businessService.getTop5BusinessWith5Stars();
        return ResponseResult.success(businessList);
    }

    /**
     * 商铺详情信息
     * @param bId
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResponseResult getDetails(@PathVariable("id") int bId){
        log.info("查询商铺详情:{}",bId);

        AccessDTO accessDTO=new AccessDTO();
        accessDTO.setBusinessId(bId);

        //对应的访问表中记录这条访问记录
        accessService.addRecords(accessDTO);



        BusinessVO2 businessVO2= businessService.getBusinessDetail(bId);


        return ResponseResult.success(businessVO2);
    }


    /**
     * 附近商家（基于用户位置）
     * @param lat 用户纬度
     * @param lng 用户经度
     * @return
     */
    @GetMapping("/nearby")
    public ResponseResult nearBy(@RequestParam(required = false) Double lat,
                                 @RequestParam(required = false) Double lng) {
        log.info("附近商家: lat={}, lng={}", lat, lng);

        List<BusinessVO1> businessVO1s;

        // 判断是否提供了经纬度参数
        if (lat != null && lng != null) {
            // 使用经纬度查询附近50公里内的商户，按星级和评论数排序
            businessVO1s = businessService.nearBy(lat, lng);
        } else {
            // 如果没有提供位置信息，使用原有逻辑（按城市查询）
            businessVO1s = businessService.nearBy();
        }

        return ResponseResult.success(businessVO1s);
    }

}
