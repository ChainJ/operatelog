package operatelog.strategy.impl;

import operatelog.annotation.OperateField;
import operatelog.constant.MetaSource;
import operatelog.model.FieldMeta;
import operatelog.strategy.FieldMetaStrategy;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 * 日志字段元数据注解解析策略
 *
 * @author : JiangCheng
 * @date : 2023/7/21 11:01
 */
public class FieldMetaAnnotationStrategy implements FieldMetaStrategy {
    @Override
    public FieldMeta buildMeta(@Nonnull Field field) {
        OperateField operateField = field.getAnnotation(OperateField.class);
        if (operateField == null) {
            return null;
        }

        FieldMeta fieldMeta = new FieldMeta();
        fieldMeta.setName(operateField.name());
        fieldMeta.setAlias(operateField.alias());
        fieldMeta.setDefaultValue(operateField.defaultValue());
        fieldMeta.setDisplayPolicy(operateField.displayPolicy());
        fieldMeta.setReferEnumType(operateField.referEnumType());
        fieldMeta.setSerializer(operateField.serializer());
        fieldMeta.setIgnore(operateField.ignore());
        fieldMeta.setSource(MetaSource.ANNOTATION);
        fieldMeta.setField(field);

        return fieldMeta;
    }
}
