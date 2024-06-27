package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.WeaponTraits;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import com.oblivioussp.spartanweaponry.data.ModWeaponTraitTagsProvider;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SpartanAddon {
    protected final HashMap<RegistryObject<WeaponTrait>, String> traitDescriptions = new HashMap<>();

    protected final void registerSpartanWeapons(DeferredRegister<Item> itemRegister) {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        for (SpartanMaterial material : getMaterials()) {
            for (WeaponType type : WeaponType.values()) {
                String name = material.material.getMaterialName() + "_" + type.toString().toLowerCase();
                var item = itemRegister.register(name, () -> type.createItem.apply(material.material, getTab()));
                getWeaponMap().put(Pair.of(material, type), item);
            }
        }
        bus.addListener(this::generateData);
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

    protected void addTranslations(LanguageProvider provider, Function<RegistryObject<?>, String> formatName) {
        provider.add("itemGroup." + modid(), ModList.get().getMods().stream().filter(mod ->
                mod.getModId().equals(modid())).findFirst().get().getDisplayName());

        getWeaponMap().values().forEach(item -> provider.add(item.get(), formatName.apply(item)));

        traitDescriptions.forEach((trait, description) -> {
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

    public void generateData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var fileHelper = event.getExistingFileHelper();

        generator.addProvider(new LanguageProvider(generator, modid(), "en_us") {
            @Override
            protected void addTranslations() {
                SpartanAddon.this.addTranslations(this, registryObject ->
                        Arrays.stream(registryObject.getId().getPath().replace("_heavy", "-Strengthened_heavy").split("_"))
                                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                                .collect(Collectors.joining(" ")));
            }
        });
        generator.addProvider(new ModWeaponTraitTagsProvider(generator, fileHelper) {
            @Override
            protected void addTags() {
                getMaterials().forEach(material -> tag(material.material.getTraitsTag()).add(material.traits.stream().map(RegistryObject::get).toArray(WeaponTrait[]::new)));
            }
        });
        generator.addProvider(new ItemTagsProvider(generator, new BlockTagsProvider(generator, modid(), fileHelper), modid(), fileHelper) {
            @Override
            protected void addTags() {
                getWeaponMap().forEach((key, item) -> tag(key.second().tag).add(item.get()));
            }
        });
        generator.addProvider(new ItemModelProvider(generator, modid(), fileHelper) {
            @Override
            protected void registerModels() {
                SpartanAddon.this.registerModels(this, new ModelGenerator(this));
            }
        });
        generator.addProvider(new RecipeProvider(generator) {
            @Override
            protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
                SpartanAddon.this.buildCraftingRecipes(consumer);
            }
        });
    }

    public abstract String modid();

    public abstract Set<SpartanMaterial> getMaterials();

    public abstract CreativeModeTab getTab();

    public abstract WeaponMap getWeaponMap();
}
