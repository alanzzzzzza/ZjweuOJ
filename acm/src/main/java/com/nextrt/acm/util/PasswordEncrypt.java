package com.nextrt.acm.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nextrt.acm.config.SystemConfig;
import com.nextrt.core.entity.user.User;
import com.nextrt.core.vo.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncrypt {
    private final SystemConfig config;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public PasswordEncrypt(SystemConfig config) {
        this.config = config;
    }

    public String getGToken(User user) {
        Token result = new Token(user.getUserId(), user.getUsername(), user.getLevel(),user.getStatus());
        result.setExpire(System.currentTimeMillis() + 1000 * 60 * config.getInt("UserLoginStatusTime"));
        String key = getKey(result.getUserId() + '-' + result.getUsername() + '-' + result.getSalt() + '-' + result.getExpire()+'-'+result.getOther());
        result.setAuthorization(key);
        return Base64.encode(JSON.toJSONString(result));
    }

    public String examUserGetToken(int cuid,String username,int contestId){
        Token result = new Token(cuid,username, 0,contestId);
        result.setExpire(System.currentTimeMillis() + 1000 * 60 * config.getInt("UserLoginStatusTime"));
        String key = getKey(result.getUserId() + '-' + result.getUsername() + '-' + result.getSalt() + '-' + result.getExpire()+'-'+result.getOther());
        result.setAuthorization(key);
        return Base64.encode(JSON.toJSONString(result));
    }

    private String getKey(String str) {
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, config.getString("PasswordEncryptKey").getBytes());
        return mac.digestHex(str);
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password + config.getString("PasswordEncryptKey"));
    }

    public Boolean passwordEquals(String contrastPassword, String password) {
        return bCryptPasswordEncoder.matches(password + config.getString("PasswordEncryptKey"), contrastPassword);
    }

    public Token checkToken(String str) {
        try {
            if (!StringUtils.isEmpty(str)) {
                Token result = JSONObject.parseObject(Base64.decode(str), Token.class);
                String key = getKey(result.getUserId() + '-' + result.getUsername() + '-' + result.getSalt() + '-' + result.getExpire()+'-'+result.getOther());
                if (key.equals(result.getAuthorization()) && result.getExpire() >= System.currentTimeMillis())//当格式符合要求且有效期大于当前时间
                    return result;
            }
        }catch (NullPointerException e){
            return null;
        }
        return null;
    }
}
