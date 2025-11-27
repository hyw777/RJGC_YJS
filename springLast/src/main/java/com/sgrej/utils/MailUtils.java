package com.sgrej.utils;

import jakarta.mail.Authenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发邮件工具类
 */
@Component
public final class MailUtils {
    @Value("${mail.user}")
    private String USER; // 发件人邮箱地址
    @Value("${mail.password}")
    private String PASSWORD; // 如果是qq邮箱可以使户端授权码

    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     */
    public boolean sendMail(String to, String text, String title){
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.qq.com");
            // 关键：启用SSL加密
            props.put("mail.smtp.ssl.enable", "true");
            // SSL端口（QQ邮箱固定为465）
            props.put("mail.smtp.port", "465");
            // 可选：设置SSLSocketFactory类（避免部分环境兼容问题）
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.fallback", "false");

            // 发件人的账号和密码（保持不变）
            props.put("mail.user", USER);
            props.put("mail.password", PASSWORD);

            // 构建授权信息（保持不变）
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };

            // 创建邮件会话（保持不变）
            Session mailSession = Session.getInstance(props, authenticator);
            mailSession.setDebug(true); // 可选：开启调试模式，打印SMTP交互日志
            MimeMessage message = new MimeMessage(mailSession);

            // 设置发件人、收件人、标题、内容（保持不变）
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(title);
            message.setContent(text, "text/html;charset=UTF-8");

            // 发送邮件（保持不变）
            Transport.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

