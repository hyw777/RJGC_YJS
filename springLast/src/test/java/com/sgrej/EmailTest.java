package com.sgrej;


import com.sgrej.utils.MailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class EmailTest {

    @Autowired
    private MailUtils mailUtils;

    @Test
    public void contextLoads(){
        /*邮件内容(随机生成验证码)*/
        String code = UUID.randomUUID().toString().substring(0,6);
        mailUtils.sendMail("2120096040@qq.com",code,"Your dad is coming");
    }
}
