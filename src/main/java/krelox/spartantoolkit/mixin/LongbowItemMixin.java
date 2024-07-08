package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.LongbowItem;
import krelox.spartantoolkit.IBetterWeaponTrait;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LongbowItem.class)
public class LongbowItemMixin extends BowItem implements WeaponItem {
    @Shadow(remap = false)
    protected WeaponMaterial material;
    @Shadow(remap = false)
    protected List<WeaponTrait> rangedTraits;
    @Shadow(remap = false)
    protected float drawTime;

    @Inject(
            method = "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/oblivioussp/spartanweaponry/api/WeaponMaterial;)V",
            at = @At("RETURN"),
            remap = false
    )
    private void spartantoolkit_injectRarity(Properties prop, WeaponMaterial material, CallbackInfo ci) {
        if (material instanceof SpartanMaterial spartanMaterial) {
            rarity = spartanMaterial.getRarity();
        }
    }

    @Redirect(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            )
    )
    private void spartantoolkit_releaseUsing(Optional<IBetterWeaponTrait> optional, Consumer<IBetterWeaponTrait> action, ItemStack stack) {
        optional.filter(trait -> trait.isEnabled(material, stack)).ifPresent(action);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void spartantoolkit_appendHoverText(ItemStack stack, Level levelIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        drawTime = 1.25f;
        triggerEnabledTraits(rangedTraits, trait -> trait.getRangedCallback()
                .ifPresent(callback -> drawTime = callback.modifyLongbowDrawTime(material, drawTime)), stack);
    }

    @Inject(method = "getNockProgress", at = @At("HEAD"), remap = false)
    private void spartantoolkit_getNockProgress(ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<Float> cir) {
        drawTime = 1.25f;
        triggerEnabledTraits(rangedTraits, trait -> trait.getRangedCallback()
                .ifPresent(callback -> drawTime = callback.modifyLongbowDrawTime(material, drawTime)), stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        triggerEnabledTraits(rangedTraits, trait ->
                ((IBetterWeaponTrait) trait).onRangedItemUpdate(material, stack, level, (LivingEntity) entity, itemSlot, isSelected), stack);
    }

    @Override
    @SuppressWarnings("all")
    public WeaponMaterial getMaterial() {
        return material;
    }

    @Deprecated
    public LongbowItemMixin(Properties properties) {
        super(properties);
    }
}
