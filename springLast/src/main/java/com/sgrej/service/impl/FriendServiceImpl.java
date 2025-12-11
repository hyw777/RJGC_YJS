package com.sgrej.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgrej.domain.dto.AgreeDTO;
import com.sgrej.domain.dto.ApplicationDTO;
import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.*;
import com.sgrej.domain.vo.*;
import com.sgrej.mapper.*;
import com.sgrej.service.FriendService;
import net.minidev.json.writer.CollectionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ApplyMapper applyMapper;

    /**
     * 朋友最近
     * @return
     */
    @Override
    @Transactional
    public FriendRecentVO getFriendRecent() {
        int userId=getUserId();

        //得到用户朋友信息
        User user=userMapper.selectById(userId);
        String friendsId=user.getFriends();

        //将朋友id进行分割成数据,并查找对应的最近一个月内的评论信息和收藏信息
        String[] friends=friendsId.split(", ");

        //用来接收朋友们的评论信息
        List<ReviewVO> reviewVOList=new ArrayList<>();

        //用来接收朋友们的收藏信息
        List<CollectionVO> collectionVOList=new ArrayList<>();

        //创建时间周期

        //结束时间
        Timestamp endTime= Timestamp.valueOf(LocalDateTime.now());

        //得到一个月前的时间点
        Timestamp startTime=Timestamp.valueOf(LocalDateTime.now().plusDays(-30));

         //并查找对应的最近一个月内的评论信息和收藏信息
        for (int i = 0; i < friends.length; i++) {
            //每个朋友的名字
            String userName=userMapper.getUserName(friends[i]);

            //每个朋友的主id
            int friendId=userMapper.getUserIdByUid(friends[i]);

            //每个朋友的评论信息
            List<Review> reviews = reviewMapper.searchByUidAndTime(friends[i],startTime,endTime);

            //对每个朋友的评论进行封装
            for(Review review:reviews){
                //首先找到评论对应的商家名和用户名
                String businessName = businessMapper.selectBusinessName(review.getBusinessId());

                int businessId=businessMapper.selectBusinessByBid(review.getBusinessId());
                //创建ReviewVO对象用于接收
                ReviewVO reviewVO=new ReviewVO();
                BeanUtils.copyProperties(review,reviewVO);
                reviewVO.setUserName(userName);
                reviewVO.setBusinessName(businessName);
                reviewVO.setBusinessId(businessId);
                //将其添加到reviewVoList中
                reviewVOList.add(reviewVO);
            }

            //得到每个朋友的收藏信息
            //根据用户id从收藏表中拿到收藏记录
            //应该是在这里对时间进行判断
            List<Collection> collections=collectMapper.getCollectionsLimitTime(friendId,startTime,endTime);


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
                collectionVO.setUserName(userName);
                collectionVOList.add(collectionVO);
            }
        }


        return new FriendRecentVO(reviewVOList,collectionVOList);
    }


    /**
     * 找朋友
     * @param findFriendDTO
     * @return
     */
    @Override
    public PageResult findFriend(FindFriendDTO findFriendDTO) {
        PageHelper.startPage(findFriendDTO.getPageNum(),findFriendDTO.getPageSize());
        if(findFriendDTO.getEmail() == null && findFriendDTO.getUserName() == null ){
            return null;
        }
        else {
            List<FindfriendVO> findFriendVOS=new ArrayList<>();
           Page<User> users = userMapper.findFriend(findFriendDTO);
            for(User user:users.getResult()){
                if(user.getUserId()==getUserId())
                    continue;
                FindfriendVO findfriendVO=new FindfriendVO();
                //复制基础属性
                BeanUtils.copyProperties(user,findfriendVO);

                //找对应用户的评论总数
                if(user.getFriends()!= null) {
                    int friendNumber = user.getFriends().split(", ").length;
                    findfriendVO.setFriendNumbers(friendNumber);
                }

                findfriendVO.setReviewCount(user.getReviewCount());
                findFriendVOS.add(findfriendVO);
            }
            return new PageResult(users.getTotal(),findFriendVOS);
        }
    }

    /**
     * 添加朋友
     * @param applicationDTO
     */
    @Override
    public void addFriend(ApplicationDTO applicationDTO) {
        //封装申请信息
            int userId=getUserId();
            Timestamp time=Timestamp.valueOf(LocalDateTime.now());
            applicationDTO.setApplicantId(userId);
            applicationDTO.setTime(time);

        //将申请记录插入表中
            applyMapper.add(applicationDTO);
    }



    private int getUserId(){
        //得到当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();
        return userId;
    }


    /**
     * 未通过好友数
     * @return
     */
    @Override
    public int unPass() {
        int userId=getUserId();
        return applyMapper.getUnPass(userId);
    }

    /**
     * 好友申请信息
     * @return
     */
    @Override
    public List<Apply> getApplys() {
        int userId=getUserId();
        List<Apply> applyList=applyMapper.getInfo(userId);
        return applyList;
    }


    /**
     * 同意or拒绝?
     * @param agreeDTO
     */
    @Override
    public void isAgree(AgreeDTO agreeDTO) {
        //同意的话就加好友，反之
        if(agreeDTO.getResult()==1){
            //自己这里先加上他
            User userMe = userMapper.selectById(getUserId());
            User userHe = userMapper.selectById(agreeDTO.getApplicantId());
            String friends;
            if(userMe.getFriends()==null || userMe.getFriends().isEmpty())
            friends=userHe.getUid();
            else {
                friends=userHe.getUid()+", "+userMe.getFriends();
            }
            userMapper.updateFriendByuId(getUserId(),friends);

            //他那里也要加上我
            if(userHe.getFriends()==null || userHe.getFriends().isEmpty()){
                friends=userMe.getUid();
            }
            else {
                friends=userMe.getUid()+", "+userHe.getFriends();
            }
            userMapper.updateFriendByuId(userHe.getUserId(),friends);

        }
        applyMapper.delete(getUserId(),agreeDTO.getApplicantId());
    }

    /**
     * 我的好友
     * @return
     */
    @Override
    public List<FindfriendVO> getMyFriends() {
        //得到当前登录用户好友
        int userId=getUserId();
        User user = userMapper.selectById(userId);
        String friendsId=user.getFriends();

        //查找每个好友信息
        if(friendsId==null || friendsId == "")
            return null;
        else {
            String[] friends = friendsId.split(", ");
            List<FindfriendVO> findFriendVOS=new ArrayList<>();
            for (int i = 0; i < friends.length; i++) {
                User friend=userMapper.getUserByUid(friends[i]);
                FindfriendVO findfriendVO=new FindfriendVO();
                //复制基础属性
                BeanUtils.copyProperties(friend,findfriendVO);

                //找对应用户的朋友总数
                if(friend.getFriends()!= null) {
                    int friendNumber = friend.getFriends().split(", ").length;
                    findfriendVO.setFriendNumbers(friendNumber);
                }

                findFriendVOS.add(findfriendVO);


            }

            return findFriendVOS;
        }
    }

    /**
     * 删除好友
     * @param uid
     */
    @Override
    public void deleteFriend(String uid) {
        //首先得到用户好友信息
        int userId=getUserId();
        User user = userMapper.selectById(userId);
        String friendsId=user.getFriends();
        if (friendsId == null || friendsId.isEmpty()) {
            // 没有好友，无需处理
            return;
        }

        // 将每个用户id分离出来（注意这里使用了", "作为分隔符）
        String[] friends = friendsId.split(", ");

        // 创建一个新的列表来存储剩余的好友ID
        List<String> remainingFriends = new ArrayList<>();

        // 遍历数组，移除包含指定uid的元素
        for (String friendId : friends) {
            if (!friendId.trim().equals(uid)) { // 使用了trim()去除空格后进行比较
                remainingFriends.add(friendId);
            }
        }

        // 将剩余的ID重新组合成以逗号加空格分隔的字符串
        String newFriendsId = String.join(", ", remainingFriends);

        // 更新用户的好友列表字段
        userMapper.updateFriendByuId(userId,newFriendsId); // 假设userMapper有一个updateById方法来更新用户




        //对方那里也需要删除自己的好友信息
        User userHe=userMapper.getUserByUid(uid);
        friendsId=userHe.getFriends();
        if (friendsId == null || friendsId.isEmpty()) {
            // 没有好友，无需处理
            return;
        }
        // 将每个用户id分离出来（注意这里使用了", "作为分隔符）
          friends = friendsId.split(", ");

        // 创建一个新的列表来存储剩余的好友ID
        List<String> remainingFriendsHe = new ArrayList<>();

        // 遍历数组，移除包含指定uid的元素
        for (String friendId : friends) {
            if (!friendId.trim().equals(user.getUid())) { // 使用了trim()去除空格后进行比较
                remainingFriendsHe.add(friendId);
            }
        }

        // 将剩余的ID重新组合成以逗号加空格分隔的字符串
        newFriendsId = String.join(", ", remainingFriendsHe);

        // 更新用户的好友列表字段
        userMapper.updateFriendByuId(userHe.getUserId(),newFriendsId); // 假设userMapper有一个updateById方法来更新用户


    }

    /**
     * 好友详情
     * @param userId
     * @return
     */

    @Override
    public FriendDetailVO getFriendDetail(int userId) {
        //创建FriendDetailVO对象
        FriendDetailVO friendDetailVO=new FriendDetailVO();

        //设置userVO属性
        User user=userMapper.selectById(userId);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        friendDetailVO.setUserVO(userVO);

        //设置reviewsList属性
        //查询用户对应的评论信息;
        String uId=user.getUid();
        List<Review> reviews=reviewMapper.searchByUid(uId);
        List<ReviewVO> reviewVOS=new ArrayList<>();
        //查询是对应哪个商家做出的评价
        for (Review review:reviews) {
            String businessName = businessMapper.selectBusinessName(review.getBusinessId());
            ReviewVO reviewVo=new ReviewVO();
            BeanUtils.copyProperties(review,reviewVo);
            reviewVo.setBusinessName(businessName);
            reviewVOS.add(reviewVo);
        }
        friendDetailVO.setReviewsList(reviewVOS);

        //设置collectionVOList属性
        //根据用户id从收藏表中拿到收藏记录
        List<Collection> collections=collectMapper.getCollections(userId);

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
        friendDetailVO.setCollectionVOList(collectionVOS);


        //设置findfriendVOS
        //查找每个好友信息
        String friendsId=user.getFriends();
        String[] friends=friendsId.split(", ");
        if(friendsId == null || friendsId == "" || friendsId.isEmpty()){
            return null;
        }

        else {
            List<FindfriendVO> findFriendVOS = new ArrayList<>();
            for (int i = 0; i < friends.length; i++) {
                User friend = userMapper.getUserByUid(friends[i]);
                if(friend==null) continue;

                FindfriendVO findfriendVO = new FindfriendVO();
                //复制基础属性
                BeanUtils.copyProperties(friend, findfriendVO);

                //找对应用户的评论总数和朋友总数
                if (friend.getFriends() != null) {
                    int friendNumber = user.getFriends().split(", ").length;
                    findfriendVO.setFriendNumbers(friendNumber);
                }

                findFriendVOS.add(findfriendVO);
            }
            friendDetailVO.setFindfriendVOS(findFriendVOS);
        }
        return friendDetailVO;
    }
}
