package com.sgrej;

import com.sgrej.domain.pojo.User;

import com.sgrej.mapper.MenuMapper;
import com.sgrej.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {
 
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void testBCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      /*  String encode = passwordEncoder.encode("123456");
        String encode2=passwordEncoder.encode("123456");
        System.out.println(encode);
        System.out.println(encode2);*/

        System.out.println( passwordEncoder.matches("111111",
                "$2a$10$aSl8/sA9B6KANZNg6jQoCOExTp3JmNK39F8zm61VAAkxWhzvCHEMO"));
    }
 
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testSelectPermsByUserId(){
        System.out.println(menuMapper.selectPermsByUserId(1));
    }
}