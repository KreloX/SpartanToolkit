package krelox.spartantoolkit.mixin;

import com.oblivioussp.spartanweaponry.api.IWeaponTraitContainer;
import com.oblivioussp.spartanweaponry.api.trait.IMeleeTraitCallback;
import com.oblivioussp.spartanweaponry.event.CommonEventHandler;
import krelox.spartantoolkit.IBetterWeaponTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CommonEventHandler.class)
public class CommonEventHandlerMixin {
    @Redirect(
            method = "onLivingHurt",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"),
            remap = false
    )
    @SuppressWarnings("DataFlowIssue")
    private static boolean spartantoolkit_onLivingHurt(Optional<IMeleeTraitCallback> opt, LivingHurtEvent event) {
        var attacker = (LivingEntity) event.getSource().getEntity();
        var attackerStack = attacker.getMainHandItem();
        var container = (IWeaponTraitContainer<?>) attackerStack.getItem();
        return opt.isPresent() && ((IBetterWeaponTrait) opt.get()).isEnabled(container.getMaterial(), attackerStack);
    }
}
