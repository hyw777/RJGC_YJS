package com.sgrej.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sgrej.domain.dto.*;
import com.sgrej.domain.pojo.*;
import com.sgrej.domain.vo.ReviewVO;
import com.sgrej.domain.vo.TipsVO;
import com.sgrej.domain.vo.UserVO;
import com.sgrej.mapper.*;
import com.sgrej.service.UserService;
import com.sgrej.utils.JwtUtil;
import com.sgrej.utils.MailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.sgrej.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManger;

    @Autowired
    private RedisCache redisCache;

    //注入邮箱验证码工具
    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private User_RoleMapper userRoleMapper;

    @Autowired
    private ReviewMapper reviewMapper;


    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private NewMapper newMapper;


    /**
     * 用户登录
     * @param loginUserDTO
     * @return
     */
    @Override
    public ResponseResult login(LoginUserDTO loginUserDTO) {

        //AuthenticationManager authenticate进行用户认证

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(),loginUserDTO.getPassword());
        Authentication authenticate = authenticationManger.authenticate(authenticationToken);
        System.out.println(authenticate);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate))
            throw new RuntimeException("登陆失败");

        //如果认证通过了，使用uuid生成一个jwt,存入ResponseResult返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String  userId=String.valueOf(loginUser.getUser().getUserId());
        String jwt = JwtUtil.createJWT(userId);
        loginUser.setToken(jwt);
        String userType=loginUser.getPermissions().get(0);

        // 获取用户名
        String userName = loginUser.getUser().getName();

        Map map=new HashMap<>();
        map.put("token",jwt);
        map.put("userType",userType);
        map.put("userName", userName); // 添加用户名到返回数据中

        //把完整的用户信息存入redis  uuid作为key
        redisCache.setCacheObject("login:"+userId,loginUser,5,TimeUnit.HOURS);
        return  ResponseResult.success(map);
    }


    /**
     * 用户登出
     */
    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int uid = user.getUser().getUserId();
        redisCache.deleteObject("login:"+uid);
    }


    /**
     * 用户注册
     */
    /**
     *
     * @param registerUserDTO
     */
    @Override
    @Transactional
    public void register(RegisterUserDTO registerUserDTO) {

        //得到验证码
        String realCode = redisCache.getCacheObject("code:"+registerUserDTO.getEmail());
        if(registerUserDTO.getCode()==null ||  registerUserDTO.getCode()== ""){
            throw new RuntimeException("验证码为空");
        }
        if(realCode==null){
            throw new RuntimeException("验证码已过期，请重新发送验证码");
        }
        else {
            //验证码错误，抛出异常
            if (!realCode.equals(registerUserDTO.getCode())) {
                throw new RuntimeException("验证码错误,请重新填写");
            }
            //验证码正确，执行注册
            else {
                User newUser = new User();
                BeanUtils.copyProperties(registerUserDTO, newUser);
                //首先判断该邮箱是否已经被注册过
                //查询用户信息
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getEmail, newUser.getEmail());
                User user = userMapper.selectOne(queryWrapper);
                //如果查询到存在使用该邮箱注册的用户就抛出异常
                if (!Objects.isNull(user)) {
                    throw new RuntimeException("该邮箱已被注册过，请重新注册");
                }
                //不存在就进行注册用户
                newUser = registerUser(newUser);
                int register = userMapper.register(newUser);
                if(register==0){
                    throw new RuntimeException("注册失败");
                }
                System.out.println(newUser.getUserId());
                //同时将用户对应的角色写入用户-角色表中
                int permission= userRoleMapper.insert(newUser.getUserId(),1,0);
                if(permission == 0){
                    throw new RuntimeException("给用户生成权限失败");
                }

            }
        }
    }


    /**
     * 发送验证码
     * @param email
     */
    @Override
    public void sendCode(String email) {
        String code = UUID.randomUUID().toString().substring(0,6);
            /*邮件内容(随机生成验证码)*/
            boolean sendMail = mailUtils.sendMail(email, "请不要将验证码泄露给其他人，验证码为:" + code + "，五分钟内有效，请尽快完成", "小众点评");
            if (sendMail == false) {
                throw new RuntimeException("验证码发送失败，请重试");
            }
        //把验证码暂时存入redis中，用于注册时进行校验
        redisCache.setCacheObject("code:"+email,code,5, TimeUnit.MINUTES);
    }


    /**
     * 封装新用户
     */
    private User registerUser(User newUser){
        //对密码进行加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password= passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(password) ;


        /**
         * 转换日期格式
         */
        Timestamp yelpingTime = Timestamp.valueOf(LocalDateTime.now());
        newUser.setYelpingSince(yelpingTime);


        //设置uid
        String uid = UUID.randomUUID().toString().substring(0,22).replaceAll("-", "");
        newUser.setUid(uid);

        return newUser;
    }


    /**
     * 编辑个人资料
     * @param editUser
     */
    @Override
    public void edit(EditUserDTO editUser) {
        //首先从SecurityContextHolder拿到用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user=(LoginUser)authentication.getPrincipal();
        int uid = user.getUser().getUserId();
        //将id存入editUser中
        editUser.setUserId(uid);
        int result=userMapper.editUser(editUser);
        if (result==0){
            throw new RuntimeException("更新失败，请重试");
        }//否则更新后还需进行删除缓存信息，重新登陆
        else {
            redisCache.deleteObject("login:"+uid);
        }
    }


    /**
     * 找回密码
     * @param findUser
     */
    @Override
    public void findUser(FindUserDTO findUser) {
        //2.判断验证码是否正确
        //得到验证码
        String realCode = redisCache.getCacheObject("code:"+findUser.getEmail());
        if(realCode==null){
            throw new RuntimeException("验证码已过期，请重新发送验证码");
        }
        if(findUser.getCode()==null){
            throw new RuntimeException("验证码为空");
        }
        else {
            //验证码错误，抛出异常
            if (!realCode.equals(findUser.getCode())) {
                throw new RuntimeException("验证码错误,请重新填写");
            }
            //验证码正确，执行重设新密码
            else {
                //3.设置新密码
                //首先判断是否存在该用户
                //查询用户信息
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getEmail, findUser.getEmail());
                User user = userMapper.selectOne(queryWrapper);
                //如果查询到不存在该用户用户就抛出异常
                if (Objects.isNull(user)) {
                    throw new RuntimeException("该用户不存在，请重试");
                }
                else {
                    EditUserDTO editUserDTO=new EditUserDTO();
                    editUserDTO.setUserId(user.getUserId());
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String newPassword= passwordEncoder.encode(findUser.getNewPassword());
                    editUserDTO.setPassword(newPassword);
                    userMapper.editUser(editUserDTO);
                }
            }
        }
    }


    /**
     * 更改密码
     * @param updatePwdDTO
     */
    @Override
    public void updatePwd(UpdatePwdDTO updatePwdDTO) {
        String oldPwd=updatePwdDTO.getOldPwd();
        String newPwd = updatePwdDTO.getNewPwd();
        String rePwd = updatePwdDTO.getRePwd();
        //1.首先判断原密码是否输入正确
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(oldPwd, loginUser.getUser().getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        //2.判断两次输入的密码是否一致
        if(!newPwd.equals(rePwd)){
            throw new RuntimeException("两次输入的新密码不一致");
        }
        //3.判断输入的新密码是否和旧密码重复
        if(newPwd.equals(oldPwd)){
            throw new RuntimeException("新密码不能和旧密码一致");
        }
        EditUserDTO editUserDTO=new EditUserDTO();
       newPwd= passwordEncoder.encode(newPwd);
       editUserDTO.setPassword(newPwd);
        int id=loginUser.getUser().getUserId();
       editUserDTO.setUserId(id);
        userMapper.editUser(editUserDTO);
        //修改之后要清除缓存--即退出登陆
        redisCache.deleteObject("login:"+id);
    }

    /**
     * 个人中心
     * @return
     */
    @Override
    public UserVO getUserVO() {
        //得到当前登录用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();

        //查询当前用户信息
        User user = userMapper.selectById(userId);

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

        //创建UserVO对象封装信息返回给前端展示
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setReviewsList(reviewVOS);
        return userVO;
    }


    /**
     * 新事件
     * @return
     */
    @Override
    @Transactional
    public List<News> getNews() {
        //得到当前登录用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();

        /**
         * 从News表中查询公共信息或者与自己相关的消息
         */
        List<News> newsList=newMapper.getWithMe(userId);

        //并且删除与自己相关的信息
        newMapper.deleteInUser(userId);

        return newsList;
    }

    @Scheduled(fixedRate = 1000*60*60*24)
    public void updateNews(){
        //每天定时删除已经超过三天时间的公众消息
        newMapper.deleteInTime();
    }

    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    @Override
    public UserVO getUserVO2(int userId) {
        //查询当前用户信息
        User user = userMapper.selectById(userId);

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

        //创建UserVO对象封装信息返回给前端展示
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setReviewsList(reviewVOS);
        return userVO;
    }
}
