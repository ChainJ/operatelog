package operatelog.constant;

import java.util.Arrays;

/**
 * 带值的基础枚举
 *
 * @author : JiangCheng
 * @date : 2023/7/19 18:05
 */
public interface NamedEnum<T> {
    /**
     * 获取枚举描述
     *
     * @return 枚举值
     */
    String getName();

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    T getValue();

    /**
     * 根据枚举类型和枚举值，解析枚举实例
     *
     * @param enumClass 枚举类型
     * @param value     枚举值
     * @param <E>       枚举范型
     * @param <T>       值类型范型
     * @return 枚举实例
     */
    static <E extends Enum<E> & NamedEnum<T>, T> E fromValue(Class<E> enumClass, T value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
