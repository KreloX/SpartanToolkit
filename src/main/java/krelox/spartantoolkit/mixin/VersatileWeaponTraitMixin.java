package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.trait.VersatileWeaponTrait;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VersatileWeaponTrait.class)
public class VersatileWeaponTraitMixin extends WeaponTrait {
    @Override
    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        return enchantIn.equals(Enchantments.BLOCK_EFFICIENCY) || enchantIn.equals(Enchantments.BLOCK_FORTUNE) || super.isEnchantmentCompatible(enchantIn);
    }

    @Deprecated
    private VersatileWeaponTraitMixin(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
    }
}
