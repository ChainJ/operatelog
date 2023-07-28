package operatelog.constant;

import operatelog.util.PropertyUtil;

/**
 * 通用常量
 *
 * @author : JiangCheng
 * @date : 2023/7/17 14:11
 */
public interface CommonConstant {
    /**
     * 配置前缀
     */
    String CONFIG_PREFIX = "operate_log.";
    /**
     * 配置文件路径
     */
    String CONFIG_FILE_PATH = PropertyUtil.getOrDefault("config.file.path", "/operate-log.properties", true);
    /**
     * 空值描述
     */
    String NULL = PropertyUtil.getOrDefault("null.description", "空");
    /**
     * 元数据配置文件目录
     */
    String META_CONFIG_DIR = PropertyUtil.getOrDefault("meta.config.dir", "/operate-meta");
}
