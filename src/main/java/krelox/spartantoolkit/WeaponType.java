package krelox.spartantoolkit;

import com.oblivioussp.spartanweaponry.api.SpartanWeaponryAPI;
import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import com.oblivioussp.spartanweaponry.api.data.recipe.RecipeProviderHelper;
import com.oblivioussp.spartanweaponry.api.tags.ModItemTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public enum WeaponType {
    DAGGER(ModItemTags.DAGGERS, SpartanWeaponryAPI::createDagger, ModelGenerator::createDaggerModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeDagger(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("DAGGER")).get(), "has_" + material.getMaterialName())),
    PARRYING_DAGGER(ModItemTags.PARRYING_DAGGERS, SpartanWeaponryAPI::createParryingDagger, ModelGenerator::createParryingDaggerModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeParryingDagger(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("PARRYING_DAGGER")).get(), "has_" + material.getMaterialName())),
    LONGSWORD(ModItemTags.LONGSWORDS, SpartanWeaponryAPI::createLongsword, ModelGenerator::createLongswordModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeLongsword(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("LONGSWORD")).get(), "has_" + material.getMaterialName())),
    KATANA(ModItemTags.KATANAS, SpartanWeaponryAPI::createKatana, ModelGenerator::createKatanaModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeKatana(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("KATANA")).get(), "has_" + material.getMaterialName())),
    SABER(ModItemTags.SABERS, SpartanWeaponryAPI::createSaber, ModelGenerator::createSaberModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeSaber(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("SABER")).get(), "has_" + material.getMaterialName())),
    RAPIER(ModItemTags.RAPIERS, SpartanWeaponryAPI::createRapier, ModelGenerator::createRapierModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeRapier(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("RAPIER")).get(), "has_" + material.getMaterialName())),
    GREATSWORD(ModItemTags.GREATSWORDS, SpartanWeaponryAPI::createGreatsword, ModelGenerator::createGreatswordModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeGreatsword(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("GREATSWORD")).get(), "has_" + material.getMaterialName())),
    BATTLE_HAMMER(ModItemTags.BATTLE_HAMMERS, SpartanWeaponryAPI::createBattleHammer, ModelGenerator::createBattleHammerModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeBattleHammer(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("BATTLE_HAMMER")).get(), "has_" + material.getMaterialName())),
    WARHAMMER(ModItemTags.WARHAMMERS, SpartanWeaponryAPI::createWarhammer, ModelGenerator::createWarhammerModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeWarhammer(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("WARHAMMER")).get(), "has_" + material.getMaterialName())),
    SPEAR(ModItemTags.SPEARS, SpartanWeaponryAPI::createSpear, ModelGenerator::createSpearModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeSpear(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("SPEAR")).get(), "has_" + material.getMaterialName())),
    HALBERD(ModItemTags.HALBERDS, SpartanWeaponryAPI::createHalberd, ModelGenerator::createHalberdModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeHalberd(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("HALBERD")).get(), "has_" + material.getMaterialName())),
    PIKE(ModItemTags.PIKES, SpartanWeaponryAPI::createPike, ModelGenerator::createPikeModel, (map, consumer, material) ->
            RecipeProviderHelper.recipePike(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("PIKE")).get(), "has_" + material.getMaterialName())),
    LANCE(ModItemTags.LANCES, SpartanWeaponryAPI::createLance, ModelGenerator::createLanceModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeLance(consumer, material.getHandle(), material.getPole(), material.getRepairTag(), map.get(material, valueOf("LANCE")).get(), "has_" + material.getMaterialName())),
    LONGBOW(ModItemTags.LONGBOWS, SpartanWeaponryAPI::createLongbow, ModelGenerator::createLongbowModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeLongbow(consumer, material.getStick(), material.getString(), material.getHandle(), material.getRepairTag(), map.get(material, valueOf("LONGBOW")).get(), "has_" + material.getMaterialName())),
    HEAVY_CROSSBOW(ModItemTags.HEAVY_CROSSBOWS, SpartanWeaponryAPI::createHeavyCrossbow, ModelGenerator::createHeavyCrossbowModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeHeavyCrossbow(consumer, material.getPlanks(), material.getBow(), material.getHandle(), material.getRepairTag(), map.get(material, valueOf("HEAVY_CROSSBOW")).get(), "has_" + material.getMaterialName())),
    THROWING_KNIFE(ModItemTags.THROWING_KNIVES, SpartanWeaponryAPI::createThrowingKnife, ModelGenerator::createThrowingKnifeModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeThrowingKnife(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("THROWING_KNIFE")).get(), "has_" + material.getMaterialName())),
    TOMAHAWK(ModItemTags.TOMAHAWKS, SpartanWeaponryAPI::createTomahawk, ModelGenerator::createTomahawkModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeTomahawk(consumer, material.getHandle(), material.getRepairTag(), map.get(material, valueOf("TOMAHAWK")).get(), "has_" + material.getMaterialName())),
    JAVELIN(ModItemTags.JAVELINS, SpartanWeaponryAPI::createJavelin, ModelGenerator::createJavelinModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeJavelin(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("JAVELIN")).get(), "has_" + material.getMaterialName())),
    BOOMERANG(ModItemTags.BOOMERANGS, SpartanWeaponryAPI::createBoomerang, ModelGenerator::createBoomerangModels, (map, consumer, material) ->
            RecipeProviderHelper.recipeBoomerang(consumer, material.getPlanks(), material.getRepairTag(), map.get(material, valueOf("BOOMERANG")).get(), "has_" + material.getMaterialName())),
    BATTLEAXE(ModItemTags.BATTLEAXES, SpartanWeaponryAPI::createBattleaxe, ModelGenerator::createBattleaxeModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeBattleaxe(consumer, material.getStick(), material.getHandle(), material.getRepairTag(), map.get(material, valueOf("BATTLEAXE")).get(), "has_" + material.getMaterialName())),
    FLANGED_MACE(ModItemTags.FLANGED_MACES, SpartanWeaponryAPI::createFlangedMace, ModelGenerator::createFlangedMaceModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeFlangedMace(consumer, material.getStick(), material.getHandle(), material.getRepairTag(), map.get(material, valueOf("FLANGED_MACE")).get(), "has_" + material.getMaterialName())),
    GLAIVE(ModItemTags.GLAIVES, SpartanWeaponryAPI::createGlaive, ModelGenerator::createGlaiveModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeGlaive(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("GLAIVE")).get(), "has_" + material.getMaterialName())),
    QUARTERSTAFF(ModItemTags.QUARTERSTAVES, SpartanWeaponryAPI::createQuarterstaff, ModelGenerator::createQuarterstaffModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeQuarterstaff(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("QUARTERSTAFF")).get(), "has_" + material.getMaterialName())),
    SCYTHE(ModItemTags.SCYTHES, SpartanWeaponryAPI::createScythe, ModelGenerator::createScytheModel, (map, consumer, material) ->
            RecipeProviderHelper.recipeScythe(consumer, material.getPole(), material.getRepairTag(), map.get(material, valueOf("SCYTHE")).get(), "has_" + material.getMaterialName()));

    public final TagKey<Item> tag;
    public final Function<WeaponMaterial, Item> createItem;
    public final BiFunction<ModelGenerator, Item, ResourceLocation> createModel;
    public final TriConsumer<WeaponMap, Consumer<FinishedRecipe>, SpartanMaterial> recipe;

    WeaponType(TagKey<Item> tag, Function<WeaponMaterial, Item> createItem, BiFunction<ModelGenerator, Item, ResourceLocation> createModel, TriConsumer<WeaponMap, Consumer<FinishedRecipe>, SpartanMaterial> recipe) {
        this.tag = tag;
        this.createItem = createItem;
        this.createModel = createModel;
        this.recipe = recipe;
    }
}
