package com.nextrt.core.vo;

import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private int userId;
    private String username;
    private int userLevel;
    private String organization;
    private long expire;
    private String salt;
    private String authorization;
    private Integer other;

    public Token(int userId, String username,int userLevel) {
        this.userId = userId;
        this.username = username;
        this.userLevel = userLevel;
        this.salt = RandomUtil.randomString(32);
    }

    public Token(int userId, String username,int userLevel,Integer other) {
        this.userId = userId;
        this.username = username;
        this.userLevel = userLevel;
        this.salt = RandomUtil.randomString(32);
        this.other  = other;
    }
}
