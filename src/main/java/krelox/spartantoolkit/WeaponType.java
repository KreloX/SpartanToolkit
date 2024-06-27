package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.SpartanWeaponryAPI;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import com.oblivioussp.spartanweaponry.api.data.recipe.RecipeProviderHelper;
import com.oblivioussp.spartanweaponry.api.tags.ModItemTags;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public enum WeaponType {
    DAGGER(ModItemTags.DAGGERS, SpartanWeaponryAPI::createDagger, ModelGenerator::createDaggerModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeDagger(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("DAGGER"))).get(), "has_item")),
    PARRYING_DAGGER(ModItemTags.PARRYING_DAGGERS, SpartanWeaponryAPI::createParryingDagger, ModelGenerator::createParryingDaggerModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeParryingDagger(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("PARRYING_DAGGER"))).get(), "has_item")),
    LONGSWORD(ModItemTags.LONGSWORDS, SpartanWeaponryAPI::createLongsword, ModelGenerator::createLongswordModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeLongsword(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("LONGSWORD"))).get(), "has_item")),
    KATANA(ModItemTags.KATANAS, SpartanWeaponryAPI::createKatana, ModelGenerator::createKatanaModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeKatana(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("KATANA"))).get(), "has_item")),
    SABER(ModItemTags.SABERS, SpartanWeaponryAPI::createSaber, ModelGenerator::createSaberModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeSaber(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("SABER"))).get(), "has_item")),
    RAPIER(ModItemTags.RAPIERS, SpartanWeaponryAPI::createRapier, ModelGenerator::createRapierModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeRapier(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("RAPIER"))).get(), "has_item")),
    GREATSWORD(ModItemTags.GREATSWORDS, SpartanWeaponryAPI::createGreatsword, ModelGenerator::createGreatswordModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeGreatsword(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("GREATSWORD"))).get(), "has_item")),
    BATTLE_HAMMER(ModItemTags.BATTLE_HAMMERS, SpartanWeaponryAPI::createBattleHammer, ModelGenerator::createBattleHammerModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeBattleHammer(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("BATTLE_HAMMER"))).get(), "has_item")),
    WARHAMMER(ModItemTags.WARHAMMERS, SpartanWeaponryAPI::createWarhammer, ModelGenerator::createWarhammerModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeWarhammer(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("WARHAMMER"))).get(), "has_item")),
    SPEAR(ModItemTags.SPEARS, SpartanWeaponryAPI::createSpear, ModelGenerator::createSpearModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeSpear(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("SPEAR"))).get(), "has_item")),
    HALBERD(ModItemTags.HALBERDS, SpartanWeaponryAPI::createHalberd, ModelGenerator::createHalberdModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeHalberd(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("HALBERD"))).get(), "has_item")),
    PIKE(ModItemTags.PIKES, SpartanWeaponryAPI::createPike, ModelGenerator::createPikeModel, (map, consumer, material) ->
            RecipeProviderHelper.recipePike(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("PIKE"))).get(), "has_item")),
    LANCE(ModItemTags.LANCES, SpartanWeaponryAPI::createLance, ModelGenerator::createLanceModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeLance(consumer, material.getHandle(), material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("LANCE"))).get(), "has_item")),
    LONGBOW(ModItemTags.LONGBOWS, SpartanWeaponryAPI::createLongbow, ModelGenerator::createLongbowModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeLongbow(consumer, material.getStick(), material.getString(), material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("LONGBOW"))).get(), "has_item")),
    HEAVY_CROSSBOW(ModItemTags.HEAVY_CROSSBOWS, SpartanWeaponryAPI::createHeavyCrossbow, ModelGenerator::createHeavyCrossbowModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeHeavyCrossbow(consumer, material.getPlanks(), material.getBow(), material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("HEAVY_CROSSBOW"))).get(), "has_item")),
    THROWING_KNIFE(ModItemTags.THROWING_KNIVES, SpartanWeaponryAPI::createThrowingKnife, ModelGenerator::createThrowingKnifeModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeThrowingKnife(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("THROWING_KNIFE"))).get(), "has_item")),
    TOMAHAWK(ModItemTags.TOMAHAWKS, SpartanWeaponryAPI::createTomahawk, ModelGenerator::createTomahawkModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeTomahawk(consumer, material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("TOMAHAWK"))).get(), "has_item")),
    JAVELIN(ModItemTags.JAVELINS, SpartanWeaponryAPI::createJavelin, ModelGenerator::createJavelinModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeJavelin(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("JAVELIN"))).get(), "has_item")),
    BOOMERANG(ModItemTags.BOOMERANGS, SpartanWeaponryAPI::createBoomerang, ModelGenerator::createBoomerangModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeBoomerang(consumer, material.getPlanks(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("BOOMERANG"))).get(), "has_item")),
    BATTLEAXE(ModItemTags.BATTLEAXES, SpartanWeaponryAPI::createBattleaxe, ModelGenerator::createBattleaxeModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeBattleaxe(consumer, material.getStick(), material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("BATTLEAXE"))).get(), "has_item")),
    FLANGED_MACE(ModItemTags.FLANGED_MACES, SpartanWeaponryAPI::createFlangedMace, ModelGenerator::createFlangedMaceModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeFlangedMace(consumer, material.getStick(), material.getHandle(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("FLANGED_MACE"))).get(), "has_item")),
    GLAIVE(ModItemTags.GLAIVES, SpartanWeaponryAPI::createGlaive, ModelGenerator::createGlaiveModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeGlaive(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("GLAIVE"))).get(), "has_item")),
    QUARTERSTAFF(ModItemTags.QUARTERSTAVES, SpartanWeaponryAPI::createQuarterstaff, ModelGenerator::createQuarterstaffModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeQuarterstaff(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("QUARTERSTAFF"))).get(), "has_item")),
    SCYTHE(ModItemTags.SCYTHES, SpartanWeaponryAPI::createScythe, ModelGenerator::createScytheModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeScythe(consumer, material.getPole(), material.material.getRepairTag(), map.get(Pair.of(material, valueOf("SCYTHE"))).get(), "has_item"));

    public final TagKey<Item> tag;
    public final BiFunction<WeaponMaterial, CreativeModeTab, Item> createItem;
    public final BiFunction<ModelGenerator, Item, ResourceLocation> createModel;
    public final TriConsumer<Map<Pair<SpartanMaterial, WeaponType>, RegistryObject<Item>>, Consumer<FinishedRecipe>, SpartanMaterial> recipe;

    WeaponType(TagKey<Item> tag, BiFunction<WeaponMaterial, CreativeModeTab, Item> createItem, BiFunction<ModelGenerator, Item, ResourceLocation> createModel, TriConsumer<Map<Pair<SpartanMaterial, WeaponType>, RegistryObject<Item>>, Consumer<FinishedRecipe>, SpartanMaterial> recipe) {
        this.tag = tag;
        this.createItem = createItem;
        this.createModel = createModel;
        this.recipe = recipe;
    }
}
