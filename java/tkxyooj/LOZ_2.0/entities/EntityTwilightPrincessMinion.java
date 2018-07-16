/*
 * Adapted from TeamTwilight's mod Twilight Forest Lich minion class.
 * This inculdes not just the mob class but the mob ai classes as well.
 */

package tkxyooj.LOZ.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTwilightPrincessMinion extends EntityTwilitBulblin {
	EntityTwilightPrincess master;

	public EntityTwilightPrincessMinion(World par1World) {
		super(par1World);
		this.master = null;
	}

	public EntityTwilightPrincessMinion(World par1World, EntityTwilightPrincess entityMidna) {
		super(par1World);
		this.master = entityMidna;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		EntityLivingBase prevTarget = getAttackTarget();

		if (super.attackEntityFrom(par1DamageSource, par2)) {
			if (par1DamageSource.getTrueSource() instanceof EntityTwilightPrincess) {
				// return to previous target but speed up
				setRevengeTarget(prevTarget);
				addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 4));
				addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 1));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onLivingUpdate() {
		// if master is dead, die.
//		if (master == null || master.isDead) {
//			this.setHealth(0);
//		}
		super.onLivingUpdate();
	}
}