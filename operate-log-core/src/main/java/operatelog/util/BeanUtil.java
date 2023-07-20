package operatelog.util;

import java.lang.reflect.Method;

/**
 * @author : JiangCheng
 * @date : 2023/7/17 15:28
 */
public abstract class BeanUtil {
    /**
     * 解析类型中的方法，包括类本身和父类的方法
     *
     * @param clazz      指定类型
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法对象
     */
    public static Method retrieveMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return retrieveDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    /**
     * 解析类型中声明的方法，包括类本身和父类的方法
     *
     * @param clazz      指定类型
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法对象
     */
    public static Method retrieveDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            if (clazz.getSuperclass() != null) {
                return retrieveDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
            }
            return null;
        }
    }
}
