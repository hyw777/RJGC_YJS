package com.sgrej.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sgrej.domain.dto.EditUserDTO;
import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 注册用户
     * @param newUser
     * @return
     */
    int register(User newUser);

    /**
     * 编辑用户
     * @param editUser
     * @return
     */
    int editUser(EditUserDTO editUser);

    /**
     * 查询用户
     * @param id
     * @return
     */
    @Select("select uid,user_id,name,password,email,gender,age,city,review_count,cool,useful,elite,yelping_since,funny,friends from user where user_id = #{id}")
    User selectById(int id);

    @Select("select name from user where uid = #{userId}")
    String getUserName(String userId);

    @Select("select name from user where user_id = #{friendId}")
    String getFriendName(int friendId);

    @Select("select user_id from user where uid = #{friend}")
    int getUserIdByUid(String friend);

    Page<User> findFriend(FindFriendDTO findFriendDTO);

    @Select("select uid,user_id,name,password,email,gender,age,city,review_count,cool,useful,elite,yelping_since,funny,friends from user where uid = #{id}")
    User getUserByUid(String id);

    @Update("update user set friends = #{newFriends} where user_id = #{userId}")
    void updateFriendByuId(int userId, String newFriends);

    Page<User> findFriend2(FindFriendDTO findFriendDTO,List<Integer> friendIds);

    Map<String, String> getUserNamesByUserIds(List<String> userIds);

}
