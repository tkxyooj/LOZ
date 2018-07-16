package tkxyooj.LOZ.entities.client.render.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import tkxyooj.LOZ.entities.projectiles.EntityBombFairy;
import tkxyooj.LOZ.items.StartupCommon;

public class RenderEntityBombFairy extends RenderSnowball<EntityBombFairy>
{
	public RenderEntityBombFairy(RenderManager renderManager,RenderItem itemRenderer) 
	{
				super(renderManager, StartupCommon.staticfairy, itemRenderer);
	}
}
