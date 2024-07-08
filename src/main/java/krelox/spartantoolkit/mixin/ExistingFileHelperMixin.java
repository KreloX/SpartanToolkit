package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExistingFileHelper.class)
public class ExistingFileHelperMixin {
    @Inject(
            method = "exists(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraftforge/common/data/ExistingFileHelper$IResourceType;)Z",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void spartantoolkit_exists(ResourceLocation loc, ExistingFileHelper.IResourceType type, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || loc.getNamespace().equals(ModSpartanWeaponry.ID));
    }
}
