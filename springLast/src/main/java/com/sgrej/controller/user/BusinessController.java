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
     * 附近商家（登陆状态才进行展示）
     * @return
     */
    @GetMapping("/nearby")
    public ResponseResult nearBy(){
        log.info("附近商家");
        List<BusinessVO1> businessVO1s=businessService.nearBy();
        return ResponseResult.success(businessVO1s);
    }

}
