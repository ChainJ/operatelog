package operatelog.serializer.common;

import operatelog.serializer.Serializer;

import java.util.Collection;

/**
 * 通用数组序列化工具
 *
 * @author : JiangCheng
 * @date : 2023/6/21 16:33
 */
public class ArraySerializer implements Serializer<Object> {
    @Override
    public String serialize(Object object) {
        if (object == null) {
            return null;
        }
        // TODO: 实现数组序列化逻辑
        if (object.getClass().isArray()) {
            throw new UnsupportedOperationException("暂不支持");
        }
        if (object.getClass().isAssignableFrom(Collection.class)) {
            throw new UnsupportedOperationException("暂不支持");
        }
        return object.toString();
    }
}
