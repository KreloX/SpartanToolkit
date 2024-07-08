package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.tags.ModWeaponTraitTags;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class SpartanMaterial extends WeaponMaterial {
    public final Set<RegistryObject<WeaponTrait>> traits;
    public final Map<Supplier<Enchantment>, Integer> enchantments;

    public SpartanMaterial(String name, String modid, Tier tier,
                           TagKey<Item> repairMaterial, Set<RegistryObject<WeaponTrait>> traits, Map<Supplier<Enchantment>, Integer> enchantments) {
        super(name, modid, tier, repairMaterial, ModWeaponTraitTags.create(new ResourceLocation(modid, name)));
        if (traits.stream().map(RegistryObject::get).filter(WeaponTrait::isActionTrait).count() > 1) {
            throw new IllegalArgumentException("Material '%s:%s' has more than one action trait".formatted(modid, name));
        }
        this.traits = traits;
        this.enchantments = enchantments;
    }

    @SafeVarargs
    @SuppressWarnings("unused")
    public SpartanMaterial(String name, String modid, Tier tier,
                           TagKey<Item> repairMaterial, RegistryObject<WeaponTrait>... traits) {
        this(name, modid, tier, repairMaterial, Set.of(traits), Map.of());
    }

    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    public TagKey<Item> getPlanks() {
        return ItemTags.PLANKS;
    }

    public TagKey<Item> getStick() {
        return Tags.Items.RODS_WOODEN;
    }

    public TagKey<Item> getString() {
        return Tags.Items.STRING;
    }

    public ItemLike getBow() {
        return Items.BOW;
    }

    public ItemLike getHandle() {
        return ModItems.HANDLE.get();
    }

    public ItemLike getPole() {
        return ModItems.POLE.get();
    }
}
