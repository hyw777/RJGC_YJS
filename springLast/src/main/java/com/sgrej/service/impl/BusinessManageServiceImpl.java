package com.sgrej.service.impl;

import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.News;
import com.sgrej.domain.vo.BusinessApplyVO;
import com.sgrej.domain.vo.BusinessVO;
import com.sgrej.mapper.BusinessMapper;
import com.sgrej.mapper.NewMapper;
import com.sgrej.mapper.UserBusinessMapper;
import com.sgrej.mapper.User_RoleMapper;
import com.sgrej.service.BusinessManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessManageServiceImpl implements BusinessManageService {

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private UserBusinessMapper userBusinessMapper;

    @Autowired
    private User_RoleMapper userRoleMapper;

    @Autowired
    private NewMapper newMapper;

    /**
     * 商户申请信息
     * @return
     */
    @Override
    public List<BusinessApplyVO> getApplys() {
        List<Business> businessList=businessMapper.getApplys();
        List<BusinessApplyVO> businessApplyVOList=new ArrayList<>();
        for(Business business:businessList){
            BusinessApplyVO businessApplyVO=new BusinessApplyVO();
            businessApplyVO.setApplyTime(business.getCreatedAt());
            BeanUtils.copyProperties(business,businessApplyVO);
            businessApplyVOList.add(businessApplyVO);
        }
        return businessApplyVOList;
    }

    /**
     * 同意or拒绝申请？
     * @param businessId
     * @param result
     */
    @Override
    @Transactional
    public void isAgree(int businessId, int result) {
        //如果拒绝，则删除对应信息
        int userId= userBusinessMapper.getBossId(businessId);
        if(result == 0){
            //删除这条商户信息
            businessMapper.delete(businessId);
            //删除对应的用户商户绑定信息
            userBusinessMapper.delete(businessId);
            //删除对应的用户身份转换信息
         //  userRoleMapper.delete(userId,-1)
        }
        else if (result == 1) {
            //如果同意，则修改对应信息
            //将商家改为上线状态
            businessMapper.online(businessId);
            //转换用户对应的角色类型
            userRoleMapper.changeRole(userId,2);
            //将这个消息添加到news表中
            Business business = businessMapper.selectDetailById(businessId);
            String businessName=business.getName();
            News news = new News();
            news.setTime(Timestamp.valueOf(LocalDateTime.now()));
            news.setContent("新店《"+businessName+"》在"+LocalDateTime.now()+"上线啦，快来看看吧");
            news.setUserId(-1);
            newMapper.add(news);
        }
    }

    @Override
    public Map<String, Object> listBusinesses(Integer page, Integer pageSize) {
        // 计算起始位置
        int start = (page - 1) * pageSize;

        // 查询数据
        List<BusinessVO> businesses = businessMapper.selectAllBusinesses(start, pageSize);
        int total = businessMapper.countAllBusinesses();

        // 封装分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", businesses);
        result.put("total", total);
        result.put("current", page);
        result.put("size", pageSize);
        result.put("pages", (total + pageSize - 1) / pageSize);

        return result;
    }

    @Override
    public void changeBusinessStatus(Integer businessId, Integer isOpen) {
        businessMapper.updateBusinessStatus(businessId, isOpen);
    }


}
