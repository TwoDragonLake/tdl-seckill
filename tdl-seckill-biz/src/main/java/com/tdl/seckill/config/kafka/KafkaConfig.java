package com.tdl.seckill.config.kafka;

import com.tdl.seckill.api.dto.JsonSerializer;
import com.tdl.seckill.bo.SeckillGoodsBO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *kafka config
 */

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.brokerList}")
    public String brokerList ;

    @Value("${kafka.topic}")
    public String topic ;

    @Value("${kafka.swicth}")
    public boolean check ;

/*    @Bean
    public KafkaProducer<String, SeckillGoodsBO> build(){

        if (!check){
            return null ;
        }

        //初始化生产者
        Map<String, Object> props = new HashMap<String, Object>(16);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", brokerList);
        props.put("bootstrap.servers", brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        KafkaProducer<String, SeckillGoodsBO> producer = new KafkaProducer(props);
        return producer ;
    }*/

   private   Map<String, Object>  producerConfigs(){
       Map<String, Object> props = new HashMap<String, Object>(16);
       props.put("serializer.class", "kafka.serializer.StringEncoder");
       props.put("metadata.broker.list", brokerList);
       props.put("bootstrap.servers", brokerList);
       props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
       props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
       return  props;
   }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }
}
