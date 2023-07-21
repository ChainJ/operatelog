package operatelog.strategy;

import operatelog.model.ModelMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志对象元数据解析策略
 *
 * @author : JiangCheng
 * @date : 2023/7/20 17:42
 */
public interface ModelMetaStrategy {
    /**
     * 类名-对象元数据配置缓存
     */
    ConcurrentHashMap<String, ModelMeta> CLASS_META_CACHE = new ConcurrentHashMap<>();

    /**
     * 构建日志对象元数据
     *
     * @param clazz 日志对象类型
     * @return 日志对象元数据
     */
    @Nullable
    ModelMeta buildMeta(@Nonnull Class<?> clazz);

    /**
     * 尝试从缓存获取，如果缓存不存在，则构建新的对象元数据
     *
     * @param clazz 日志对象类型
     * @return 日志对象元数据
     */
    @Nullable
    default ModelMeta retrieveFromCache(@Nonnull Class<?> clazz) {
        ModelMeta cachedMeta = CLASS_META_CACHE.computeIfAbsent(clazz.getName(), className -> buildMeta(clazz));

        return cachedMeta == null ? null : cachedMeta.copy();
    }
}
