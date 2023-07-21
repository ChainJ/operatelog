package operatelog.strategy;

import operatelog.model.FieldMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志字段元数据解析策略
 *
 * @author : JiangCheng
 * @date : 2023/7/20 17:42
 */
public interface FieldMetaStrategy {
    /**
     * 字段名-对象元数据配置缓存
     */
    ConcurrentHashMap<String, FieldMeta> FIELD_META_CACHE = new ConcurrentHashMap<>();

    /**
     * 构建日志对象元数据
     *
     * @param field 日志字段类型
     * @return 日志对象元数据
     */
    @Nullable
    FieldMeta buildMeta(@Nonnull Field field);

    /**
     * 尝试从缓存获取，如果缓存不存在，则构建新的对象元数据
     *
     * @param field 日志字段类型
     * @return 日志对象元数据
     */
    @Nullable
    default FieldMeta retrieveFromCache(@Nonnull Field field) {
        String fieldName = field.getDeclaringClass().getName() + "." + field.getName();
        FieldMeta cachedMeta = FIELD_META_CACHE.computeIfAbsent(fieldName, fName -> buildMeta(field));

        return cachedMeta == null ? null : cachedMeta.copy();
    }
}
