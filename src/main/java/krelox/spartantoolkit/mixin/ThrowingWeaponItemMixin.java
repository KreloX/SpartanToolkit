package krelox.spartantoolkit.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.ThrowingWeaponItem;
import com.oblivioussp.spartanweaponry.util.WeaponArchetype;
import krelox.spartantoolkit.IBetterWeaponTrait;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ThrowingWeaponItem.class)
public abstract class ThrowingWeaponItemMixin extends Item implements WeaponItem {
    @Shadow(remap = false)
    public abstract float getDirectAttackDamage();

    @Shadow(remap = false)
    protected double attackSpeed;

    @Shadow(remap = false)
    public abstract Collection<WeaponTrait> getAllWeaponTraits();

    @Shadow(remap = false)
    protected Multimap<Attribute, AttributeModifier> modifiers;

    @Inject(
            method = "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;Lcom/oblivioussp/spartanweaponry/util/WeaponArchetype;FFFII)V",
            at = @At("RETURN"),
            remap = false
    )
    private void spartantoolkit_injectRarity(Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn,
                                             float weaponBaseDamage, float weaponDamageMultiplier, float weaponSpeed,
                                             int maxAmmoCapacity, int chargeTicks, CallbackInfo ci) {
        if (material instanceof SpartanMaterial spartanMaterial) {
            rarity = spartanMaterial.getRarity();
        }
    }

    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), remap = false)
    public void spartantoolkit_getAttributeModifiers(EquipmentSlot slot, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDirectAttackDamage(), AttributeModifier.Operation.ADDITION));
        mapBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed - 4.0D, AttributeModifier.Operation.ADDITION));

        triggerEnabledTraits(getAllWeaponTraits(), trait -> trait.getMeleeCallback()
                .ifPresent(callback -> callback.onModifyAttributesMelee(mapBuilder)), stack);

        modifiers = mapBuilder.build();
    }

    @Redirect(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_inventoryTick(List<WeaponTrait> traits, Consumer<WeaponTrait> consumer, ItemStack stack) {
        triggerEnabledTraits(traits, consumer, stack);
    }

    @Redirect(
            method = "appendHoverText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;hasAnyBonusTraits()Z",
                    remap = false
            )
    )
    private boolean spartantoolkit_appendHoverText(WeaponMaterial material, ItemStack stack) {
        return material.hasAnyBonusTraits() && material.getBonusTraits().stream().anyMatch(trait -> ((IBetterWeaponTrait) trait).isEnabled(material, stack));
    }

    @Redirect(
            method = "hurtEnemy",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_hurtEnemy(List<WeaponTrait> traits, Consumer<WeaponTrait> consumer, ItemStack stack) {
        triggerEnabledTraits(traits, consumer, stack);
    }

    @Redirect(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_releaseUsing(List<WeaponTrait> traits, Consumer<WeaponTrait> consumer, ItemStack stack) {
        triggerEnabledTraits(traits, consumer, stack);
    }

    @Redirect(
            method = "onCraftedBy",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_onCraftedBy(List<WeaponTrait> traits, Consumer<WeaponTrait> consumer, ItemStack stack) {
        triggerEnabledTraits(traits, consumer, stack);
    }

    @Redirect(
            method = "getMaxChargeTicks",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_getMaxChargeTicks(Optional<IBetterWeaponTrait> optional, ItemStack stack) {
        return optional.isPresent() && optional.get().isEnabled(getMaterial(), stack);
    }

    @Deprecated
    private ThrowingWeaponItemMixin(Properties properties) {
        super(properties);
    }
}
