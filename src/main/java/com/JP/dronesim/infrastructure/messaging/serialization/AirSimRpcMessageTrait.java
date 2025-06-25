package com.JP.dronesim.infrastructure.messaging.serialization;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;

/**
 * AirSim RPC消息序列化特征类
 * 实现MessagePack序列化/反序列化逻辑
 */
public class AirSimRpcMessageTrait implements MessagePackable {
    
    /**
     * 将对象序列化为MessagePack格式
     * @param pk MessagePack打包器
     * @throws IOException 序列化异常
     */
    @Override
    public void writeTo(Packer pk) throws IOException {
        Field[] fields = this.getClass().getDeclaredFields();
        pk.writeMapBegin(fields.length);
        
        for (Field field : fields) {
            String key = field.getName();
            pk.write(key);
            
            try {
                // 设置字段可访问
                field.setAccessible(true);
                Object value = field.get(this);
                pk.write(value);
                
            } catch (IllegalArgumentException e) {
                System.err.println("序列化字段时参数错误: " + key + " - " + e.getMessage());
                pk.write(null);
            } catch (IllegalAccessException e) {
                System.err.println("序列化字段时访问错误: " + key + " - " + e.getMessage());
                pk.write(null);
            } catch (IOException e) {
                System.err.println("序列化字段时IO错误: " + key + " - " + e.getMessage());
                throw e;
            }
        }
        pk.writeMapEnd();
    }
    
    /**
     * 从MessagePack格式反序列化对象
     * @param unpacker MessagePack解包器
     * @throws IOException 反序列化异常
     */
    @Override
    public void readFrom(Unpacker unpacker) throws IOException {
        if (!unpacker.getNextType().equals(ValueType.MAP)) {
            throw new IOException("期望MAP类型，但得到: " + unpacker.getNextType());
        }
        
        int mapSize = unpacker.readMapBegin();
        
        // 构建字段映射表
        HashMap<String, Field> fieldMap = new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        
        // 读取每个键值对
        for (int i = 0; i < mapSize; i++) {
            if (unpacker.trySkipNil()) {
                continue;
            }
            
            if (unpacker.getNextType() != ValueType.RAW) {
                unpacker.skip();
                continue;
            }
            
            String key = unpacker.readString();
            Field field = fieldMap.get(key);
            
            if (field == null) {
                System.err.println("未找到字段: " + key + "，跳过该值");
                unpacker.skip();
                continue;
            }
            
            try {
                Object value = readFieldValue(unpacker, field.getType());
                field.set(this, value);
                
            } catch (IllegalArgumentException e) {
                System.err.println("反序列化字段时参数错误: " + key + " - " + e.getMessage());
                unpacker.skip();
            } catch (IllegalAccessException e) {
                System.err.println("反序列化字段时访问错误: " + key + " - " + e.getMessage());
                unpacker.skip();
            } catch (Exception e) {
                System.err.println("反序列化字段时未知错误: " + key + " - " + e.getMessage());
                unpacker.skip();
            }
        }
        
        unpacker.readMapEnd();
    }
    
    /**
     * 根据字段类型读取值
     * @param unpacker 解包器
     * @param fieldType 字段类型
     * @return 读取的值
     * @throws IOException 读取异常
     */
    private Object readFieldValue(Unpacker unpacker, Class<?> fieldType) throws IOException {
        if (unpacker.trySkipNil()) {
            return null;
        }
        
        ValueType valueType = unpacker.getNextType();
        
        // 处理基本类型
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return unpacker.readBoolean();
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return unpacker.readByte();
        } else if (fieldType == short.class || fieldType == Short.class) {
            return unpacker.readShort();
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return unpacker.readInt();
        } else if (fieldType == long.class || fieldType == Long.class) {
            return unpacker.readLong();
        } else if (fieldType == float.class || fieldType == Float.class) {
            return unpacker.readFloat();
        } else if (fieldType == double.class || fieldType == Double.class) {
            return unpacker.readDouble();
        } else if (fieldType == String.class) {
            return unpacker.readString();
        } else if (fieldType.isArray()) {
            return readArrayValue(unpacker, fieldType.getComponentType());
        } else if (MessagePackable.class.isAssignableFrom(fieldType)) {
            // 处理嵌套的MessagePackable对象
            try {
                MessagePackable nestedObject = (MessagePackable) fieldType.newInstance();
                nestedObject.readFrom(unpacker);
                return nestedObject;
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("创建嵌套对象失败: " + fieldType.getName());
                unpacker.skip();
                return null;
            }
        } else {
            // 对于其他类型，尝试通用读取
            return unpacker.read(fieldType);
        }
    }
    
    /**
     * 读取数组值
     * @param unpacker 解包器
     * @param componentType 数组元素类型
     * @return 数组对象
     * @throws IOException 读取异常
     */
    private Object readArrayValue(Unpacker unpacker, Class<?> componentType) throws IOException {
        if (unpacker.getNextType() != ValueType.ARRAY) {
            unpacker.skip();
            return null;
        }
        
        int arraySize = unpacker.readArrayBegin();
        
        if (componentType == float.class) {
            float[] array = new float[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = unpacker.readFloat();
            }
            unpacker.readArrayEnd();
            return array;
        } else if (componentType == int.class) {
            int[] array = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = unpacker.readInt();
            }
            unpacker.readArrayEnd();
            return array;
        } else if (componentType == double.class) {
            double[] array = new double[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = unpacker.readDouble();
            }
            unpacker.readArrayEnd();
            return array;
        } else {
            // 对于其他类型的数组，跳过
            for (int i = 0; i < arraySize; i++) {
                unpacker.skip();
            }
            unpacker.readArrayEnd();
            return null;
        }
    }
    
    /**
     * 获取对象的字符串表示
     * @return 字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("{");
        
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            
            try {
                Object value = field.get(this);
                sb.append(field.getName()).append("=").append(value);
                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=<访问错误>");
                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
} 