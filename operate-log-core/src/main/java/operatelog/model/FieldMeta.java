package operatelog.model;

import com.sun.tools.javac.util.Assert;
import lombok.Data;
import operatelog.annotation.OperateField;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.MetaSource;
import operatelog.constant.NamedEnum;
import operatelog.serializer.Serializer;
import operatelog.serializer.common.StringSerializer;

import java.lang.reflect.Field;

/**
 * 日志字段元数据
 *
 * @author : JiangCheng
 * @date : 2023/7/18 15:17
 */
@Data
public class FieldMeta {
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
    DisplayPolicy displayPolicy = DisplayPolicy.STRING;

    /**
     * 关联枚举类型
     */
    Class<? extends NamedEnum> referEnumType = NamedEnum.class;

    /**
     * 自定义的序列化类
     */
    Class<? extends Serializer<Object>> serializer = StringSerializer.class;

    /**
     * 是否忽略
     */
    boolean ignore = false;

    /**
     * 元数据来源
     */
    MetaSource source;

    /**
     * 根据目标类型生成日志字段元数据
     *
     * @param field 目标字段
     * @return 日志字段元数据
     */
    public static FieldMeta create(Field field) {
        Assert.checkNonNull(field, "日志对象字段不能为空");

        FieldMeta fieldMeta = create(field, MetaSource.CONFIGURATION);
        if (fieldMeta == null) {
            fieldMeta = create(field, MetaSource.DATA_BASE);
        }
        if (fieldMeta == null) {
            fieldMeta = create(field, MetaSource.ANNOTATION);
        }
        if (fieldMeta == null) {
            fieldMeta = new FieldMeta();
        }

        return fieldMeta;
    }

    /**
     * 根据目标类型生成日志字段元数据
     *
     * @param field      目标字段
     * @param metaSource 元数据来源
     * @return 日志字段元数据
     */
    public static FieldMeta create(Field field, MetaSource metaSource) {
        Assert.checkNonNull(field, "日志对象字段不能为空");
        Assert.checkNonNull(metaSource, "元数据来源不能为空");

        FieldMeta fieldMeta = new FieldMeta();
        switch (metaSource) {
            case ANNOTATION:
                OperateField operateField = field.getAnnotation(OperateField.class);
                if (operateField == null) {
                    return null;
                }
                fieldMeta.name = operateField.name();
                fieldMeta.alias = operateField.alias();
                fieldMeta.defaultValue = operateField.defaultValue();
                fieldMeta.displayPolicy = operateField.displayPolicy();
                fieldMeta.referEnumType = operateField.referEnumType();
                fieldMeta.serializer = operateField.serializer();
                fieldMeta.ignore = operateField.ignore();
                break;
            default:
                // 不支持的元数据来源，默认不做处理
                return null;
        }
        fieldMeta.source = metaSource;

        return fieldMeta;
    }
}
