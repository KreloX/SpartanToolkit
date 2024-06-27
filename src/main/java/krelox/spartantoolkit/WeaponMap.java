package krelox.spartantoolkit;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class WeaponMap extends LinkedHashMap<Pair<SpartanMaterial, WeaponType>, RegistryObject<Item>> {
    public WeaponMap() {
        super();
    }
}
