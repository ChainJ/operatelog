package operatelog.model;

import lombok.Data;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.MetaSource;
import operatelog.constant.NamedEnum;
import operatelog.serializer.Serializer;
import operatelog.serializer.common.StringSerializer;
import operatelog.strategy.FieldMetaStrategy;
import operatelog.strategy.MetaStrategyFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
     * 构建元数据的字段
     */
    Field field;

    /**
     * 复制当前对象
     *
     * @return 对象的复制体
     */
    public FieldMeta copy() {
        FieldMeta copy = new FieldMeta();
        copy.name = name;
        copy.alias = alias;
        copy.defaultValue = defaultValue;
        copy.displayPolicy = displayPolicy;
        copy.referEnumType = referEnumType;
        copy.serializer = serializer;
        copy.ignore = ignore;
        copy.source = source;
        copy.field = field;

        return copy;
    }

    /**
     * 根据目标类型生成日志字段元数据
     *
     * @param field 目标字段
     * @return 日志字段元数据
     */
    @Nonnull
    public static FieldMeta create(@Nonnull Field field) {
        FieldMeta fieldMeta = null;
        List<MetaSource> orderedMetaSources = Arrays.stream(MetaSource.values())
                .sorted(Comparator.comparingInt(MetaSource::getOrder))
                .collect(Collectors.toList());
        for (MetaSource metaSource : orderedMetaSources) {
            FieldMetaStrategy strategy = MetaStrategyFactory.getFieldMetaStrategy(metaSource);
            if (strategy == null) {
                continue;
            }

            fieldMeta = strategy.retrieveFromCache(field);
            if (fieldMeta != null) {
                break;
            }
        }

        if (fieldMeta == null) {
            fieldMeta = new FieldMeta();
            fieldMeta.field = field;
        }

        return fieldMeta;
    }

}
