package com.tdl.seckill.consumer.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/29 21:25
 */

@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext context;

    public static <T> T getBean(Class<T> c){
        return context.getBean(c);
    }


    public static <T> T getBean(String name,Class<T> clazz){
        return context.getBean(name,clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
