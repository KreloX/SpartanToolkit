package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.trait.IActionTraitCallback;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.item.HeavyCrossbowItem;
import com.oblivioussp.spartanweaponry.item.LongbowItem;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface WeaponItem {
    WeaponMaterial getMaterial();

    default boolean isRanged() {
        return this instanceof LongbowItem || this instanceof HeavyCrossbowItem;
    }

    default void triggerEnabledTraits(Collection<WeaponTrait> traits, Consumer<WeaponTrait> consumer, ItemStack stack) {
        if (traits != null) {
            traits.stream()
                    .filter(trait -> ((IBetterWeaponTrait) trait).isEnabled(getMaterial(), stack))
                    .forEach(consumer);
        }
    }

    default void triggerFirstEnabledActionTrait(ItemStack stack, Consumer<IActionTraitCallback> consumer) {
        if (getMaterial() instanceof SpartanMaterial material) {
            material.getBonusTraits().stream()
                    .filter(WeaponTrait::isActionTrait)
                    .filter(trait -> ((IBetterWeaponTrait) trait).isEnabled(getMaterial(), stack))
                    .findFirst()
                    .flatMap(WeaponTrait::getActionCallback)
                    .ifPresent(consumer);
        }
    }

    default <T> T getFirstEnabledActionTraitResult(ItemStack stack, Function<IActionTraitCallback, T> result, Supplier<T> fallback) {
        if (getMaterial() instanceof SpartanMaterial material) {
            return material.getBonusTraits().stream()
                    .filter(WeaponTrait::isActionTrait)
                    .filter(trait -> ((IBetterWeaponTrait) trait).isEnabled(getMaterial(), stack))
                    .findFirst()
                    .flatMap(WeaponTrait::getActionCallback)
                    .map(result)
                    .orElseGet(fallback);
        }
        return fallback.get();
    }
}
