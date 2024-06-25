package krelox.spartanaddontoolkit;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SpartanAddon {
    public final DeferredRegister<Item> weaponsRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modid());
    public final WeaponMap weapons = new WeaponMap();

    public SpartanAddon() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        for (SpartanMaterial material : getMaterials()) {
            for (WeaponType type : WeaponType.values()) {
                String name = material.material.getMaterialName() + "_" + type.toString().toLowerCase();
                var item = weaponsRegister.register(name, () -> type.createItem.apply(material.material, getTab()));
                weapons.put(Pair.of(material, type), item);
            }
        }

        bus.addListener(this::generateData);

        weaponsRegister.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    protected void addTranslations(LanguageProvider provider, Function<RegistryObject<?>, String> formatName) {
        provider.add("itemGroup." + modid(), ModList.get().getMods().stream().filter(mod ->
                mod.getModId().equals(modid())).findFirst().get().getDisplayName());

        weapons.values().forEach(item -> provider.add(item.get(), formatName.apply(item)));

        getMaterials().stream()
                .flatMap(material -> material.traits.stream())
                .forEach(trait -> provider.add("tooltip.%s.trait.%s".formatted(modid(), trait.get().getType()), formatName.apply(trait)));
    }

    protected void registerModels(ItemModelProvider provider, ModelGenerator generator) {
        weapons.forEach((key, item) -> key.second().createModel.apply(generator, item.get()));
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        weapons.forEach((key, item) -> key.second().recipe.accept(weapons, consumer, key.first()));
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
                weapons.forEach((key, item) -> tag(key.second().tag).add(item.get()));
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
}
