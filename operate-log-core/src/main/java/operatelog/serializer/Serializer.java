package operatelog.serializer;

/**
 * 日志字段序列化工具
 *
 * @author : JiangCheng
 * @date : 2023/7/18 14:37
 */
public interface Serializer<T> {
    /**
     * 序列化当前对象
     *
     * @param object 目标对象
     * @return 序列化结果
     */
    String serialize(T object);
}
