package operatelog.strategy.impl;

import operatelog.annotation.OperateModel;
import operatelog.constant.MetaSource;
import operatelog.model.ModelMeta;
import operatelog.strategy.ModelMetaStrategy;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 日志对象注解构建策略
 *
 * @author : JiangCheng
 * @date : 2023/7/20 17:45
 */
public class ModelMetaAnnotationStrategy implements ModelMetaStrategy {
    @Override
    public ModelMeta buildMeta(@Nonnull Class<?> clazz) {
        OperateModel operateModel = clazz.getAnnotation(OperateModel.class);
        if (operateModel == null) {
            return null;
        }

        ModelMeta modelMeta = new ModelMeta();
        modelMeta.setIgnoreUnmarkedField(operateModel.ignoreUnmarkedField());
        modelMeta.setRetrieveSuperField(operateModel.retrieveSuperField());
        if (operateModel.fields().length > 0) {
            modelMeta.setTargetFields(Arrays.stream(operateModel.fields()).collect(Collectors.toList()));
        }
        modelMeta.setSource(MetaSource.ANNOTATION);
        modelMeta.setClazz(clazz);

        return modelMeta;
    }
}
