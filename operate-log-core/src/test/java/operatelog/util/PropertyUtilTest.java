package operatelog.util;

import operatelog.constant.CommonConstant;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * 配置工具测试类
 *
 * @author : JiangCheng
 * @date : 2023/7/27 14:35
 */
public class PropertyUtilTest {
    @Test
    public void testGetProperty() {
        String key = "testKey";
        String defaultValue = "testDefaultValue";

        // 测试属性仅存在于配置文件，从配置文件中获取值
        String expectedValue = "testValueInConfigFile";
        String value = PropertyUtil.getOrDefault(key, defaultValue);
        Assertions.assertEquals(expectedValue, value);

        // 测试属性存在于应用程序属性，从程序属性中获取值
        expectedValue = "testValueInProps";
        System.setProperty(CommonConstant.CONFIG_PREFIX + key, expectedValue);
        value = PropertyUtil.getOrDefault(key, defaultValue);
        Assertions.assertEquals(expectedValue, value);

        // 测试属性不存在，返回默认值
        key = "testKeyNotExists";
        value = PropertyUtil.getOrDefault(key, defaultValue);
        Assertions.assertEquals(defaultValue, value);
    }
}
