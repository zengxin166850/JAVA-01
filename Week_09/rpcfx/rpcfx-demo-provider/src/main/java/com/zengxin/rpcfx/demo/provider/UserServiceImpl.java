package com.zengxin.rpcfx.demo.provider;

import com.zengxin.rpcfx.annotation.RPCService;
import com.zengxin.rpcfx.demo.api.User;
import com.zengxin.rpcfx.demo.api.UserService;

@RPCService
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "user" + System.currentTimeMillis());
    }
}
