package krelox.spartanaddontoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.item.HeavyCrossbowItem;
import krelox.spartanaddontoolkit.WeaponItem;
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
