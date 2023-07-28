package operatelog.util;

import lombok.extern.slf4j.Slf4j;
import operatelog.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 配置工具
 *
 * @author : JiangCheng
 * @date : 2023/7/26 18:13
 */
@Slf4j
public abstract class PropertyUtil {
    /**
     * 从环境变量、程序属性或者配置文件中，读取一个配置
     *
     * @param key          配置名称
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getOrDefault(String key, String defaultValue) {
        return getOrDefault(key, defaultValue, false);
    }

    /**
     * 从环境变量、程序属性或者配置文件中，读取一个配置
     *
     * @param key          配置名称
     * @param defaultValue 默认值
     * @param envOnly      是否仅限从环境变量中读取
     * @return 配置值
     */
    public static String getOrDefault(String key, String defaultValue, boolean envOnly) {
        if (StringUtils.isBlank(key)) {
            return "";
        }

        key = CommonConstant.CONFIG_PREFIX + key;
        String value = System.getenv(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        value = System.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        if (envOnly) {
            return defaultValue;
        }

        Properties props = new Properties();
        URL url = PropertyUtil.class.getResource(CommonConstant.CONFIG_FILE_PATH);
        if(url == null) {
            log.warn("读取配置文件失败! path:{}", CommonConstant.CONFIG_FILE_PATH);
            return defaultValue;
        }
        try (InputStream fis = Files.newInputStream(Paths.get(url.getPath()))) {
            props.load(fis);
        } catch (Exception e) {
            log.error("读取配置文件失败! error:{}", e.getMessage(), e);
        }

        value = props.getProperty(key, defaultValue);
        return value;
    }
}
