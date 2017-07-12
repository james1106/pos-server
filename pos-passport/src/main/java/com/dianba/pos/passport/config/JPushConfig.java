package com.dianba.pos.passport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by zhangyong on 2017/7/12.
 */
@Configuration
@PropertySource("classpath:jpush.properties")
public class JPushConfig {
    @Value("${pos.jpush.secret}")
    private String posJPushSecret;

    @Value("${pos.jpush.key}")
    private String posJPushKey;

    public String getPosJPushSecret() {
        return posJPushSecret;
    }

    public void setPosJPushSecret(String posJPushSecret) {
        this.posJPushSecret = posJPushSecret;
    }

    public String getPosJPushKey() {
        return posJPushKey;
    }

    public void setPosJPushKey(String posJPushKey) {
        this.posJPushKey = posJPushKey;
    }
}
