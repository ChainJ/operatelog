package operatelog.strategy.model;

import lombok.Getter;
import lombok.Setter;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.NamedEnum;
import operatelog.serializer.common.StringSerializer;

/**
 * 日志字段元数据配置
 *
 * @author : JiangCheng
 * @date : 2023/7/21 11:26
 */
@Getter
@Setter
public class FieldMetaConfig {
    /**
     * 所属元数据对象的字段
     */
    String field;

    /**
     * 字段名称
     */
    String name = "";

    /**
     * 字段别名
     */
    String alias = "";

    /**
     * 字段默认值
     */
    String defaultValue = "";

    /**
     * 展示策略
     */
    String displayPolicy = DisplayPolicy.STRING.name();

    /**
     * 关联枚举类型
     */
    String referEnumType = NamedEnum.class.getName();

    /**
     * 自定义的序列化类
     */
    String serializer = StringSerializer.class.getName();

    /**
     * 是否忽略
     */
    boolean ignore = false;
}
