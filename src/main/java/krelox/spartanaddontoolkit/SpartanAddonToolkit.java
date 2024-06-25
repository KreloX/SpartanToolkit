package krelox.spartanaddontoolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("spartanaddontoolkit")
public class SpartanAddonToolkit {
    public SpartanAddonToolkit() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
