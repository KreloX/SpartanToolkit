package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.item.HeavyCrossbowItem;
import krelox.spartantoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HeavyCrossbowItem.class)
public class HeavyCrossbowItemMixin implements WeaponItem {
    @Shadow(remap = false)
    protected WeaponMaterial material;

    @Override
    public WeaponMaterial getMaterial() {
        return material;
    }
}
