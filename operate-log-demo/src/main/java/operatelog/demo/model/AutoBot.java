package operatelog.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 汽车人
 *
 * @author : JiangCheng
 * @date : 2023/7/27 18:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoBot {
    /**
     * ID
     */
    Long id;
    /**
     * 名称
     */
    String name;
    /**
     * 武器
     */
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
