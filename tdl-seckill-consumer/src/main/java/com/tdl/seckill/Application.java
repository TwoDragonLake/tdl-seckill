package com.tdl.seckill;

import com.tdl.seckill.consumer.service.ConsumerGroup;
import com.tdl.seckill.consumer.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/27 20:24
 */

@SpringBootApplication
//@EnableAutoConfiguration
//@MapperScan(basePackages = "com.tdl.seckill.mapper")
public class Application {

    private static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).
                listeners(new ApplicationPidFileWriter())
                .web(false)
                .run(args);

        ConsumerGroup consumerGroup = SpringContextUtil.getBean(ConsumerGroup.class);
        consumerGroup.execute();

        LOGGER.info("启动成功");
    }
}
