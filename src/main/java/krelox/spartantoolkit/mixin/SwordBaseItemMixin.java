package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.item.SwordBaseItem;
import krelox.spartantoolkit.WeaponItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordBaseItem.class)
public abstract class SwordBaseItemMixin implements WeaponItem {
}
