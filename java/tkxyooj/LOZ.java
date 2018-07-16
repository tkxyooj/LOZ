package tkxyooj.LOZ;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tkxyooj.LOZ.proxy.CommonProxy;

@Mod(modid = LOZ.MODID, version = LOZ.VERSION)
public class LOZ
{
	// you also need to update the modid and version in two other places as well:
	// build.gradle file (the version, group, and archivesBaseName parameters)
	// resources/mcmod.info (the name, description, and version parameters)
	public static final String MODID = "lozmod";
    public static final String VERSION = "1.12.2_2.0";

    // The instance of your mod that Forge uses.  Optional.
    @Mod.Instance(LOZ.MODID)
    public static LOZ instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="tkxyooj.LOZ.proxy.ClientOnlyProxy", serverSide="tkxyooj.LOZ.proxy.DedicatedServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
      proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
      proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
      proxy.postInit();
    }

    /**
     * Prepend the name with the mod ID, suitable for ResourceLocations such as textures.
     * @param name
     * @return eg "minecraftbyexample:myblockname"
     */
    public static String prependModID(String name) {return MODID + ":" + name;}
}
