package krelox.spartantoolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("spartantoolkit")
public class SpartanToolkit {
    public SpartanToolkit() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
