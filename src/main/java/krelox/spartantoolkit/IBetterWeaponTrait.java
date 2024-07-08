package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.IActionTraitCallback;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.SwordBaseItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IBetterWeaponTrait {
    default float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        return baseDamage;
    }

    default float modifyRangedDamageTaken(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        return baseDamage;
    }

    default void onRangedItemUpdate(WeaponMaterial material, ItemStack stack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {
    }

    default void onRangedHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
    }

    @SuppressWarnings("unused")
    default boolean isEnabled(WeaponMaterial material, ItemStack stack) {
        return !(this instanceof IActionTraitCallback)
                || !(stack.getItem() instanceof SwordBaseItem item)
                || item.getAllWeaponTraits().stream().filter(trait -> !trait.equals(this)).noneMatch(WeaponTrait::isActionTrait);
    }
}