/*
 * Adapted from TeamTwilight's mod Twilight Forest Lich Bolt class
 */

package tkxyooj.LOZ.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityStaticFairy extends EntityThrowable {

	public EntityStaticFairy(World par1World) {
		super(par1World);
	}

	public EntityStaticFairy(World par1World, EntityLivingBase par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionX;
			double sy = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionY;
			double sz = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionZ;


			double dx = posX + sx;
			double dy = posY + sy;
			double dz = posZ + sz;

			world.spawnParticle(EnumParticleTypes.SPIT, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
		}
	}

	@Override
	public boolean isBurning() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		super.attackEntityFrom(damagesource, i);

		if (damagesource.getImmediateSource() != null) {
			if (!damagesource.isExplosion())
				shock();
			return true;
		} else {
			return false;
		}
	}

	private void shock() {
		if (!this.world.isRemote) 
		{
			this.world.spawnEntity(new EntityLightningBolt(world, this.posX, this.posY, this.posZ, false));
			this.setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	public void setThrowableHeading(double x, double y, double z, float speed, float accuracy) {
		// save velocity
		double velX = this.motionX;
		double velY = this.motionY;
		double velZ = this.motionZ;
		x = velX;
		y = velY;
		z = velZ;
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit instanceof EntityStaticFairy
				|| result.entityHit instanceof EntityBombFairy) {
			return;
		}

		shock();
	}
}
