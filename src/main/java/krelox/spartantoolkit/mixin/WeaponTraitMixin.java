package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.IBetterWeaponTrait;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WeaponTrait.class)
public class WeaponTraitMixin implements IBetterWeaponTrait {
    @Inject(
            method = "addTooltip(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;ZLcom/oblivioussp/spartanweaponry/api/trait/WeaponTrait$InvalidReason;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void spartantoolkit_addTooltip(ItemStack stack, List<Component> tooltip, boolean isShiftPressed, WeaponTrait.InvalidReason invalidReason, CallbackInfo ci) {
        if (!isEnabled(((WeaponItem) stack.getItem()).getMaterial(), stack)) {
            ci.cancel();
        }
    }
}
