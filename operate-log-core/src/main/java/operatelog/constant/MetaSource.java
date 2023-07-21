package operatelog.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 元数据来源
 *
 * @author : JiangCheng
 * @date : 2023/7/18 16:57
 */
@Getter
@AllArgsConstructor
public enum MetaSource {
    /**
     * 配置文件
     */
    CONFIGURATION(100),
    /**
     * 数据库
     */
    DATA_BASE(200),
    /**
     * 注解
     */
    ANNOTATION(300),
    ;

    /**
     * 检索顺序
     * 按照检索顺序从小到大检索数据来源
     */
    final int order;
}
