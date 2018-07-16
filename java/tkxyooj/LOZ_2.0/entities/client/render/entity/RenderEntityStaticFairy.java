package tkxyooj.LOZ.entities.client.render.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import tkxyooj.LOZ.entities.projectiles.EntityStaticFairy;
import tkxyooj.LOZ.items.StartupCommon;

public class RenderEntityStaticFairy extends RenderSnowball<EntityStaticFairy>
{	
	public RenderEntityStaticFairy(RenderManager renderManager,RenderItem itemRenderer) 
	{
		super(renderManager, StartupCommon.staticfairy, itemRenderer);
	}
}
