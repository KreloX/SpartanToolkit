package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.tags.ModWeaponTraitTags;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.init.ModItems;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class SpartanMaterial extends ForgeRegistryEntry<SpartanMaterial> {
    public final WeaponMaterial material;
    public final Set<RegistryObject<WeaponTrait>> traits;
    public final Set<Pair<Lazy<Enchantment>, Integer>> enchantments;

    public SpartanMaterial(WeaponMaterial material, Set<RegistryObject<WeaponTrait>> traits, Set<Pair<Lazy<Enchantment>, Integer>> enchantments) {
        this.material = material;
        this.traits = traits;
        this.enchantments = enchantments;
    }

    public SpartanMaterial(String name, String modid, Tier tier,
                           TagKey<Item> repairMaterial, Set<RegistryObject<WeaponTrait>> traits, Set<Pair<Lazy<Enchantment>, Integer>> enchantments) {
        this(new WeaponMaterial(name, modid, tier, repairMaterial, ModWeaponTraitTags.create(new ResourceLocation(modid, name))), traits, enchantments);
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
