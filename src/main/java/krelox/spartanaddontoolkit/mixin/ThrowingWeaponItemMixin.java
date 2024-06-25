package krelox.spartanaddontoolkit.mixin;

import com.oblivioussp.spartanweaponry.item.ThrowingWeaponItem;
import krelox.spartanaddontoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrowingWeaponItem.class)
public abstract class ThrowingWeaponItemMixin implements WeaponItem {
}
