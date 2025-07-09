package com.ecsimsw.event.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttConfig {

    public static final String MQTT_INPUT_CHANNEL_NAME = "mqttInputChannel";

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound(
        @Value("${mqtt.broker.endpoint}")
        String mqttUrl,
        @Value("${mqtt.broker.port}")
        int mqttPort,
        @Value("${mqtt.broker.clientId}")
        String clientId
    ) {
        var adapter = new MqttPahoMessageDrivenChannelAdapter("tcp://" + mqttUrl + ":" + mqttPort, clientId, "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }
}