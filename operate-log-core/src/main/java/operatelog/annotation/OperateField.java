package operatelog.annotation;

import operatelog.constant.DisplayPolicy;
import operatelog.constant.NamedEnum;
import operatelog.serializer.Serializer;
import operatelog.serializer.common.StringSerializer;

import java.lang.annotation.*;

/**
 * 操作日志字段
 *
 * @author : JiangCheng
 * @date : 2023/6/12 15:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OperateField {
    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String name() default "";

    /**
     * 字段别名
     *
     * @return 字段别名
     */
    String alias() default "";

    /**
     * 字段默认值
     *
     * @return 字段默认值
     */
    String defaultValue() default "";

    /**
     * 展示策略
     *
     * @return 展示策略
     */
    DisplayPolicy displayPolicy() default DisplayPolicy.STRING;

    /**
     * 关联枚举类型
     *
     * @return 关联枚举类型
     */
    Class<? extends NamedEnum> referEnumType() default NamedEnum.class;

    /**
     * 自定义的序列化类
     *
     * @return 自定义的序列化类
     */
    Class<? extends Serializer<Object>> serializer() default StringSerializer.class;

    /**
     * 是否忽略
     *
     * @return 是否忽略
     */
    boolean ignore() default false;
}
