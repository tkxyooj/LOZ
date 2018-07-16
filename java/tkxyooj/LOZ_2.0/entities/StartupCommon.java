package tkxyooj.LOZ.entities;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.entities.projectiles.EntitySpike;

public class StartupCommon {

	public static void preInitCommon() 
	{
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "TwilightPrincess"), EntityTwilightPrincess.class, "Twilight Princess", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "TwilightPrincessMinion"), EntityTwilightPrincessMinion.class, "Minion", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "PhantomGanon"), EntityPhantomGanon.class, "Phantom Ganon", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "GanonMinion"), EntityGanonMinion.class, "Minions", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "BeastGanon"), EntityBeastGanon.class, "Beast Ganon", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "PuppetPrincess"), EntityPuppetPrincess.class, "Puppet Princess", id++, LOZ.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "Spike"), EntitySpike.class, "Spike", id++, LOZ.instance, 64, 3, true);
	}

	public static void initCommon() {
		// TODO Auto-generated method stub
		
	}

	public static void postInitCommon() {
		// TODO Auto-generated method stub
		
	}

}
