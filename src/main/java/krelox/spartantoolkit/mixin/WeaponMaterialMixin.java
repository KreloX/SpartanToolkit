package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.util.WeaponType;
import krelox.spartantoolkit.IBetterWeaponTrait;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(WeaponMaterial.class)
public abstract class WeaponMaterialMixin {
    @Shadow(remap = false)
    public abstract List<WeaponTrait> getBonusTraits(WeaponType type);

    @Redirect(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
                    ordinal = 0
            ),
            remap = false
    )
    private <T> Stream<T> spartantoolkit_skipActionTraitCheck(Stream<T> stream, Predicate<? super T> predicate) {
        return stream;
    }

    @Redirect(
            method = "addTraitsToTooltip(Lnet/minecraft/world/item/ItemStack;Lcom/oblivioussp/spartanweaponry/util/WeaponType;Ljava/util/List;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;hasAnyBonusTraits(Lcom/oblivioussp/spartanweaponry/util/WeaponType;)Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_hasAnyEnabledBonusTraits(WeaponMaterial material, WeaponType type, ItemStack stack) {
        return material.hasAnyBonusTraits(type) && material.getBonusTraits(type).stream().anyMatch(trait -> ((IBetterWeaponTrait) trait).isEnabled(material, stack));
    }

    @Inject(method = "lambda$addTraitsToTooltip$2", at = @At("HEAD"), cancellable = true, remap = false)
    private static void spartantoolkit_cancelForEach(ItemStack stack, List<Component> tooltip, boolean isShiftPressed, WeaponTrait trait, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "addTraitsToTooltip(Lnet/minecraft/world/item/ItemStack;Lcom/oblivioussp/spartanweaponry/util/WeaponType;Ljava/util/List;Z)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1, shift = At.Shift.AFTER),
            remap = false
    )
    private void spartantoolkit_addTraitsToTooltip(ItemStack stack, WeaponType type, List<Component> tooltip, boolean isShiftPressed, CallbackInfo ci) {
        getBonusTraits(type).forEach(trait -> trait.addTooltip(stack, tooltip, isShiftPressed, WeaponTrait.InvalidReason.NONE));
    }
}
