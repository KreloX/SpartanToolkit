package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BetterWeaponTrait extends WeaponTrait implements IBetterWeaponTrait, IMeleeTraitCallback, IThrowingTraitCallback, IRangedTraitCallback, IActionTraitCallback {
    @SuppressWarnings("unused")
    public BetterWeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
    }

    @Override
    public float modifyRangedDamageDealt(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        if (getMeleeCallback().isPresent()) {
            return getMeleeCallback().get().modifyDamageDealt(material, baseDamage, source, attacker, victim);
        }
        return baseDamage;
    }

    @Override
    public float modifyRangedDamageTaken(WeaponMaterial material, float baseDamage, DamageSource source, LivingEntity attacker, LivingEntity victim) {
        if (getMeleeCallback().isPresent()) {
            return getMeleeCallback().get().modifyDamageTaken(material, baseDamage, source, attacker, victim);
        }
        return baseDamage;
    }

    @Override
    public void onRangedItemUpdate(WeaponMaterial material, ItemStack stack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {
        getMeleeCallback().ifPresent(callback -> callback.onItemUpdate(material, stack, level, entity, itemSlot, isSelected));
    }

    @Override
    public void onRangedHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        getMeleeCallback().ifPresent(callback -> callback.onHitEntity(material, stack, target, attacker, projectile));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack stack, Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Optional<IMeleeTraitCallback> getMeleeCallback() {
        if (isMelee) return Optional.of(this);
        return super.getMeleeCallback();
    }

    @Override
    public Optional<IRangedTraitCallback> getRangedCallback() {
        if (isRanged) return Optional.of(this);
        return super.getRangedCallback();
    }

    @Override
    public Optional<IThrowingTraitCallback> getThrowingCallback() {
        if (isThrowing) return Optional.of(this);
        return super.getThrowingCallback();
    }

    @Override
    public Optional<IActionTraitCallback> getActionCallback() {
        if (isAction) return Optional.of(this);
        return super.getActionCallback();
    }
}
