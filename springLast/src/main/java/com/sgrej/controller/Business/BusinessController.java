package com.sgrej.controller.Business;
import com.sgrej.domain.dto.WorkBenchDTO;
import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessVO2;
import com.sgrej.domain.vo.WorkBenchVO;
import com.sgrej.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController("businessController2")
@RequestMapping("/business")
@Slf4j
public class BusinessController {
    @Autowired
    private BusinessService businessService;


    /**
     * 商户基本信息
     * @return
     */
    @GetMapping("/baseInfo")
    public ResponseResult baseInfo(){
        log.info("基本信息展示");
      BusinessVO2 businessVO2=businessService.getBusinessDetailByUid();
      return ResponseResult.success(businessVO2);
    }

    @GetMapping("/stores")
    public ResponseResult storesByUid(){
        log.info("商铺列表");
        //从context中获得当前户主id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int userId = user.getUser().getUserId();
        List<Integer> businessList=businessService.bIdsByUid(userId);
        
        // 创建一个Map来存储bId和店铺名称的映射关系
        Map<Integer, String> businessNameMap = new HashMap<>();
        for (Integer bId : businessList) {
            String businessName = businessService.getBusinessNameById(bId);

            businessNameMap.put(bId, businessName);
        }
        
        return ResponseResult.success(businessNameMap);
    }

    @GetMapping("/businessInfo")
    public ResponseResult businessInfo(@RequestParam Integer bId){
        log.info("商户信息");
        System.out.println(bId);
        BusinessVO2 businessVO2=businessService.getBusinessDetail(bId);
        return ResponseResult.success(businessVO2);
    }

    @PostMapping("/AIRecommend")
    public ResponseResult AIRecommend(@RequestParam String text){
        log.info("AI推荐");
        return ResponseResult.success();
    }
    /**
     * 修改基本信息
     * @param business
     * @return
     */
    @PutMapping("/changeInfo")
    public ResponseResult changeInfo(@RequestBody Business business){
        log.info("修改基本信息:{}",business);
        businessService.changeInfo(business);
        return ResponseResult.success("更新成功");
    }

    /**
     * 工作台
     * @param
     * @return
     */
    @PostMapping("/workBench")
    public ResponseResult workBench(@RequestBody WorkBenchDTO workBenchDTO){
        log.info("工作台");
        WorkBenchVO workBenchVO=businessService.workBench(workBenchDTO);
        return ResponseResult.success(workBenchVO);
    }


    /**
     * 注册商家
     * @param business
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody Business business){
        log.info("注册商家");
        businessService.register(business);
        return ResponseResult.success();
    }

}
