package com.sgrej.controller.admin;

import com.sgrej.domain.dto.BusinessResultDTO;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessApplyVO;
import com.sgrej.service.BusinessManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/businessManage")
public class BusinessManageController {
    @Autowired
    private BusinessManageService businessManageService;

    /**
     * 商户注册申请
     * @return
     */
    @GetMapping("/getApply")
    public ResponseResult application(){
        List<BusinessApplyVO> businessApplyVOList=businessManageService.getApplys();
        return ResponseResult.success(businessApplyVOList);
    }

    /**
     * 同意or拒绝申请？

     */
    @PutMapping("/isAgree")
    public ResponseResult isAgree(@RequestBody BusinessResultDTO businessResultDTO){
        log.info("是否同意该商家申请：");
        businessManageService.isAgree(businessResultDTO.getBusinessId(),businessResultDTO.getResult());
        return ResponseResult.success();
    }

}
