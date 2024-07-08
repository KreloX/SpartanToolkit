package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(WeaponMaterial.class)
public class WeaponMaterialMixin {

    @ModifyArg(
            method = "reload",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
            ),
            remap = false
    )
    private <T> Predicate<T> spartantoolkit_reload(Predicate<T> predicate) {
        return t -> true;
    }
}
