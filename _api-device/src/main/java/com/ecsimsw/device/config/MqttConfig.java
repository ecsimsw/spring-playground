package com.ecsimsw.device.config;

import com.ecsimsw.device.controller.MqttSpmController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@RequiredArgsConstructor
@Configuration
public class MqttConfig {

    @Value("${mqtt.device.broker}")
    private String brokerEndpoint;

    private final MqttSpmController mqttSpmController;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        var adapter = new MqttPahoMessageDrivenChannelAdapter(
            brokerEndpoint,
            "ecsimsw",
            "stat/hejspm_12C724/RESULT"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            var topic = message.getHeaders()
                .get("mqtt_receivedTopic")
                .toString();
            var payload = message.getPayload()
                .toString();
            mqttSpmController.handle(topic, payload);
        };
    }
}