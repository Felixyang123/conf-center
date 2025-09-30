package com.wly.config.server.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wly.config.server.dao.entity.User;
import com.wly.config.server.dao.rep.UserRepository;
import com.wly.config.server.utils.DigestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User checkLogin(String username, String password) {
        User user = userRepository.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!Objects.equals(user.getPassword(), DigestUtils.SHA256(password + user.getSalt()))) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    public void register(String username, String pwd) {
        String salt = DigestUtils.md5(username + System.currentTimeMillis());

        String encryptPwd = DigestUtils.SHA256(pwd + salt);

        Date now = new Date();
        User user = User.builder().username(username).password(encryptPwd).salt(salt).status(0).createTime(now).updateTime(now).build();
        try {
            userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("用户已存在");
        } catch (Exception e) {
            throw new RuntimeException("注册失败");
        }
    }
}
