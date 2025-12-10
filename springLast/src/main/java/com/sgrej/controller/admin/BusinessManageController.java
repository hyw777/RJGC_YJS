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

    /**
     * 获取所有商户（分页）
     * @param page 页码
     * @param pageSize 每页大小
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listBusinesses(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = businessManageService.listBusinesses(page, pageSize);
        return ResponseResult.success(result);
    }

    /**
     * 修改商户状态
     * @param request 包含商户ID和状态的请求对象
     * @return
     */
    @PutMapping("/changeBusinessStatus")
    public ResponseResult changeBusinessStatus(@RequestBody Map<String, Object> request) {
        Integer businessId = (Integer) request.get("businessId");
        Integer isOpen = (Integer) request.get("isOpen");
        businessManageService.changeBusinessStatus(businessId, isOpen);
        return ResponseResult.success();
    }

}
