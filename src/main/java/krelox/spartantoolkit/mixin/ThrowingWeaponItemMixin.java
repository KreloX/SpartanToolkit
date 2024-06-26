package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.item.ThrowingWeaponItem;
import krelox.spartantoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrowingWeaponItem.class)
public abstract class ThrowingWeaponItemMixin implements WeaponItem {
}
