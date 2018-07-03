/*
 * Adapted from Vazkii's Botania mod
 * Entity is the same as the magical landmine during the guardian of gaia battle
 */

package tkxyooj.LOZ.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import javax.annotation.Nonnull;

import java.util.List;

public class EntityTwilightLandmine extends Entity {

	public EntityTwilightPrincess summoner;

	public EntityTwilightLandmine(World world) {
		super(world);
		setSize(0F, 0F);
	}

	@Override
	public void onUpdate() {
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		super.onUpdate();

		float range = 2.5F;

		if(ticksExisted >= 55) {
			if(!world.isRemote) {
				List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				for(EntityPlayer player : players) {
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, summoner), 10);
					player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 25, 0));
					PotionEffect wither = new PotionEffect(MobEffects.WITHER, 120, 2);
					wither.getCurativeItems().clear();
					player.addPotionEffect(wither);
				}
			}

			setDead();
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound var1) {
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound var1) {
	}
}
