package tkxyooj.LOZ.entities.client.render.entity;

import javax.annotation.Nonnull;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.entities.EntityGanonMinion;

public class RenderEntityGanonMinion extends RenderBiped<EntityGanonMinion> 
{
	private static final ResourceLocation skin = new ResourceLocation(LOZ.MODID,"textures/entities/bulblins.png");
	
	public RenderEntityGanonMinion(RenderManager renderManager) {
		super(renderManager, new ModelPlayer(0.0F, false), 0.5F);
	}

	@Override
	public void doRender(@Nonnull EntityGanonMinion minion, double par2, double par4, double par6, float par8, float par9) 
	{
		super.doRender(minion, par2, par4, par6, par8, par9);
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityGanonMinion entity) {
		return skin;
	}

}
