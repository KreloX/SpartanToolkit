package krelox.spartantoolkit.mixin;

import com.google.gson.JsonObject;
import com.oblivioussp.spartanweaponry.api.data.recipe.ConditionalShapedRecipeBuilder;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ConditionalShapedRecipeBuilder.Result.class)
public class ConditionalShapedRecipeBuilderResultMixin {
    @Shadow(remap = false)
    @Final
    private Item result;

    @ModifyVariable(
            method = "serializeRecipeData",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/gson/JsonObject;addProperty(Ljava/lang/String;Ljava/lang/String;)V",
                    ordinal = 1,
                    remap = false
            ),
            ordinal = 2
    )
    private JsonObject spartantoolkit_serializeRecipeData(JsonObject resultJson) {
        if (result instanceof WeaponItem weapon && weapon.getMaterial() instanceof SpartanMaterial material) {
            var listTag = new ListTag();
            material.enchantments.forEach((enchantment, level) -> {
                if (result.getDefaultInstance().canApplyAtEnchantingTable(enchantment.get())) {
                    listTag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment.get()), (byte) (int) level));
                }
            });
            if (!listTag.isEmpty()) {
                resultJson.addProperty("type", "minecraft:item_nbt");
                resultJson.addProperty("nbt", "{Enchantments:%s}".formatted(listTag.toString()));
            }
        }
        return resultJson;
    }
}
