package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WeaponTrait.class)
public class WeaponTraitMixin {
    @Shadow(remap = false)
    protected String modId;

    @ModifyArg(
            method = "initTooltipTypes",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"),
            index = 0
    )
    private String formatTooltip(String original) {
        return original.replace(modId, ModSpartanWeaponry.ID);
    }
}
