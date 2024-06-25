package krelox.spartanaddontoolkit;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

public class WeaponMap extends HashMap<Pair<SpartanMaterial, WeaponType>, RegistryObject<Item>> {
    public WeaponMap() {
        super();
    }
}
