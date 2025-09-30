package com.wly.config.server.dao.rep;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wly.config.server.dao.entity.User;
import com.wly.config.server.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRepository extends ServiceImpl<UserMapper, User> {
}
