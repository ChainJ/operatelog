package operatelog.demo;

import lombok.*;
import operatelog.annotation.OperateField;
import operatelog.annotation.OperateModel;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.NamedEnum;
import operatelog.util.OperateLogUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * 注解操作日志示例
 *
 * @author : JiangCheng
 * @date : 2023/7/28 17:58
 */
public class AnnotationOperateLogDemo {
    public static void main(String... args) {
        AutoBot optimus = AutoBot.builder()
                .id(1L)
                .name("Optimus Prime")
                .weapon(Weapon.builder()
                        .id(1L)
                        .name("Nuclear Axe")
                        .status(WeaponStatus.OFF.value)
                        .build())
                .build();
        AutoBot bee = AutoBot.builder()
                .id(2L)
                .name("Bee")
                .weapon(Weapon.builder()
                        .id(2L)
                        .name("Left Hand Gun")
                        .status(WeaponStatus.OFF.value)
                        .build())
                .build();
        Map<String, Pair<String, String>> fieldChanges = OperateLogUtil.diff(optimus, bee);
        fieldChanges.forEach((fieldName, change) -> {
            String pattern = "field [%s] change from [%s] to [%s]; %n";
            System.out.printf(pattern, fieldName, change.getLeft(), change.getRight());
        });
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @OperateModel(fields = {"id", "name", "weapon"}, ignoreUnmarkedField = true)
    static class AutoBot {
        /**
         * ID
         */
        Long id;
        /**
         * 名称
         */
        @OperateField(name = "name", alias = "名字")
        String name;
        /**
         * 武器
         */
        @OperateField(name = "weapon", alias = "武器", displayPolicy = DisplayPolicy.EXTRACT)
        Weapon weapon;

        /**
         * 攻击
         */
        public void attack() {
            if (weapon == null) {
                throw new RuntimeException("缺少可用武器");
            }
            weapon.fire();
        }

        /**
         * 生成一个幻影
         *
         * @return 幻影
         */
        public AutoBot phantom() {
            Weapon fakeWeapon = new Weapon();
            fakeWeapon.id = weapon.id;
            fakeWeapon.name = weapon.name;
            fakeWeapon.status = weapon.status;

            AutoBot phantom = new AutoBot();
            phantom.id = id;
            phantom.name = name;
            phantom.weapon = fakeWeapon;

            return phantom;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Weapon {
        /**
         * ID
         */
        Long id;
        /**
         * 名称
         */
        @OperateField(name = "name", alias = "名称")
        String name;
        /**
         * 状态
         */
        @OperateField(name = "status", alias = "状态", displayPolicy = DisplayPolicy.NAMED_ENUM, referEnumType = WeaponStatus.class)
        Byte status;

        /**
         * 武器开火
         */
        public void fire() {
            status = WeaponStatus.FIRING.value;
        }
    }

    @Getter
    @AllArgsConstructor
    enum WeaponStatus implements NamedEnum<Byte> {
        OFF((byte) 1, "关闭"),
        READY((byte) 2, "就绪"),
        FIRING((byte) 4, "开火"),
        ;

        /**
         * 枚举值
         */
        final Byte value;
        /**
         * 枚举描述
         */
        final String name;
    }
}
