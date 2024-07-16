package krelox.spartantoolkit.mixin;

import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(I18n.class)
public class I18nMixin {
    @Inject(method = "exists", at = @At("RETURN"), cancellable = true)
    private static void i18nExists(String key, CallbackInfoReturnable<Boolean> cir) {
        if (key.matches("^tooltip\\.\\w+\\.trait\\.\\w+\\.desc$")) {
            cir.setReturnValue(true);
        }
    }
}
