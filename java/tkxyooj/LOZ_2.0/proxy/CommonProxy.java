package tkxyooj.LOZ.proxy;

import net.minecraft.entity.player.EntityPlayer;

/**
 *	CommonProxy is used to set up the mod and start it running.  It contains all the code that should run on both the
 *  Standalone client and the dedicated server.
 */
public abstract class CommonProxy {

  /**
   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
   */
  public void preInit()
  {
	  //read config first
	  tkxyooj.LOZ.config.StartupCommon.preInitCommon();

	  tkxyooj.LOZ.blocks.StartupCommon.preInitCommon();
	  tkxyooj.LOZ.items.StartupCommon.preInitCommon();
	  tkxyooj.LOZ.entities.StartupCommon.preInitCommon();
	  tkxyooj.LOZ.recipes.StartupCommon.preInitCommon();
	  tkxyooj.LOZ.sounds.StartupCommon.preInitCommon();
  }

  /**
   * Do your mod setup. Build whatever data structures you care about. Register recipes,
   * send FMLInterModComms messages to other mods.
   */
  public void init()
  {
	  tkxyooj.LOZ.config.StartupCommon.initCommon();
	  
	  tkxyooj.LOZ.blocks.StartupCommon.initCommon();
	  tkxyooj.LOZ.items.StartupCommon.initCommon();
	  tkxyooj.LOZ.entities.StartupCommon.initCommon();
	  tkxyooj.LOZ.recipes.StartupCommon.initCommon();
  }

  /**
   * Handle interaction with other mods, complete your setup based on this.
   */
  public void postInit()
  {
	  tkxyooj.LOZ.config.StartupCommon.postInitCommon();
	  
	  tkxyooj.LOZ.blocks.StartupCommon.postInitCommon();
	  tkxyooj.LOZ.items.StartupCommon.postInitCommon();
	  tkxyooj.LOZ.entities.StartupCommon.postInitCommon();
	  tkxyooj.LOZ.recipes.StartupCommon.postInitCommon();
  }

  // helper to determine whether the given player is in creative mode
  //  not necessary for most examples
  abstract public boolean playerIsInCreativeMode(EntityPlayer player);

  /**
   * is this a dedicated server?
   * @return true if this is a dedicated server, false otherwise
   */
  abstract public boolean isDedicatedServer();
}
