package operatelog.util;

import lombok.*;
import operatelog.annotation.OperateField;
import operatelog.annotation.OperateModel;
import operatelog.constant.DisplayPolicy;
import operatelog.constant.NamedEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

/**
 * 注解操作日志单元测试
 *
 * @author : JiangCheng
 * @date : 2023/7/19 17:58
 */
public class AnnotationOperateLogTest {
    @Test
    public void testDiff() {
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
        Map<String, Pair<String, String>> result = OperateLogUtil.diff(optimus, optimus, null);
        // 类型为空，不进行对比
        Assertions.assertEquals(0, result.size());
        // 同一对象，不进行对比
        result = OperateLogUtil.diff(optimus, optimus, AutoBot.class);
        Assertions.assertEquals(0, result.size());
        // 原始数据为空，目标数据被标记的字段不为空，返回被标记的差异字段
        result = OperateLogUtil.diff(null, optimus, AutoBot.class);
        Assertions.assertEquals(4, result.size());
        // 目标数据为空，原始数据被标记的字段不为空，返回被标记的差异字段
        result = OperateLogUtil.diff(optimus, null, AutoBot.class);
        Assertions.assertEquals(4, result.size());

        // 对比擎天柱和大黄蜂
        result = OperateLogUtil.diff(optimus, bee, AutoBot.class);
        Pair<String, String> valueDiff = result.get("名字");
        Assertions.assertNotNull(valueDiff);
        Assertions.assertEquals("Optimus Prime", valueDiff.getLeft());
        Assertions.assertEquals("Bee", valueDiff.getRight());
        valueDiff = result.get("武器.id");
        Assertions.assertNotNull(valueDiff);
        Assertions.assertEquals("1", valueDiff.getLeft());
        Assertions.assertEquals("2", valueDiff.getRight());
        valueDiff = result.get("武器.名称");
        Assertions.assertNotNull(valueDiff);
        Assertions.assertEquals("Nuclear Axe", valueDiff.getLeft());
        Assertions.assertEquals("Left Hand Gun", valueDiff.getRight());

        // 对比开火前和开火后的擎天柱
        AutoBot optimusPhantom = optimus.phantom();
        optimus.attack();
        result = OperateLogUtil.diff(optimusPhantom, optimus, AutoBot.class);
        valueDiff = result.get("武器.状态");
        Assertions.assertNotNull(valueDiff);
        Assertions.assertEquals(WeaponStatus.OFF.getName(), valueDiff.getLeft());
        Assertions.assertEquals(WeaponStatus.FIRING.getName(), valueDiff.getRight());
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
