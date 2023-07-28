package operatelog.demo;

import operatelog.demo.model.AutoBot;
import operatelog.demo.model.Weapon;
import operatelog.util.OperateLogUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * 配置文件操作日志示例
 *
 * @author : JiangCheng
 * @date : 2023/7/27 18:11
 */
public class ConfigOperateLogDemo {
    public static void main(String... args) {
        System.setProperty("operate_log.config.file.path", "/config/operate-log.properties");
        AutoBot optimus = AutoBot.builder()
                .id(1L)
                .name("Optimus Prime")
                .weapon(Weapon.builder()
                        .id(1L)
                        .name("Nuclear Axe")
                        .status(Weapon.WeaponStatus.OFF.getValue())
                        .build())
                .build();
        AutoBot bee = AutoBot.builder()
                .id(2L)
                .name("Bee")
                .weapon(Weapon.builder()
                        .id(2L)
                        .name("Left Hand Gun")
                        .status(Weapon.WeaponStatus.OFF.getValue())
                        .build())
                .build();
        Map<String, Pair<String, String>> fieldChanges = OperateLogUtil.diff(optimus, bee);
        fieldChanges.forEach((fieldName, change) -> {
            String pattern = "field [%s] change from [%s] to [%s]; %n";
            System.out.printf(pattern, fieldName, change.getLeft(), change.getRight());
        });
    }
}
