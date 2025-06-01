package com.ecsimsw.device.config;

import com.ecsimsw.device.service.MqttBetaHandlerService;
import lombok.RequiredArgsConstructor;
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

    private static final String MQTT_BROKER = "tcp://hejdev1.goqual.com:1883";
    private static final String CLIENT_ID = "ecsimsw";
    private static final String TOPIC = "stat/hejspm_12C724/RESULT";

    private final MqttBetaHandlerService mqttBetaHandlerService;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        var adapter = new MqttPahoMessageDrivenChannelAdapter(
            MQTT_BROKER,
            CLIENT_ID,
            TOPIC
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
            var payload = message.getPayload().toString();
            mqttBetaHandlerService.handle(topic, payload);
        };
    }
}