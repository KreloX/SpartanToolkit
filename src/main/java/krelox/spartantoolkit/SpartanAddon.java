package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponTraits;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.data.ModWeaponTraitTagsProvider;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class SpartanAddon {
    protected final List<SpartanMaterial> materials = getMaterials();

    protected final void registerSpartanWeapons(DeferredRegister<Item> items) {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        for (WeaponType type : WeaponType.values()) {
            for (SpartanMaterial material : materials) {
                if (getBlacklist().contains(Pair.of(material, type))) continue;
                registerSpartanWeapon(items, material, type);
            }
        }
        bus.addListener(this::gatherData);
    }

    protected final void registerSpartanWeapon(DeferredRegister<Item> items, SpartanMaterial material, WeaponType type) {
        String name = material.getMaterialName() + "_" + type.toString().toLowerCase();
        var item = items.register(name, () -> type.createItem.apply(material, getTab()));
        getWeaponMap().put(material, type, item);
    }

    public static CreativeModeTab tab(String label, Supplier<Item> icon) {
        return new CreativeModeTab(label) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(icon.get());
            }
        };
    }

    public static DeferredRegister<Item> itemRegister(String modid) {
        return DeferredRegister.create(ForgeRegistries.ITEMS, modid);
    }

    public static DeferredRegister<WeaponTrait> traitRegister(String modid) {
        return DeferredRegister.create(WeaponTraits.REGISTRY_KEY, modid);
    }

    public static RegistryObject<WeaponTrait> registerTrait(DeferredRegister<WeaponTrait> traitRegister, WeaponTrait trait) {
        return traitRegister.register(trait.getType(), () -> trait);
    }

    protected static TriggerInstance has(ItemLike itemLike) {
        return TriggerInstance.hasItems(ItemPredicate.Builder.item().of(itemLike).build());
    }

    protected static TriggerInstance has(TagKey<Item> tag) {
        return TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    protected void addTranslations(LanguageProvider provider, Function<RegistryObject<?>, String> formatName) {
        ModList.get().getModContainerById(modid()).ifPresent(mod ->
                provider.add("itemGroup." + modid(), mod.getModInfo().getDisplayName()));

        getWeaponMap().values().forEach(item -> provider.add(item.get(), formatName.apply(item)));

        getTraitDescriptions().forEach((trait, description) -> {
            provider.add("tooltip.%s.trait.%s".formatted(modid(), trait.get().getType()), formatName.apply(trait));
            provider.add("tooltip.%s.trait.%s.desc".formatted(modid(), trait.get().getType()), description);
        });
    }

    protected void registerModels(ItemModelProvider provider, ModelGenerator generator) {
        getWeaponMap().forEach((key, item) -> key.second().createModel.apply(generator, item.get()));
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        getWeaponMap().forEach((key, item) -> key.second().recipe.accept(getWeaponMap(), consumer, key.first()));
    }

    public void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var fileHelper = event.getExistingFileHelper();

        Consumer<DataProvider> client = provider -> generator.addProvider(event.includeClient(), provider);
        Consumer<DataProvider> server = provider -> generator.addProvider(event.includeServer(), provider);

        client.accept(new LanguageProvider(generator, modid(), "en_us") {
            private static final Set<String> ROMAN_NUMERALS = Set.of("i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x");

            @Override
            protected void addTranslations() {
                SpartanAddon.this.addTranslations(this, registryObject ->
                        Arrays.stream(registryObject.getId().getPath().replace("_heavy", "-Strengthened_heavy").split("_"))
                                .map(name -> ROMAN_NUMERALS.contains(name) ? name.toUpperCase() : name.substring(0, 1).toUpperCase() + name.substring(1))
                                .collect(Collectors.joining(" ")));
            }
        });
        client.accept(new ItemModelProvider(generator, modid(), fileHelper) {
            @Override
            protected void registerModels() {
                SpartanAddon.this.registerModels(this, new ModelGenerator(this));
            }
        });

        server.accept(new ModWeaponTraitTagsProvider(generator, fileHelper) {
            @Override
            protected void addTags() {
                materials.forEach(material -> tag(material.getTraitsTag()).add(material.traits.stream().map(RegistryObject::get).toArray(WeaponTrait[]::new)));
            }
        });
        server.accept(new ItemTagsProvider(generator, new BlockTagsProvider(generator, modid(), fileHelper), modid(), fileHelper) {
            @Override
            protected void addTags() {
                getWeaponMap().forEach((key, item) -> tag(key.second().tag).add(item.get()));
            }
        });
        server.accept(new RecipeProvider(generator) {
            @Override
            protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
                SpartanAddon.this.buildCraftingRecipes(consumer);
            }
        });
    }

    protected Set<Pair<SpartanMaterial, WeaponType>> getBlacklist() {
        return Set.of();
    }

    protected Map<RegistryObject<WeaponTrait>, String> getTraitDescriptions() {
        return new HashMap<>();
    }

    public abstract String modid();

    public abstract List<SpartanMaterial> getMaterials();

    public abstract CreativeModeTab getTab();

    public abstract WeaponMap getWeaponMap();
}
