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
    private Rarity rarity = Rarity.COMMON;
    private TagKey<Item> planks = ItemTags.PLANKS;
    private TagKey<Item> stick = Tags.Items.RODS_WOODEN;
    private TagKey<Item> string = Tags.Items.STRING;
    private ItemLike bow = Items.BOW;
    private ItemLike handle = ModItems.HANDLE.get();
    private ItemLike pole = ModItems.POLE.get();

    public SpartanMaterial(String name, String modid, Tier tier,
                           TagKey<Item> repairMaterial, Set<RegistryObject<WeaponTrait>> traits, Map<Supplier<Enchantment>, Integer> enchantments) {
        super(name, modid, tier, repairMaterial, ModWeaponTraitTags.create(new ResourceLocation(modid, name)));
        this.traits = traits;
        this.enchantments = enchantments;
    }

    @SafeVarargs
    @SuppressWarnings("unused")
    public SpartanMaterial(String name, String modid, Tier tier,
                           TagKey<Item> repairMaterial, RegistryObject<WeaponTrait>... traits) {
        this(name, modid, tier, repairMaterial, Set.of(traits), Map.of());
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setRarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setPlanks(TagKey<Item> planks) {
        this.planks = planks;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setStick(TagKey<Item> stick) {
        this.stick = stick;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setString(TagKey<Item> string) {
        this.string = string;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setBow(ItemLike bow) {
        this.bow = bow;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setHandle(ItemLike handle) {
        this.handle = handle;
        return this;
    }

    @SuppressWarnings("unused")
    public SpartanMaterial setPole(ItemLike pole) {
        this.pole = pole;
        return this;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public TagKey<Item> getPlanks() {
        return planks;
    }

    public TagKey<Item> getStick() {
        return stick;
    }

    public TagKey<Item> getString() {
        return string;
    }

    public ItemLike getBow() {
        return bow;
    }

    public ItemLike getHandle() {
        return handle;
    }

    public ItemLike getPole() {
        return pole;
    }
}
