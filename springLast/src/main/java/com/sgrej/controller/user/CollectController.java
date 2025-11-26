package com.sgrej.controller.user;

import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.CollectionVO;
import com.sgrej.service.CollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 收藏商铺
     * @param businessId
     * @return
     */
    @PostMapping("/save")
    public ResponseResult save(int businessId){
        log.info("收藏商铺：{}",businessId);
        collectService.save(businessId);
        return ResponseResult.success("收藏成功");
    }

    /**
     * 我的收藏
     * @return
     */
    @GetMapping("/myCollect")
    public ResponseResult myCollect(){
        log.info("我的收藏");
        List<CollectionVO> collect= collectService.getCollect();
        return ResponseResult.success(collect);
    }

    /**
     * 取消收藏
     * @param businessId
     * @return
     */
    @DeleteMapping("/cancel")
    public ResponseResult cancel(int businessId){
        log.info("取消收藏");
        collectService.cancel(businessId);
        return ResponseResult.success("取消成功");
    }
}
