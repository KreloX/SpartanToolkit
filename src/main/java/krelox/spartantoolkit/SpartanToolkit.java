package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.IWeaponTraitContainer;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.util.WeaponType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(SpartanToolkit.MODID)
@Mod.EventBusSubscriber(modid = SpartanToolkit.MODID)
public class SpartanToolkit {
    public static final String MODID = "spartantoolkit";

    @SubscribeEvent
    public static void onHitEntity(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        var projectile = event.getSource().getDirectEntity();

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        var stack = attacker.getMainHandItem();

        if (!stack.isEmpty() && stack.getItem() instanceof WeaponItem weapon) {
            var material = weapon.getMaterial();

            if (!(weapon instanceof IWeaponTraitContainer<?> container)) {
                weapon.triggerEnabledTraits(material.getBonusTraits(WeaponType.RANGED), trait ->
                        ((IBetterWeaponTrait) trait).onRangedHitEntity(material, stack, target, attacker, projectile), stack);
            } else if (!(attacker instanceof Player)) {
                weapon.triggerEnabledTraits(container.getAllWeaponTraits(), trait -> trait.getMeleeCallback()
                        .ifPresent(callback -> callback.onHitEntity(material, stack, target, attacker, projectile)), stack);
            }
        }
    }

    @SubscribeEvent
    public static void onHurtEntity(LivingHurtEvent event) {
        var target = event.getEntity();
        var source = event.getSource();
        float amount = event.getAmount();

        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        if (attacker == source.getDirectEntity()) return;

        var attackerStack = attacker.getMainHandItem();
        var targetStack = target.getMainHandItem();

        if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof WeaponItem weapon && weapon.isRanged()) {
            var material = weapon.getMaterial();
            var traits = material.getBonusTraits(WeaponType.RANGED);

            if (traits == null) return;

            for (WeaponTrait trait : traits) {
                IBetterWeaponTrait betterTrait = (IBetterWeaponTrait) trait;
                if (betterTrait.isEnabled(material, attackerStack)) {
                    amount = betterTrait.modifyRangedDamageDealt(material, amount, source, attacker, target);
                }
            }
        }
        if (!targetStack.isEmpty() && targetStack.getItem() instanceof WeaponItem weapon && weapon.isRanged()) {
            var material = weapon.getMaterial();
            var traits = material.getBonusTraits(WeaponType.RANGED);

            if (traits == null) return;

            for (WeaponTrait trait : traits) {
                IBetterWeaponTrait betterTrait = (IBetterWeaponTrait) trait;
                if (betterTrait.isEnabled(material, targetStack)) {
                    amount = betterTrait.modifyRangedDamageTaken(material, amount, source, attacker, target);
                }
            }
        }
        if (amount != event.getAmount()) {
            event.setAmount(amount);
        }
    }
}
