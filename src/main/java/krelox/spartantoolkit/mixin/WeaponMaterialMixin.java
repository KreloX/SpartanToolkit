package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.util.WeaponType;
import krelox.spartantoolkit.IBetterWeaponTrait;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(WeaponMaterial.class)
public class WeaponMaterialMixin {

    @ModifyArg(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
                    ordinal = 0
            ),
            remap = false
    )
    private <T> Predicate<T> spartantoolkit_reload(Predicate<T> predicate) {
        return t -> true;
    }

    @Redirect(
            method = "addTraitsToTooltip(Lnet/minecraft/world/item/ItemStack;Lcom/oblivioussp/spartanweaponry/util/WeaponType;Ljava/util/List;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;hasAnyBonusTraits(Lcom/oblivioussp/spartanweaponry/util/WeaponType;)Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_addTraitsToTooltip(WeaponMaterial material, WeaponType type, ItemStack stack) {
        return material.hasAnyBonusTraits(type) && material.getBonusTraits(type).stream().anyMatch(trait -> ((IBetterWeaponTrait) trait).isEnabled(material, stack));
    }
}
