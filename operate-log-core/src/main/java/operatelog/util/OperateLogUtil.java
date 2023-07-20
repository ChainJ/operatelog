package operatelog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import operatelog.constant.CommonConstant;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.NamedEnum;
import operatelog.model.FieldMeta;
import operatelog.model.ModelMeta;
import operatelog.serializer.Serializer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 操作日志工具类
 *
 * @author : JiangCheng
 * @date : 2023/6/12 16:24
 */
@Slf4j
public abstract class OperateLogUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取指定类型对象的字段差异
     *
     * @param source 原始对象
     * @param target 目标对象
     * @param <T>    指定类型范型
     * @return 字段差异列表
     */
    public static <T> Map<String, Pair<String, String>> diff(T source, T target) {
        if (source == null && target == null) {
            return Collections.emptyMap();
        }

        Class<T> clazz = target == null ? (Class<T>) source.getClass() : (Class<T>) target.getClass();
        return diff(source, target, clazz);
    }

    /**
     * 获取指定类型对象的字段差异
     *
     * @param source 原始对象
     * @param target 目标对象
     * @param clazz  对象类型
     * @param <T>    指定类型范型
     * @return 字段差异列表
     */
    public static <T> Map<String, Pair<String, String>> diff(T source, T target, Class<T> clazz) {
        if (source == target || clazz == null) {
            return Collections.emptyMap();
        }

        ModelMeta modelMeta = ModelMeta.create(clazz);
        boolean retrieveSuperField = modelMeta.isRetrieveSuperField();
        boolean ignoreUnmarked = modelMeta.isIgnoreUnmarkedField();

        Map<String, Field> fieldNameMap = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toMap(
                        Field::getName,
                        Function.identity(),
                        (o1, o2) -> o1,
                        LinkedHashMap::new
                ));
        List<String> targetFieldNames = ObjectUtils.isEmpty(modelMeta.getTargetFields()) ?
                new ArrayList<>(fieldNameMap.keySet()) :
                new ArrayList<>(modelMeta.getTargetFields());

        Class<?> superClass = clazz.getSuperclass();
        // 遍历所有父类，并解析父类的字段
        while (retrieveSuperField && !superClass.isAssignableFrom(Object.class)) {
            // 解析父类的字段，如果字段名称冲突，则只保留子类的字段
            Map<String, Field> superFieldNameMap = Arrays.stream(superClass.getDeclaredFields())
                    .collect(Collectors.toMap(
                            Field::getName,
                            Function.identity(),
                            (o1, o2) -> o1,
                            LinkedHashMap::new
                    ));
            superFieldNameMap.forEach(fieldNameMap::putIfAbsent);
            ModelMeta superModelMeta = ModelMeta.create(superClass);
            if (ObjectUtils.isEmpty(superModelMeta.getTargetFields())) {
                targetFieldNames.addAll(superFieldNameMap.keySet());
            } else {
                targetFieldNames.addAll(superModelMeta.getTargetFields());
            }

            superClass = superClass.getSuperclass();
        }

        Map<String, Pair<String, String>> fieldValueChangeMap = new LinkedHashMap<>();
        for (String fieldName : targetFieldNames) {
            Field field = fieldNameMap.get(fieldName);
            if (field == null) {
                continue;
            }

            FieldMeta fieldMeta = FieldMeta.create(field);
            if (ignoreUnmarked && fieldMeta.getSource() == null) {
                continue;
            }
            if (fieldMeta.isIgnore()) {
                continue;
            }

            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = BeanUtil.retrieveMethod(clazz, getterName);
            if (getter == null) {
                continue;
            }
            if (!getter.isAccessible()) {
                getter.setAccessible(true);
            }

            try {
                Object v1 = source == null ? null : getter.invoke(source);
                Object v2 = target == null ? null : getter.invoke(target);
                if (Objects.equals(v1, v2)) {
                    continue;
                }

                fieldName = fieldMeta.getAlias();
                if (StringUtils.isBlank(fieldName)) {
                    fieldName = fieldMeta.getName();
                }
                if (StringUtils.isBlank(fieldName)) {
                    fieldName = field.getName();
                }
                String desc1 = null, desc2 = null;
                DisplayPolicy displayPolicy = fieldMeta.getDisplayPolicy();
                switch (displayPolicy) {
                    case STRING:
                        desc1 = v1 == null ? null : v1.toString();
                        desc2 = v2 == null ? null : v2.toString();
                        break;
                    case JSON:
                        desc1 = v1 == null ? null : mapper.writeValueAsString(v1);
                        desc2 = v2 == null ? null : mapper.writeValueAsString(v2);
                        break;
                    case NAMED_ENUM:
                        NamedEnum<?>[] enums = fieldMeta.getReferEnumType().getEnumConstants();
                        desc1 = Arrays.stream(enums)
                                .filter(e -> e.getValue().equals(v1))
                                .findFirst()
                                .map(NamedEnum::getName)
                                .orElse(null);
                        desc2 = Arrays.stream(enums)
                                .filter(e -> e.getValue().equals(v2))
                                .findFirst()
                                .map(NamedEnum::getName)
                                .orElse(null);
                        break;
                    case EXTRACT:
                        // 将对象字段平铺展开，成为 field1.childField1 = xxx 的形式
                        // TODO：处理范型类型
                        Class<Object> fieldType = (Class<Object>) field.getType();
                        Map<String, Pair<String, String>> valueChangeMap = diff(v1, v2, fieldType);
                        String fName = fieldName;
                        valueChangeMap.forEach((childFieldName, valueChange) ->
                                fieldValueChangeMap.put(fName + "." + childFieldName, valueChange)
                        );
                        continue;
                    case CUSTOMIZED:
                        Serializer<Object> serializer = fieldMeta.getSerializer().newInstance();
                        desc1 = serializer.serialize(v1);
                        desc2 = serializer.serialize(v2);
                        break;
                    default:
                        break;
                }
                if (desc1 == null) {
                    desc1 = CommonConstant.NULL;
                }
                if (desc2 == null) {
                    desc2 = CommonConstant.NULL;
                }

                fieldValueChangeMap.put(fieldName, Pair.of(desc1, desc2));
            } catch (Exception e) {
                log.error("解析操作日志字段值失败！field:{}, source:{}, target:{}", field.getName(), source, target);
            }
        }

        return fieldValueChangeMap;
    }
}
