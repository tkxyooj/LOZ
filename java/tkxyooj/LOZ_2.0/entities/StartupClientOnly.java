package tkxyooj.LOZ.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tkxyooj.LOZ.entities.client.render.entity.*;
import tkxyooj.LOZ.entities.projectiles.*;

public class StartupClientOnly {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void preInitClientOnly() 
	{
		//Rendering Registry
		RenderingRegistry.registerEntityRenderingHandler(EntityTwilightPrincess.class, RenderTwilightPrincess::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTwilightPrincessMinion.class, RenderTwilightPrincessMinion::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityStaticFairy.class, m -> new RenderSnowball<>(m,tkxyooj.LOZ.items.StartupCommon.staticfairy,Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBombFairy.class, m -> new RenderSnowball<>(m,tkxyooj.LOZ.items.StartupCommon.bombfairy,Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBeastGanon.class, new RenderEntityBeastGanon.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPhantomGanon.class, RenderEntityPhantomGanon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPuppetPrincess.class, RenderEntityPuppetPrincess::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGanonMinion.class, RenderEntityGanonMinion::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpike.class, m -> new RenderSnowball<>(m,tkxyooj.LOZ.items.StartupCommon.spike,Minecraft.getMinecraft().getRenderItem()));	
	}

	public static void postInitClientOnly() {
		// TODO Auto-generated method stub
		
	}

	public static void initClientOnly() {
		// TODO Auto-generated method stub
		
	}

}
