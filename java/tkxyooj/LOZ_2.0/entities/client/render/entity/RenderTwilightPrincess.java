/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 12, 2014, 4:07:26 PM (GMT)]
 */
package tkxyooj.LOZ.entities.client.render.entity;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.entities.EntityTwilightPrincess;

import javax.annotation.Nonnull;

public class RenderTwilightPrincess extends RenderBiped<EntityTwilightPrincess> {

	public static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	public static final float DEFAULT_DISFIGURATION = 0.025F;

	public static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	public static float disfiguration = DEFAULT_DISFIGURATION;
	
	private static final ResourceLocation skin = new ResourceLocation(LOZ.MODID,"textures/entities/twilight_princess.png");

	public RenderTwilightPrincess(RenderManager renderManager) {
		super(renderManager, new ModelPlayer(0.0F, false), 0F);
	}

	@Override
	public void doRender(@Nonnull EntityTwilightPrincess midna, double par2, double par4, double par6, float par8, float par9) {
		int invulTime = midna.getInvulTime();
		if(invulTime > 0) {
			grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
			disfiguration = grainIntensity * 0.3F;
		} else {
			disfiguration = (0.025F + midna.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
			grainIntensity = 0.05F + midna.hurtTime * ((1F - 0.15F) / 10F);
		}

		super.doRender(midna, par2, par4, par6, par8, par9);
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityTwilightPrincess entity) {
		return skin;
	}

}
