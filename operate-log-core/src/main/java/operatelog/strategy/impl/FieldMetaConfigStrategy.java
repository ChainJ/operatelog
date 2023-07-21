package operatelog.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import operatelog.constant.CommonConstant;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.MetaSource;
import operatelog.constant.NamedEnum;
import operatelog.model.FieldMeta;
import operatelog.serializer.Serializer;
import operatelog.strategy.FieldMetaStrategy;
import operatelog.strategy.model.FieldMetaConfig;
import operatelog.strategy.model.ModelMetaConfig;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Objects;

/**
 * 日志字段元数据配置构建策略
 * @author : JiangCheng
 * @date : 2023/7/21 11:25
 */
@Slf4j
public class FieldMetaConfigStrategy implements FieldMetaStrategy {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    /**
     * YML文件后缀
     */
    private static final String FILE_SUFFIX_YML = ".yml";

    /**
     * JSON文件后缀
     */
    private static final String FILE_SUFFIX_JSON = ".json";

    @Override
    public FieldMeta buildMeta(@Nonnull Field field) {
        URL baseUrl = field.getDeclaringClass().getResource(CommonConstant.META_CONFIG_DIR);
        if (baseUrl == null) {
            log.warn("元数据配置文件目录不存在");
            return null;
        }
        File configDir = new File(baseUrl.getPath());
        if (!configDir.exists() || !configDir.isDirectory()) {
            log.warn("元数据配置文件目录不存在");
            return null;
        }

        File[] configFiles = configDir.listFiles();
        if (ObjectUtils.isEmpty(configFiles)) {
            log.warn("元数据配置文件目录为空");
            return null;
        }

        String className = field.getDeclaringClass().getName();
        for (File file : configFiles) {
            if (file.isDirectory()) {
                continue;
            }

            String fileName = file.getName().toLowerCase();
            try {
                ObjectMapper mapper = null;
                if (fileName.endsWith(FILE_SUFFIX_YML)) {
                    mapper = yamlMapper;
                } else if (fileName.endsWith(FILE_SUFFIX_JSON)) {
                    mapper = jsonMapper;
                } else {
                    // TODO: 支持配置文件等配置格式
                    continue;
                }

                ModelMetaConfig[] modelMetaConfigs = mapper.readValue(file, ModelMetaConfig[].class);
                for (ModelMetaConfig modelMetaConfig : modelMetaConfigs) {
                    if (!Objects.equals(className, modelMetaConfig.getClassName())) {
                        continue;
                    }
                    if (ObjectUtils.isEmpty(modelMetaConfig.getFieldMeta())) {
                        continue;
                    }

                    FieldMetaConfig fieldMetaConfig = modelMetaConfig.getFieldMeta().stream()
                            .filter(config -> Objects.equals(config.getField(), field.getName()))
                            .findFirst()
                            .orElse(null);
                    if (fieldMetaConfig == null) {
                        continue;
                    }

                    FieldMeta fieldMeta = new FieldMeta();
                    fieldMeta.setName(fieldMetaConfig.getName());
                    fieldMeta.setAlias(fieldMetaConfig.getAlias());
                    fieldMeta.setDefaultValue(fieldMetaConfig.getDefaultValue());
                    fieldMeta.setIgnore(fieldMetaConfig.isIgnore());
                    fieldMeta.setDisplayPolicy(DisplayPolicy.valueOf(DisplayPolicy.class, fieldMetaConfig.getDisplayPolicy()));
                    fieldMeta.setReferEnumType((Class<? extends NamedEnum>) Class.forName(fieldMetaConfig.getReferEnumType()));
                    fieldMeta.setSerializer((Class<? extends Serializer<Object>>) Class.forName(fieldMetaConfig.getSerializer()));
                    fieldMeta.setSource(MetaSource.CONFIGURATION);
                    fieldMeta.setField(field);

                    return fieldMeta;
                }
            } catch (Exception e) {
                log.error("元数据配置文件读取错误! error:{}", e.getMessage(), e);
            }
        }

        return null;
    }
}
