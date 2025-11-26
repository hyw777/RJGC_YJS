package com.sgrej.controller.Business;

import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.handler.MultipartFileHandler;
import com.sgrej.service.BusinessService;
import com.sgrej.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private BusinessService businessService;
 
    //上传图片
    @Transactional
    @PostMapping("/uploadImage")
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file,@RequestParam String bId){
        log.info("上传图片",file);
    /*    if(file.isEmpty()) { return ResponseResult.error("文件为空"); }
        //获取文件后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String[] suffixes = new String[]{//常见图片后缀集合
                ".jpeg",
                ".jpg",
                ".png",
                ".bmp"
        };
        boolean bool = false;
        for(String string : suffixes){//判断后缀是否是常见图片后缀集合中的后缀
            if (string.equals(suffix)){
                bool = true;
            }
        }*/

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString().substring(0,16).replaceAll("-", "") + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            //并且将相应的图片路劲和对应的商家绑定到一起
            businessService.addImage(filePath,bId);
            return ResponseResult.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return ResponseResult.error("失败");
    }
    
    //下载图片
    @GetMapping ("/downloadImage")
    public String downloadImage(HttpServletResponse response, @RequestParam("fileName") String fileName){
        String contentType = "image/jpeg";
        MultipartFileHandler.downloadFile(response,fileName,contentType);//下载文件
        return null;
    }
    
    //删除图片
    @DeleteMapping("/deleteImage")
    public ResponseResult<String> deleteImage(@RequestParam("fileName") String fileName,@RequestParam  String businessId){
        log.info("删除图片：{}",fileName);
        String s = MultipartFileHandler.deleteFile(fileName);//删除文件并返回结果
        businessService.deleteImage(fileName,businessId);
        return  ResponseResult.success(s);
    }
 
}