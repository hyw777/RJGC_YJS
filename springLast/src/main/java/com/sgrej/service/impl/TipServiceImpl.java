package com.sgrej.service.impl;

import com.sgrej.domain.dto.TipDTO;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.mapper.TipMapper;
import com.sgrej.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TipServiceImpl implements TipService {
    @Autowired
    private TipMapper tipMapper;


    /**
     * 增加提示
     * @param tipDTO
     */
    @Override
    public void addTip(TipDTO tipDTO) {
        //得到当前用户id和时间
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);

        //对传来的tipDTO的时间和用户id字段赋值
        tipDTO.setUserId(userId);
        tipDTO.setDatePosted(time);

        //向表中添加数据
        tipMapper.addTip(tipDTO);
    }

    /**
     * 删除提示
     * @param tipId
     */
    @Override
    public void delete(int tipId) {
        tipMapper.delete(tipId);
    }
}