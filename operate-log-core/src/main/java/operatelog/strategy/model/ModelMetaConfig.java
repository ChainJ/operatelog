package operatelog.strategy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * 日志对象元数据配置
 *
 * @author : JiangCheng
 * @date : 2023/7/21 11:27
 */
@Getter
@Setter
public class ModelMetaConfig {
    /**
     * 所属类名
     */
    @JsonProperty("class")
    String className;

    /**
     * 目标字段
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
     * 日志字段元数据配置列表
     */
    List<FieldMetaConfig> fieldMeta = Collections.emptyList();
}
