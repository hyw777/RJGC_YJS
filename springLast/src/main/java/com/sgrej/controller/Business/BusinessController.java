package com.sgrej.controller.Business;
import com.sgrej.domain.dto.WorkBenchDTO;
import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessVO2;
import com.sgrej.domain.vo.WorkBenchVO;
import com.sgrej.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
