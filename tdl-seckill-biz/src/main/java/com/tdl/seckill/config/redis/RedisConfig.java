package com.tdl.seckill.config.redis;

import com.tdl.redis.access.RedisLimit;
import com.tdl.redis.constants.RedisToolsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.lang.reflect.Method;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/7 15:48
 */
@Configuration
@EnableCaching
class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    ClusterConfigurationProperties clusterConfigurationProperties;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Value("${redis.limit}")
    private int limit;

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append("_").append(method.getName());
                for (Object obj : params) {
                    sb.append("_").append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间        //rcm.setDefaultExpiration(60);//秒
        return rcm;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(new RedisClusterConfiguration(clusterConfigurationProperties.getNodes()));
/*        factory.setHostName(clusterConfigurationProperties.getHost());
        factory.setPort(clusterConfigurationProperties.getPort());*/
        factory.setPassword(clusterConfigurationProperties.getPassword());
        factory.setTimeout(clusterConfigurationProperties.getTimeout());
        //设置连接超时时间
        return factory;
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
/*        @SuppressWarnings({"rawtypes", "unchecked"}) JdkSerializationRedisSerializer jackson2JsonRedisSerializer = new JdkSerializationRedisSerializer(Object.class.getClassLoader());
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        /*JedisConnectionFactory jc = (JedisConnectionFactory) factory;
        System.out.println(jc.getHostName());*/
        return template;
    }

    @Bean
    public RedisLimit build() {
        RedisLimit redisLimit = new RedisLimit.Builder(jedisConnectionFactory, RedisToolsConstant.SINGLE)
                .limit(limit)
                .build();

        return redisLimit;
    }
}

