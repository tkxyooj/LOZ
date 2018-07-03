package tkxyooj.LOZ.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.client.render.entity.RenderTwilightPrincess;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincess;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerRenderers(LOZ instance) {
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		initRenderers();
		}
	
	private void initRenderers() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTwilightPrincess.class, RenderTwilightPrincess::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntitySpark.class, RenderSpark::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityCorporeaSpark.class, RenderCorporeaSpark::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityPoolMinecart.class, RenderPoolMinecart::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityPinkWither.class, RenderPinkWither::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityManaStorm.class, RenderManaStorm::new);
//		RenderingRegistry.registerEntityRenderingHandler(EntityBabylonWeapon.class, RenderBabylonWeapon::new);

	}
}
