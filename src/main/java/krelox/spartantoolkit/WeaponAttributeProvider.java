package krelox.spartantoolkit;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class WeaponAttributeProvider implements DataProvider {
    protected static final ExistingFileHelper.ResourceType WEAPON_ATTRIBUTES = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "weapon_attributes");
    protected final PackOutput.PathProvider attributePathProvider;
    protected final Map<ResourceLocation, JsonObject> generatedAttributes = new HashMap<>();
    protected final ExistingFileHelper existingFileHelper;

    protected abstract void registerAttributes();

    protected WeaponAttributeProvider(PackOutput output, ExistingFileHelper fileHelper) {
        attributePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "weapon_attributes");
        Preconditions.checkNotNull(output);
        Preconditions.checkNotNull(fileHelper);
        existingFileHelper = fileHelper;
    }

    public void spartanAttributes(ResourceLocation location, WeaponType type) {
        if (type == WeaponType.BOOMERANG || type == WeaponType.HEAVY_CROSSBOW || type == WeaponType.LONGBOW) return;
        var attributes = new JsonObject();
        attributes.addProperty("parent", ModSpartanWeaponry.ID + ":base/" + type.name().toLowerCase(Locale.US));
        existingFileHelper.trackGenerated(location, WEAPON_ATTRIBUTES);
        generatedAttributes.put(location, attributes);
    }

    public void spartanAttributes(WeaponMap map) {
        map.forEach((pair, item) -> spartanAttributes(item.getId(), pair.second()));
    }

    @Override
    public String getName() {
        return "Weapon Attributes";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        generatedAttributes.clear();
        registerAttributes();
        return generateAll(cache);
    }

    protected CompletableFuture<Void> generateAll(CachedOutput cache) {
        var futures = new CompletableFuture[generatedAttributes.size()];
        int i = 0;

        for (var idJsonEntry : generatedAttributes.entrySet()) {
            var attributes = idJsonEntry.getValue();
            var targetPath = attributePathProvider.json(idJsonEntry.getKey());
            futures[i++] = DataProvider.saveStable(cache, attributes, targetPath);
        }

        return CompletableFuture.allOf(futures);
    }
}
