package com.nextrt.acm.biz.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.user.User;
import com.nextrt.core.mapper.user.UserMapper;
import com.nextrt.core.vo.user.UserRankVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBiz {
    private final UserMapper userMapper;

    public UserBiz(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //用户注册
    public synchronized int userRegister(User user) {
        return userMapper.insert(user);
    }

    //检测邮箱和用户名是否被占用
    public boolean detectingUsers(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, user.getEmail()).or().eq(User::getUsername, user.getUsername());
        List<User> list = userMapper.selectList(queryWrapper);
        return list.isEmpty();
    }

    //匹配邮箱或者用户名是否包含该用户
    public User getUserByLogin(String email, String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username == null)
            queryWrapper.lambda().eq(User::getEmail, email);
        else
            queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    public User getUserByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(queryWrapper);
    }
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }
    public List<User> getUserByPrefix(String prefix) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().likeRight(User::getUsername, prefix);
        return userMapper.selectList(queryWrapper);
    }

    public int updateUserById(User user) {
        return userMapper.updateById(user);
    }

    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }

    public List<User> getAllUser() {
        return userMapper.selectList(new QueryWrapper<>());
    }

    public IPage<UserRankVO> getUserRankVO(int page, int size) {
        Page<UserRankVO> aPage = new Page<UserRankVO>(page, size);
        return userMapper.getUserRank(aPage);
    }

    public IPage<User> adminGetUserList(int page,int size,String query){
        Page<User> aPage = new Page<User>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!StrUtil.hasBlank(query))
            queryWrapper.lambda().or().like(User::getUsername,query).or().like(User::getNick,query).or().like(User::getEmail,query);
        return userMapper.selectPage(aPage,queryWrapper);
    }
    public int adminDeleteUser(int userId){
        return userMapper.deleteById(userId);
    }
}
