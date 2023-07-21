package operatelog.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import operatelog.constant.CommonConstant;
import operatelog.constant.MetaSource;
import operatelog.model.ModelMeta;
import operatelog.strategy.ModelMetaStrategy;
import operatelog.strategy.model.ModelMetaConfig;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * 日志对象配置构建策略
 *
 * @author : JiangCheng
 * @date : 2023/7/20 17:50
 */
@Slf4j
public class ModelMetaConfigStrategy implements ModelMetaStrategy {
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
    public ModelMeta buildMeta(@Nonnull Class<?> clazz) {
        URL baseUrl = clazz.getResource(CommonConstant.META_CONFIG_DIR);
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
                    if (!Objects.equals(clazz.getName(), modelMetaConfig.getClassName())) {
                        continue;
                    }

                    ModelMeta modelMeta = new ModelMeta();
                    modelMeta.setTargetFields(modelMetaConfig.getTargetFields());
                    modelMeta.setIgnoreUnmarkedField(modelMetaConfig.isIgnoreUnmarkedField());
                    modelMeta.setRetrieveSuperField(modelMetaConfig.isRetrieveSuperField());
                    modelMeta.setSource(MetaSource.CONFIGURATION);
                    modelMeta.setClazz(clazz);

                    return modelMeta;
                }
            } catch (Exception e) {
                log.error("元数据配置文件读取错误! error:{}", e.getMessage(), e);
            }
        }

        return null;
    }
}
