package com.tdl.seckill.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2019/3/3 22:01
 */

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisSingleNodeConfig {

    private String host;
    private Integer port;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
