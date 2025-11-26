package com.sgrej.handler;

import org.springframework.web.multipart.MultipartFile;
 
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
 
/**
 * 文件上传,下载和删除
 */
public class MultipartFileHandler {
 
    //配置文件保存路径(可以修改成任意路径，看个人喜好)
    private static final String basePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
 
    /**
     * 上传文件
     * @param file 文件
     * @return  成功则返回文件名
     */
    public static String uploadFile(MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String  customSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(file.isEmpty()) {
            return "文件为空";
        }else if(file.getSize() > 1024*1024*10){
            return "文件大于10M";
        }
        //判断是否应该创建保存文件的目录
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        //生成新文件名(避免出现重复的文件名导致文件被覆盖)
        String fileName = UUID.randomUUID().toString().substring(0,16).replaceAll("-", "") + customSuffix;
        try {
            file.transferTo(new File(basePath + fileName));//保存文件
        }catch (IOException e){
            e.printStackTrace();
            return "上传出错";
        }
        return fileName;
    }
 
    /**
     * 下载文件
     * @param response
     * @param fileName 文件名
     * @param contentType 响应的HTTP内容类型
     * @return
     */
    public static String downloadFile(HttpServletResponse response,String fileName,String contentType){
        File dir = new File(basePath + fileName);
        //传输文件
        if(dir.exists()){
            try (FileInputStream fileInputStream = new FileInputStream(new File(basePath + fileName));
                 ServletOutputStream outputStream = response.getOutputStream()){
 
                response.setContentType(contentType);
                int len;
                byte[] bytes = new byte[1024];
                while((len = fileInputStream.read(bytes))!= -1){
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            return "上传成功";
        }else {
            return "文件不存在";
        }
    }
 
    /**
     * 删除文件
     * @param fileName 文件名
     * @return
     */
    public static String deleteFile(String fileName){
        File dir = new File(basePath + fileName);
        //isFile()判断是否是文件，exists()判断该文件是否存在，delete()删除成功返回1失败则返回0
        if(dir.isFile() && dir.exists() && dir.delete()) {
            return "删除成功";
        }else {
            return "文件不存在";
        }
    }
 
}