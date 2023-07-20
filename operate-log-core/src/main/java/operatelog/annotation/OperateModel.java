package operatelog.annotation;

import java.lang.annotation.*;

/**
 * 操作日志对象
 *
 * @author : JiangCheng
 * @date : 2023/6/12 15:31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface OperateModel {
    /**
     * 日志字段名称列表
     *
     * @return 日志字段名称列表
     */
    String[] fields() default {};

    /**
     * 是否忽略未标记的字段
     *
     * @return 是否忽略未标记的字段
     */
    boolean ignoreUnmarkedField() default false;

    /**
     * 是否解析父类字段
     *
     * @return 是否解析父类字段
     */
    boolean retrieveSuperField() default true;
}
