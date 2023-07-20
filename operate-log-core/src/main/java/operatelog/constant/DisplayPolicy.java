package operatelog.constant;

/**
 * 日志字段的展示策略
 * @author : JiangCheng
 * @date : 2023/7/18 14:35
 */
public enum DisplayPolicy {
    /**
     * 原生字符串转化
     */
    STRING,
    /**
     * 转化为JSON
     */
    JSON,
    /**
     * 平铺展开字段
     */
    EXTRACT,
    /**
     * 枚举描述取值
     */
    NAMED_ENUM,
    /**
     * 自定义的
     */
    CUSTOMIZED,
    ;
}