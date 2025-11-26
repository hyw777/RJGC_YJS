package com.sgrej.server;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgrej.domain.dto.MessageDTO;
import com.sgrej.domain.pojo.Message;
import com.sgrej.mapper.LastMessageMapper;
import com.sgrej.mapper.MessageMapper;
import com.sgrej.provider.ApplicationContextProvider;
import com.sgrej.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务
 */
@Service
@ServerEndpoint("/ws/{token}")
@Slf4j
public class WebSocketServer {

    @Autowired
    MessageMapper messageMapper = ApplicationContextProvider.getBean(MessageMapper.class);

    @Autowired
    LastMessageMapper lastMessageMapper=ApplicationContextProvider.getBean(LastMessageMapper.class);


    //存放会话对象
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        //解析token为用户id;
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //将用户id作为key将当前会话存入绘画记录中
        System.out.println("客户端：" + userid + "建立连接");
        sessionMap.put(userid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("token") String token) throws JsonProcessingException {

        //将Json格式的字符串反序列化成MessageDTO实体类
        ObjectMapper mapper = new ObjectMapper();
        MessageDTO messageDTO=mapper.readValue(message,MessageDTO.class);


        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

      log.info("客户端："+userid+"传来消息:"+message);
        //接受到消息后，首先将消息存入消息记录中，新消息是未读状态

        Message message1=new Message();
        BeanUtils.copyProperties(messageDTO,message1);
        message1.setTime(Timestamp.valueOf(LocalDateTime.now()));
        message1.setStatus(0);

        messageMapper.add(message1);


        //然后把每一次的消息作为最新消息更新到最近消息表中
        int result=lastMessageMapper.update(messageDTO);
        if(result == 0){
            lastMessageMapper.add(messageDTO);
        }



        //查看对方是否在线，在线的话发送这个消息给前端进行处理
        Session session = sessionMap.get(String.valueOf(messageDTO.getReceiveId()));

        if(session != null && session.isOpen()){

            System.out.println("发消息给客户端:"+messageDTO.getReceiveId());
            // 将MessageDTO转换为前端可以理解的格式，比如JSON
            String jsonMessage = mapper.writeValueAsString(message1);
            System.out.println(jsonMessage);
            // 发送消息
            sendToClient(jsonMessage,session);
        }

    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开:" + sid);
        sessionMap.remove(sid);
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToClient(String message,Session session) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

}
