package operatelog.demo.model;

import lombok.*;
import operatelog.constant.NamedEnum;

/**
 * 武器
 *
 * @author : JiangCheng
 * @date : 2023/7/27 18:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weapon {
    /**
     * ID
     */
    Long id;
    /**
     * 名称
     */
    String name;
    /**
     * 状态
     */
    Byte status;

    /**
     * 武器开火
     */
    public void fire() {
        status = WeaponStatus.FIRING.value;
    }

    @Getter
    @AllArgsConstructor
    public enum WeaponStatus implements NamedEnum<Byte> {
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
