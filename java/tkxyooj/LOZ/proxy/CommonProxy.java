package tkxyooj.LOZ.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tkxyooj.LOZ.LOZ;

public class CommonProxy{

	public void registerRenderers(LOZ instance) {
	}

	public void preInit(FMLPreInitializationEvent event) {
		registerRenderers(null);
	}
}
