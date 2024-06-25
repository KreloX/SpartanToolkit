package krelox.spartanaddontoolkit.mixin;

import com.oblivioussp.spartanweaponry.item.SwordBaseItem;
import krelox.spartanaddontoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordBaseItem.class)
public abstract class SwordBaseItemMixin implements WeaponItem {
}
