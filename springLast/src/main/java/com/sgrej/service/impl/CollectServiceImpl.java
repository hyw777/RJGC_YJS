package com.sgrej.service.impl;

import com.sgrej.domain.pojo.Collection;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.domain.vo.BusinessVO;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.CollectionVO;
import com.sgrej.mapper.BusinessMapper;
import com.sgrej.mapper.CollectMapper;
import com.sgrej.mapper.PhotoMapper;
import com.sgrej.service.CollectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * 收藏商铺
     * @param businessId
     */
    @Override
    public void save(int businessId) {
        //首先从SecurityContextHolder拿到用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int uid = user.getUser().getUserId();

        //得到当前系统时间
        Timestamp date = Timestamp.valueOf(LocalDateTime.now());

        //将数据插入到收藏列表中
        collectMapper.save(businessId,uid,date);
    }

    /**
     * 我的收藏列表
     * @return
     */
    @Override
    public List<CollectionVO> getCollect() {
        //首先从SecurityContextHolder拿到用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int uid = user.getUser().getUserId();

        //根据用户id从收藏表中拿到收藏记录
        List<Collection> collections=collectMapper.getCollections(uid);

        List<CollectionVO> collectionVOS=new ArrayList<>();

        //遍历businessIds得到对应的商户信息
        for(Collection collection:collections){
            int businessId=collection.getBusinessId();
           BusinessVO businessVO= businessMapper.getById(businessId);
           String bid=businessVO.getBid();
            // 得到商家对应的第一张图片
            String imageUrl = photoMapper.selectImage(bid);
            //TODO
            // 对得到的图片路径还需要进行拼接


            // 创建一个新的BusinessVO1对象或直接在BusinessVO上设置图片（如果允许）
            BusinessVO1 businessVO1 = new BusinessVO1();
            BeanUtils.copyProperties(businessVO, businessVO1); // 复制属性到BusinessVO1
            businessVO1.setImage(imageUrl); // 假设BusinessVO1有setImage()方法

            CollectionVO collectionVO=new CollectionVO();

            collectionVO.setDate(collection.getDate());
            collectionVO.setBusinessVO1(businessVO1);

            collectionVOS.add(collectionVO);
        }



        return collectionVOS;
    }


    /**
     * 取消收藏
     * @param businessId
     */
    @Override
    public void cancel(int businessId) {
        //首先从SecurityContextHolder拿到用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int uid = user.getUser().getUserId();

        //从对应的收藏表中删除该条记录
        collectMapper.delete(businessId,uid);
    }
}
