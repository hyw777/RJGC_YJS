package com.sgrej.controller.bigdata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sgrej.domain.pojo.Business;
import com.sgrej.mapper.BusinessMapper;
import com.sgrej.service.BusinessService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rank")
@Slf4j
public class PVTop10Contorller {

    @Autowired
    private BusinessMapper businessMapper;

    @PostMapping("getRankList")
    public List<Business> getRankList(@RequestBody List<Integer> ids){
        System.out.println(ids.toString());
        List<Business> list =new ArrayList<>();
        for (Integer id : ids) {
            Business business = businessMapper.selectDetailById(id);
            list.add(business);
        }
        return list;
    }
}
