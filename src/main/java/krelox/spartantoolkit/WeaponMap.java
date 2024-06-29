package krelox.spartantoolkit;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class WeaponMap extends LinkedHashMap<Pair<SpartanMaterial, WeaponType>, RegistryObject<Item>> {
    public WeaponMap() {
        super();
    }

    public RegistryObject<Item> get(SpartanMaterial material, WeaponType type) {
        return get(Pair.of(material, type));
    }
}
