package operatelog.strategy;

import lombok.NonNull;
import operatelog.constant.MetaSource;
import operatelog.strategy.impl.FieldMetaAnnotationStrategy;
import operatelog.strategy.impl.FieldMetaConfigStrategy;
import operatelog.strategy.impl.ModelMetaAnnotationStrategy;
import operatelog.strategy.impl.ModelMetaConfigStrategy;

/**
 * 元数据策略工厂
 *
 * @author : JiangCheng
 * @date : 2023/7/21 11:50
 */
public class MetaStrategyFactory {
    /**
     * 获取日志对象元数据策略
     *
     * @param metaSource 元数据来源
     * @return 日志对象元数据策略
     */
    public static ModelMetaStrategy getModelMetaStrategy(@NonNull MetaSource metaSource) {
        switch (metaSource) {
            case ANNOTATION:
                return new ModelMetaAnnotationStrategy();
            case CONFIGURATION:
                return new ModelMetaConfigStrategy();
            default:
                return null;
        }
    }

    /**
     * 获取日志字段元数据策略
     *
     * @param metaSource 元数据来源
     * @return 日志字段元数据策略
     */
    public static FieldMetaStrategy getFieldMetaStrategy(@NonNull MetaSource metaSource) {
        switch (metaSource) {
            case ANNOTATION:
                return new FieldMetaAnnotationStrategy();
            case CONFIGURATION:
                return new FieldMetaConfigStrategy();
            default:
                return null;
        }
    }
}
