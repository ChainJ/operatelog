package operatelog.model;

import com.sun.tools.javac.util.Assert;
import lombok.Data;
import operatelog.constant.MetaSource;
import operatelog.strategy.MetaStrategyFactory;
import operatelog.strategy.ModelMetaStrategy;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志对象元数据
 *
 * @author : JiangCheng
 * @date : 2023/7/18 15:17
 */
@Data
public class ModelMeta {
    /**
     * 目标字段列表
     */
    List<String> targetFields = Collections.emptyList();

    /**
     * 是否忽略未标记字段
     */
    boolean ignoreUnmarkedField = false;

    /**
     * 是否解析父类字段
     */
    boolean retrieveSuperField = true;

    /**
     * 元数据来源
     */
    MetaSource source;

    /**
     * 构建元数据的类型
     */
    Class<?> clazz;

    /**
     * 复制当前对象
     *
     * @return 对象的复制体
     */
    public ModelMeta copy() {
        ModelMeta copy = new ModelMeta();
        copy.targetFields = new ArrayList<>(targetFields);
        copy.ignoreUnmarkedField = ignoreUnmarkedField;
        copy.retrieveSuperField = retrieveSuperField;
        copy.source = source;
        copy.clazz = clazz;

        return copy;
    }

    /**
     * 根据目标类型生成日志对象元数据
     *
     * @param clazz 目标类型
     * @return 日志对象元数据
     */
    @Nonnull
    public static ModelMeta create(Class<?> clazz) {
        Assert.checkNonNull(clazz, "日志对象类型不能为空");

        ModelMeta modelMeta = null;
        List<MetaSource> orderedMetaSources = Arrays.stream(MetaSource.values())
                .sorted(Comparator.comparingInt(MetaSource::getOrder))
                .collect(Collectors.toList());
        for (MetaSource metaSource : orderedMetaSources) {
            ModelMetaStrategy strategy = MetaStrategyFactory.getModelMetaStrategy(metaSource);
            if (strategy == null) {
                continue;
            }

            modelMeta = strategy.retrieveFromCache(clazz);
            if (modelMeta != null) {
                break;
            }
        }
        if (modelMeta == null) {
            modelMeta = new ModelMeta();
            modelMeta.clazz = clazz;
        }

        return modelMeta;
    }
}
