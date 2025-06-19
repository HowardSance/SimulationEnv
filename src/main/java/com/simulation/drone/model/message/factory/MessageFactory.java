package com.simulation.drone.model.message.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulation.drone.model.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageFactory {
    private final ObjectMapper objectMapper;
    
    public MessageFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public <T extends BaseMessage> T createMessage(Class<T> messageClass) {
        try {
            return messageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("创建消息对象失败: {}", messageClass.getName(), e);
            throw new RuntimeException("创建消息对象失败: " + messageClass.getName(), e);
        }
    }
    
    public byte[] serialize(BaseMessage message) {
        try {
            return objectMapper.writeValueAsBytes(message);
        } catch (Exception e) {
            log.error("序列化消息失败", e);
            throw new RuntimeException("序列化消息失败", e);
        }
    }
    
    public <T extends BaseMessage> T deserialize(byte[] data, Class<T> messageClass) {
        try {
            return objectMapper.readValue(data, messageClass);
        } catch (Exception e) {
            log.error("反序列化消息失败", e);
            throw new RuntimeException("反序列化消息失败", e);
        }
    }
} 