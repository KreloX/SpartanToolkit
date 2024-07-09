package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.HeavyCrossbowItem;
import com.oblivioussp.spartanweaponry.util.Defaults;
import krelox.spartantoolkit.IBetterWeaponTrait;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
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

@Mixin(HeavyCrossbowItem.class)
public class HeavyCrossbowItemMixin extends CrossbowItem implements WeaponItem {
    @Shadow(remap = false)
    protected WeaponMaterial material;
    @Shadow(remap = false)
    protected List<WeaponTrait> rangedTraits;
    @Shadow(remap = false)
    protected int loadTicks;
    @Shadow(remap = false)
    protected int aimTicks;

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
            method = "spawnProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            ),
            remap = false
    )
    private void spartantoolkit_spawnProjectile(Optional<IBetterWeaponTrait> optional, Consumer<IBetterWeaponTrait> action, ItemStack crossbow) {
        optional.filter(trait -> trait.isEnabled(material, crossbow)).ifPresent(action);
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
        return material.hasAnyBonusTraits() && rangedTraits.stream().anyMatch(trait -> ((IBetterWeaponTrait) trait).isEnabled(material, stack));
    }

    @Inject(method = "getFullLoadTicks", at = @At("HEAD"), remap = false)
    private void spartantoolkit_getFullLoadTicks(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        loadTicks = Defaults.CrossbowTicksToLoad;
        triggerEnabledTraits(rangedTraits, trait -> trait.getRangedCallback()
                .ifPresent(callback -> loadTicks = callback.modifyHeavyCrossbowLoadTime(material, loadTicks)), stack);
    }

    @Inject(method = "getAimTicks", at = @At("HEAD"), remap = false)
    private void spartantoolkit_getAimTicks(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        aimTicks = Defaults.CrossbowInaccuracyTicks;
        triggerEnabledTraits(rangedTraits, trait -> trait.getRangedCallback()
                .ifPresent(callback -> aimTicks = callback.modifyHeavyCrossbowAimTime(material, aimTicks)), stack);
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
    private HeavyCrossbowItemMixin(Properties properties) {
        super(properties);
    }
}
