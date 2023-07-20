package operatelog.model;

import com.sun.tools.javac.util.Assert;
import lombok.Data;
import operatelog.annotation.OperateModel;
import operatelog.constant.MetaSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
     * 根据目标类型生成日志对象元数据
     *
     * @param clazz 目标类型
     * @return 日志对象元数据
     */
    public static ModelMeta create(Class<?> clazz) {
        Assert.checkNonNull(clazz, "日志对象类型不能为空");

        ModelMeta modelMeta = create(clazz, MetaSource.CONFIGURATION);
        if (modelMeta == null) {
            modelMeta = create(clazz, MetaSource.DATA_BASE);
        }
        if (modelMeta == null) {
            modelMeta = create(clazz, MetaSource.ANNOTATION);
        }
        if (modelMeta == null) {
            modelMeta = new ModelMeta();
        }

        return modelMeta;
    }

    /**
     * 根据目标类型生成日志对象元数据
     *
     * @param clazz      目标类型
     * @param metaSource 元数据来源
     * @return 元数据对象
     */
    public static ModelMeta create(Class<?> clazz, MetaSource metaSource) {
        Assert.checkNonNull(clazz, "日志对象类型不能为空");
        Assert.checkNonNull(metaSource, "元数据来源不能为空");

        ModelMeta modelMeta = new ModelMeta();
        switch (metaSource) {
            case ANNOTATION:
                OperateModel operateModel = clazz.getAnnotation(OperateModel.class);
                if (operateModel == null) {
                    return null;
                }
                modelMeta.ignoreUnmarkedField = operateModel.ignoreUnmarkedField();
                modelMeta.retrieveSuperField = operateModel.retrieveSuperField();
                if (operateModel.fields().length > 0) {
                    modelMeta.targetFields = Arrays.stream(operateModel.fields()).collect(Collectors.toList());
                }
                break;
            default:
                // 不支持的元数据来源，默认不做处理
                return null;
        }
        modelMeta.source = metaSource;

        return modelMeta;
    }
}
