package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
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
            method = "addTraitsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;hasAnyBonusTraits()Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_addTraitsToTooltip(WeaponMaterial material, ItemStack stack) {
        return material.hasAnyBonusTraits() && material.getBonusTraits().stream().anyMatch(trait -> ((IBetterWeaponTrait) trait).isEnabled(material, stack));
    }
}