package operatelog.serializer.common;

import operatelog.constant.CommonConstant;
import operatelog.serializer.Serializer;

/**
 * 枚举序列化工具
 * @author : JiangCheng
 * @date : 2023/7/14 17:32
 */
public class EnumSerializer implements Serializer<Enum<?>> {
    @Override
    public String serialize(Enum<?> object) {
        if(object == null) {
            return CommonConstant.NULL;
        }

        return object.name();
    }
}
