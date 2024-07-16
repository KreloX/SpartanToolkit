package krelox.spartantoolkit.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.SwordBaseItem;
import com.oblivioussp.spartanweaponry.util.WeaponArchetype;
import krelox.spartantoolkit.IBetterWeaponTrait;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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

@Mixin(SwordBaseItem.class)
public abstract class SwordBaseItemMixin extends SwordItem implements WeaponItem {
    @Shadow(remap = false)
    public abstract float getDirectAttackDamage();

    @Shadow(remap = false)
    protected double attackSpeed;

    @Shadow(remap = false)
    public abstract Collection<WeaponTrait> getAllWeaponTraits();

    @Shadow(remap = false)
    protected Multimap<Attribute, AttributeModifier> modifiers;

    @Inject(
            method = "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;Lcom/oblivioussp/spartanweaponry/util/WeaponArchetype;FFD)V",
            at = @At("RETURN"),
            remap = false
    )
    private void spartantoolkit_injectRarity(Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn,
                                             float weaponBaseDamage, float weaponDamageMultiplier, double weaponSpeed, CallbackInfo ci) {
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
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            )
    )
    private boolean spartantoolkit_useOn(Optional<IBetterWeaponTrait> optional, UseOnContext context) {
        if (optional.isEmpty()) {
            triggerFirstEnabledActionTrait(context.getItemInHand(), trait -> trait.useOn(context));
            return false;
        }
        return optional.get().isEnabled(getMaterial(), context.getItemInHand());
    }

    @Redirect(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"
            )
    )
    private InteractionResult spartantoolkit_useOn(SwordItem item, UseOnContext context) {
        return getFirstEnabledActionTraitResult(context.getItemInHand(),
                callback -> callback.useOn(context),
                () -> super.useOn(context));
    }

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            )
    )
    private boolean spartantoolkit_use(Optional<IBetterWeaponTrait> optional, Level level, Player player, InteractionHand hand) {
        if (optional.isEmpty()) {
            triggerFirstEnabledActionTrait(player.getItemInHand(hand), trait -> trait.use(player.getItemInHand(hand), level, player, hand));
            return false;
        }
        return optional.get().isEnabled(getMaterial(), player.getItemInHand(hand));
    }

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;"
            )
    )
    private InteractionResultHolder<ItemStack> spartantoolkit_use(SwordItem item, Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return getFirstEnabledActionTraitResult(stack,
                callback -> callback.use(stack, level, player, hand),
                () -> super.use(level, player, hand));
    }

    @Redirect(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_releaseUsing(Optional<IBetterWeaponTrait> optional, Consumer<IBetterWeaponTrait> action,
                                             ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        optional.filter(trait -> trait.isEnabled(getMaterial(), stack)).ifPresentOrElse(action, () ->
                triggerFirstEnabledActionTrait(stack, trait -> trait.releaseUsing(stack, level, entityLiving, timeLeft, getDirectAttackDamage())));
    }

    @Redirect(
            method = "onUseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_onUseTick(Optional<IBetterWeaponTrait> optional, Consumer<IBetterWeaponTrait> action,
                                          Level levelIn, LivingEntity player, ItemStack stack, int count) {
        optional.filter(trait -> trait.isEnabled(getMaterial(), stack)).ifPresentOrElse(action, () ->
                triggerFirstEnabledActionTrait(stack, trait -> trait.onUsingTick(stack, player, count, getDirectAttackDamage())));
    }

    @Redirect(
            method = "getUseDuration",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            )
    )
    private boolean spartantoolkit_getUseDuration(Optional<IBetterWeaponTrait> optional, ItemStack stack) {
        return optional.isPresent() && optional.get().isEnabled(getMaterial(), stack);
    }

    @Redirect(
            method = "getUseDuration",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;getUseDuration(Lnet/minecraft/world/item/ItemStack;)I"
            )
    )
    private int spartantoolkit_getUseDuration(SwordItem item, ItemStack stack) {
        return getFirstEnabledActionTraitResult(stack,
                callback -> callback.getUseDuration(stack),
                () -> super.getUseDuration(stack));
    }

    @Redirect(
            method = "getUseAnimation",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            )
    )
    private boolean spartantoolkit_getUseAnimation(Optional<IBetterWeaponTrait> optional, ItemStack stack) {
        return optional.isPresent() && optional.get().isEnabled(getMaterial(), stack);
    }

    @Redirect(
            method = "getUseAnimation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;getUseAnimation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/UseAnim;"
            )
    )
    private UseAnim spartantoolkit_getUseAnimation(SwordItem item, ItemStack stack) {
        return getFirstEnabledActionTraitResult(stack,
                callback -> callback.getUseAnimation(stack),
                () -> super.getUseAnimation(stack));
    }

    @Redirect(
            method = "doesSneakBypassUse",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_doesSneakBypassUse(Optional<IBetterWeaponTrait> optional, ItemStack stack,
                                                      LevelReader level, BlockPos pos, Player player) {
        return optional.isPresent() && optional.get().isEnabled(getMaterial(), stack);
    }

    @Redirect(
            method = "doesSneakBypassUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/SwordItem;doesSneakBypassUse(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Z"
            ),
            remap = false
    )
    private boolean spartantoolkit_doesSneakBypassUse(SwordItem item, ItemStack stack, LevelReader levelReader, BlockPos blockPos, Player player) {
        return getFirstEnabledActionTraitResult(stack,
                callback -> callback.doesSneakBypassUse(stack, levelReader, blockPos, player),
                () -> super.doesSneakBypassUse(stack, levelReader, blockPos, player));
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

    @Deprecated
    private SwordBaseItemMixin(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }
}
