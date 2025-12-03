package com.sgrej.service.impl;


import com.sgrej.domain.dto.BusinessPageQueryDTO;
import com.sgrej.domain.dto.WorkBenchDTO;
import com.sgrej.domain.pojo.*;
import com.sgrej.domain.vo.*;
import com.sgrej.mapper.*;
import com.sgrej.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private TipMapper tipMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccessMapper accessMapper;


    @Autowired
    private User_RoleMapper userRoleMapper;

    /**
     * 搜索商铺基本信息
     * @param businessPageQueryDTO
     * @return
     */
    public PageResult pageQuery(BusinessPageQueryDTO businessPageQueryDTO) {
        PageHelper.startPage(businessPageQueryDTO.getPage(), businessPageQueryDTO.getPageSize());

        // 将搜索内容存进mysql表
        businessMapper.saveSearchContent(businessPageQueryDTO.getBusinessName(),Timestamp.valueOf(LocalDateTime.now()));


        // 首先从business表中得到商家的基本信息
        Page<BusinessVO> page = businessMapper.pageQuery(businessPageQueryDTO);

        // 如果需要，可以创建一个新的List来存储包含图片的BusinessVO对象
        List<BusinessVO1> businessVO1List = new ArrayList<>();

        // 遍历原始分页结果，并添加图片信息
        for (BusinessVO businessVO : page.getResult()) {
            // 得到每个商家的bid
            String bid = businessVO.getBid(); // 假设BusinessVO有getBid()方法

            // 得到商家对应的第一张图片
            String imageUrl = photoMapper.selectImage(bid);

            //TODO
            // 对得到的图片路径还需要进行拼接


            // 创建一个新的BusinessVO1对象或直接在BusinessVO上设置图片（如果允许）
            BusinessVO1 businessVO1 = new BusinessVO1();
            BeanUtils.copyProperties(businessVO, businessVO1); // 复制属性到BusinessVO1
            businessVO1.setImage(imageUrl); // 假设BusinessVO1有setImage()方法

            // 将包含图片的BusinessVO1对象添加到列表中
            businessVO1List.add(businessVO1);
        }

         /*如果直接使用原始的Page<BusinessVO>对象，并且只是添加了图片信息，
         则不需要新的Page<BusinessVO1>对象，可以直接返回包含图片的List。
         但如果API需要Page<BusinessVO1>作为响应，则需要创建一个新的Page对象。
         这里假设我们只需要List。

         注意：这里我们没有原始的总数，因为我们没有修改Page<BusinessVO>对象的逻辑。
         如果需要总数，您可能需要再次查询数据库或使用原始page的总数（如果它包含正确的总数）。
         但在实际应用中，通常Page对象会包含正确的总数，所以这里我们假设它包含。*/

        return new PageResult(page.getTotal(), businessVO1List); // 假设PageResult接受总数和List作为参数
    }




    /**
     * 商铺详情页
     */
    @Override
    public BusinessVO2 getBusinessDetail(int bId) {
        //首先根据businessId查询到对应商铺的详细信息
        Business business=businessMapper.selectDetailById(bId);

        //再根据bId找商户对应的图片信息
        String bid=business.getBid();
        List<String> images= photoMapper.selectImages(bid);

        //TODO
        //后面还需添加对应图片信息


        //查询该商铺收到的评价,并且找出对应的用户是谁
        List<Review> reviewList=reviewMapper.searchByBid(bid);
        List<ReviewVO> reviewVOS=new ArrayList<>();
        for(Review review:reviewList){
            String userName=userMapper.getUserName(review.getUserId());
            ReviewVO reviewVO=new ReviewVO();
            BeanUtils.copyProperties(review,reviewVO);
            reviewVO.setUserName(userName);
            reviewVOS.add(reviewVO);
        }

        // 添加小贴士
        List<Tips> tipList=tipMapper.searchByBid(bid);
        List<TipsVO> tipsVOList=new ArrayList<>();
        for(Tips tip:tipList){
            String userName=userMapper.getUserName(tip.getUserId());
            TipsVO tipsVO=new TipsVO();
            BeanUtils.copyProperties(tip,tipsVO);
            tipsVO.setUserName(userName);
            tipsVOList.add(tipsVO);
        }

        //将全部信息进行封装并且返回结果
        BusinessVO2 businessVO2=new BusinessVO2();
        businessVO2.setReviewVOList(reviewVOS);
        businessVO2.setTipList(tipsVOList);
        BeanUtils.copyProperties(business,businessVO2);
        businessVO2.setImageList(images);

        return businessVO2;
    }


    /**
     * 根据户主id查询对应的商户信息
     * @return
     */
    @Override
    public BusinessVO2 getBusinessDetailByUid() {
        //从context中获得当前户主id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int userId = user.getUser().getUserId();

        //根据户主id查询对应的商户id
        int bId=businessMapper.selectByUId(userId);

        //再根据对应的商户id查找基本信息进行返回
        BusinessVO2 businessDetail = getBusinessDetail(bId);
        return businessDetail;
    }


    /**
     * 更新基本信息
     * @param business
     */
    @Override
    public void changeInfo(Business business) {
        businessMapper.changeInfo(business);
    }


    /**
     * 获取附近商家信息
     *
     * @return
     */
    @Override
    public List<BusinessVO1> nearBy() {
       //1.获取当前用户所在城市
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        String city = loginUser.getUser().getCity();
        //从数据库中查找附近商家信息
        List<BusinessVO> businessVOs=businessMapper.getNearBy(city);

        //找到每个商家对应的图片信息
        List<BusinessVO1> businessVO1s= new ArrayList<>();

        for (BusinessVO businessVO : businessVOs) {
            // 得到每个商家的bid
            String bid = businessVO.getBid(); // 假设BusinessVO有getBid()方法

            // 得到商家对应的第一张图片
            String imageUrl = photoMapper.selectImage(bid);

            if("".equals(imageUrl) || imageUrl==null){
                continue;
            }
            //TODO
            // 对得到的图片路径还需要进行拼接


            // 创建一个新的BusinessVO1对象或直接在BusinessVO上设置图片（如果允许）
            BusinessVO1 businessVO1 = new BusinessVO1();
            BeanUtils.copyProperties(businessVO, businessVO1); // 复制属性到BusinessVO1
            businessVO1.setImage(imageUrl); // 假设BusinessVO1有setImage()方法

            // 将包含图片的BusinessVO1对象添加到列表中
            businessVO1s.add(businessVO1);
        }
        return businessVO1s;
    }


    /**
     * 工作台
     * @param workBenchDTO
     * @return
     */
    @Override
    public WorkBenchVO workBench(WorkBenchDTO workBenchDTO) {
        //得到时间周期
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate begin=workBenchDTO.getBegin();
        LocalDate end=workBenchDTO.getEnd();

        dateList.add(begin);

        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每一天的访客量
        List<Integer> reviewCount=new ArrayList<>();

        //得到每一天的访客量
        for(LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Timestamp begin1= Timestamp.valueOf(beginTime);
            Timestamp end1=Timestamp.valueOf(endTime);

            Integer count=accessMapper.getReviewCount(begin1,end1,workBenchDTO.getBusinessId());
            reviewCount.add(count);
        }

        String visitList= StringUtils.join(reviewCount,",");

        String timeList=StringUtils.join(dateList,",");

        //昨日新增
        int newVisits=0;
        if (reviewCount.size() >= 2) {
            int last = reviewCount.get(reviewCount.size() - 1); // 获取最后一个元素
            int secondToLast = reviewCount.get(reviewCount.size() - 2); // 获取倒数第二个元素
           newVisits = last - secondToLast; // 计算两者的差
        }

        int totalReview=accessMapper.getTotal(workBenchDTO.getBusinessId());

        WorkBenchVO workBenchVO=new WorkBenchVO();
        workBenchVO.setNewVisits(newVisits);
        workBenchVO.setXIndex(timeList);
        workBenchVO.setYIndex(visitList);
        workBenchVO.setLastDayVisitCount(reviewCount.get(reviewCount.size() - 1));
        workBenchVO.setTotalVisitCount(totalReview);

        return workBenchVO;
    }

    /**
     * 添加图片
     * @param path
     * @param businessId
     */
    @Override
    public void addImage(String path, String businessId) {
        photoMapper.addImage(path,businessId);
    }

    /**
     * 删除图片
     * @param fileName
     * @param businessId
     */
    @Override
    public void deleteImage(String fileName, String businessId) {
        photoMapper.deleteImage(fileName,businessId);
    }

    /**
     * 注册商家
     * @param business
     */
    @Override
    @Transactional
    public void register(Business business) {
       //注册商家信息并且返回id
        String bid= UUID.randomUUID().toString().substring(0,16).replaceAll("-", "");
        business.setBid(bid);
        business.setIsOpen(-1);
        businessMapper.register(business);
        int businessId=business.getBusinessId();
       //得到当前用户
        LoginUser loginUser=(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=loginUser.getUser().getUserId();
        //将户主-商家信息绑定到表中
       // userRoleMapper.insert(userId,2,-1);
        businessMapper.bindBusiness(userId,businessId);
    }
}
