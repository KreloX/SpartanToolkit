package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.WeaponTraits;
import com.oblivioussp.spartanweaponry.api.trait.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.Optional;

public class BetterWeaponTrait extends WeaponTrait implements IBetterWeaponTrait, IMeleeTraitCallback, IThrowingTraitCallback, IRangedTraitCallback, IActionTraitCallback {
    @SuppressWarnings("unused")
    public BetterWeaponTrait(String typeIn, String modIdIn, TraitQuality qualityIn) {
        super(typeIn, modIdIn, qualityIn);
    }

    public String getDescription() {
        return "The author of this trait should provide a description or set it to null";
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

    @Override
    protected void addTooltipTitle(ItemStack stack, List<Component> tooltip, ChatFormatting... formatting) {
        if (level == 0) {
            super.addTooltipTitle(stack, tooltip, formatting);
            return;
        }
        var titleText = Component.literal("- ").withStyle(formatting);
        tooltip.add(titleText
                .append(Component.translatable(String.format("tooltip.%s.trait.%s", modId, type)))
                .append(" ")
                .append(Component.translatable("enchantment.level." + level)));

    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        if (getDescription() == null) return;
        String registryName = RegistryManager.FROZEN.getRegistry(WeaponTraits.REGISTRY_KEY).getKey(this).getPath();
        tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", modId, registryName)).withStyle(DESCRIPTION_FORMAT)));
    }
}
