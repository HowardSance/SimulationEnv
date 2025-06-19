package com.simulation.drone.model.message;

import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseMessage implements MessagePackable {
    public void writeTo(Packer pk) throws IOException {
        Field[] fields = this.getClass().getDeclaredFields();
        pk.writeMapBegin(fields.length);
        
        for (Field f : fields) {
            String key = f.getName();
            pk.write(key);
            try {
                pk.write(f.get(this));
            } catch (Exception e) {
                log.error("序列化字段失败: {}", key, e);
            }
        }
        pk.writeMapEnd();
    }
    
    public void readFrom(Unpacker u) throws IOException {
        if (u.getNextType().equals(ValueType.MAP)) {
            u.readMapBegin();
            Map<String, Field> fields = new HashMap<>();
            for (Field f : this.getClass().getDeclaredFields()) {
                fields.put(f.getName(), f);
            }
            
            while (!u.trySkipNil() && u.getNextType() == ValueType.RAW) {
                String key = u.readString();
                Field field = fields.get(key);
                if (field != null) {
                    Object value = u.read(field.getType());
                    try {
                        field.set(this, value);
                    } catch (Exception e) {
                        log.error("反序列化字段失败: {}", key, e);
                    }
                }
            }
            u.readMapEnd();
        }
    }
} 