package tkxyooj.LOZ.entities.client.render.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import tkxyooj.LOZ.entities.projectiles.EntitySpike;
import tkxyooj.LOZ.items.StartupCommon;

public class RenderEntitySpike extends RenderSnowball<EntitySpike>
{
	public RenderEntitySpike(RenderManager renderManager,RenderItem itemRenderer) 
	{
		super(renderManager, StartupCommon.spike, itemRenderer);
	}
}
