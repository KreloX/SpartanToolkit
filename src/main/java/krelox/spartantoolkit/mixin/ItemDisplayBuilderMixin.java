package krelox.spartantoolkit.mixin;

import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.world.item.CreativeModeTab$ItemDisplayBuilder")
public class ItemDisplayBuilderMixin {
    @ModifyVariable(
            method = "accept(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private ItemStack spartantoolkit_accept(ItemStack stack) {
        if (stack.getItem() instanceof WeaponItem weapon && weapon.getMaterial() instanceof SpartanMaterial material) {
            material.enchantments.forEach((enchantment, level) -> {
                if (stack.getEnchantmentLevel(enchantment.get()) == 0 && stack.canApplyAtEnchantingTable(enchantment.get())) {
                    stack.enchant(enchantment.get(), level);
                }
            });
        }
        return stack;
    }
}
