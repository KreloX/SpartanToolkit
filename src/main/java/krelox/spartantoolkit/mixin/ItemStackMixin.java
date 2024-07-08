package krelox.spartantoolkit.mixin;

import krelox.spartantoolkit.SpartanAddon;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.extensions.IForgeItemStack;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack {
    @Shadow
    public abstract void enchant(Enchantment p_41664_, int p_41665_);

    @Inject(
            method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/nbt/CompoundTag;)V",
            at = @At("RETURN")
    )
    public void spartantoolkit_init(ItemLike item, int p_41605_, CompoundTag p_41606_, CallbackInfo ci) {
        if (item instanceof WeaponItem weapon) {
            ModList.get().forEachModContainer((modid, modContainer) -> {
                if (modContainer.getMod() instanceof SpartanAddon addon) {
                    for (var material : addon.getMaterials()) {
                        if (weapon.getMaterial().equals(material)) {
                            material.enchantments.forEach((enchantment, integer) -> {
                                if (canApplyAtEnchantingTable(enchantment.get())) {
                                    enchant(enchantment.get(), integer);
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
