package com.sgrej.service.impl;

import com.sgrej.domain.dto.AccessDTO;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.mapper.AccessMapper;
import com.sgrej.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AccessServiceImpl implements AccessService{

    @Autowired
    private AccessMapper accessMapper;

    /**
     * 用户查看商户详情时增加对应的访问记录
     * @param accessDTO
     */
    @Override
    public void addRecords(AccessDTO accessDTO) {
        //得到当前登录用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();

        // 转换日期格式
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());

        accessDTO.setTime(time);
        accessDTO.setUserId(userId);

        //添加记录到表中
        accessMapper.addRecord(accessDTO);
    }
}
