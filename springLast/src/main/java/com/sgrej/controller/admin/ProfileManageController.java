package com.sgrej.controller.admin;

import com.sgrej.domain.dto.ProfileDTO;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.BusinessVO2;
import com.sgrej.domain.vo.ProfileInfoVO;
import com.sgrej.domain.vo.UserVO;
import com.sgrej.service.ProfileService;
import com.sgrej.utils.RedisCache;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/profileManage")
public class ProfileManageController{

    @Autowired
    private ProfileService profileService;

    /**
     * 得到所有画像类别
     * @return
     */
    @GetMapping("/getCategories")
    public ResponseResult getProfile(){
        log.info("得到所有画像类别");
    List<ProfileInfoVO> profileInfoVOList=profileService.getProFileCategories();
      return ResponseResult.success(profileInfoVOList);
    }

    /**
     * 得到某一类画像里面的所有信息
     * @return
     */
    @PostMapping("/getDetail")
    public ResponseResult getDetail(@RequestBody ProfileDTO profileDTO){

        log.info("得到某一类画像里面的所有信息:{}",profileDTO);
        if(profileDTO.getGroupName().contains("用户")){
            List<UserVO> userVOList= profileService.getUserDetails(profileDTO.getId(),profileDTO.getPageNum(),10);
            return ResponseResult.success(userVOList);
        }
        if(profileDTO.getGroupName().contains("商户")){
            List<BusinessVO1> businessVO1List=profileService.getBusinessDetails(profileDTO.getId(),profileDTO.getPageNum(),6);
            return ResponseResult.success(businessVO1List);
        }


        return ResponseResult.error("失败了" );
    }

}
