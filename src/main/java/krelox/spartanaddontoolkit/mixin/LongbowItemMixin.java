package krelox.spartanaddontoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.item.LongbowItem;
import krelox.spartanaddontoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LongbowItem.class)
public class LongbowItemMixin implements WeaponItem {
    @Shadow(remap = false)
    protected WeaponMaterial material;

    @Override
    public WeaponMaterial getMaterial() {
        return material;
    }
}
