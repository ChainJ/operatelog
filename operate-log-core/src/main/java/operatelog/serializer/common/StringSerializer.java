package operatelog.serializer.common;

import operatelog.serializer.Serializer;

/**
 * 将对象序列化为字符串
 * @author : JiangCheng
 * @date : 2023/7/18 14:38
 */
public class StringSerializer implements Serializer<Object> {

    @Override
    public String serialize(Object object) {
        if (object == null) {
            return null;
        }

        return object.toString();
    }
}
