package tkxyooj.LOZ.entities.client.render.entity;

import javax.annotation.Nonnull;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.entities.EntityBeastGanon;

public class RenderEntityBeastGanon <T extends EntityBeastGanon> extends RenderLiving<T> 
{

	private static final ResourceLocation skin = new ResourceLocation(LOZ.MODID,"textures/entities/beast_ganon.png");
	
	public RenderEntityBeastGanon(RenderManager renderManager) 
	{
		super(renderManager, new ModelPig(), 2.0F);
	}

//	@Override
//	public void doRender(@Nonnull EntityPig beastganon, double par2, double par4, double par6, float par8, float par9) {
//		super.doRender(beastganon, par2, par4, par6, par8, par9);
//	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(T entity) 
	{
		return skin;
	}
	
	public static class Factory<T extends EntityBeastGanon> implements IRenderFactory <T>
	{
		@Override
		public Render<? super T> createRenderFor(RenderManager manager) 
		{
			return new RenderEntityBeastGanon(manager);
		}	
	}
}
