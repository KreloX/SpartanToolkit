package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ModelGenerator.class)
public class ModelGeneratorMixin {
    @ModifyArg(
            method = "createMeleeWeaponModels",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/generators/ItemModelBuilder;texture(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraftforge/client/model/generators/ModelBuilder;"),
            index = 1,
            remap = false
    )
    private String injectTexture(String texture) {
        if (!texture.contains("item/coating/")) return texture;
        return ModSpartanWeaponry.ID + ":" + texture;
    }
}
