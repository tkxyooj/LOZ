/**
    Copyright (C) <2015> <coolAlias>
    This file is part of coolAlias' Zelda Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package zeldaswordskills.entity.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * 
 * Abstract class that provides constructor for throwing entity as a mob
 *
 */
public abstract class EntityMobThrowable extends EntityThrowable implements IEntityAdditionalSpawnData
{
	/** The throwing entity's ID, in case it is not a player. Only used after loading from NBT */
	private int throwerId;

	/** Usually the damage this entity will cause upon impact */
	private float damage;

	/** Projectile gravity velocity */
	private float gravity = 0.03F;

	public EntityMobThrowable(World world) {
		super(world);
	}

	public EntityMobThrowable(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntityMobThrowable(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	/**
	 * Constructs a throwable entity heading towards target's initial position with given velocity, with possible abnormal trajectory;
	 * @param inaccuracy amount of deviation from base trajectory, used by Skeletons and the like; set to 0.0F for no x/z deviation
	 */
	public EntityMobThrowable(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy) {
		super(world, shooter);
		this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
		double d0 = target.posX - shooter.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - this.posY;
		double d2 = target.posZ - shooter.posZ;
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		if (d3 >= 1.0E-7D) {
			float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
			float f4 = (float) d3 * 0.2F;
			setThrowableHeading(d0, d1 + (double) f4, d2, velocity, inaccuracy);
		}
	}

	@Override
	public EntityLivingBase getThrower() {
		EntityLivingBase thrower = super.getThrower();
		if (thrower == null) {
			Entity entity = worldObj.getEntityByID(throwerId);
			return (entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null);
		}
		return thrower;
	}

	/** Returns the amount of damage this entity will cause upon impact */
	public float getDamage() {
		return damage;
	}

	/**
	 * Sets the damage this entity will cause upon impact
	 */
	public EntityMobThrowable setDamage(float amount) {
		this.damage = amount;
		return this;
	}

	/**
	 * Re-sets the projectile's heading on the exact same trajectory but using the given velocity
	 */
	public EntityMobThrowable setProjectileVelocity(float velocity) {
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity, 0.0F);
		return this;
	}

	@Override
	protected float getGravityVelocity() {
		return gravity;
	}

	/**
	 * Sets the projectile's gravity velocity
	 */
	public EntityMobThrowable setGravityVelocity(float amount) {
		this.gravity = amount;
		return this;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if ((!gravityCheck() || posY > 255.0F) && !worldObj.isRemote) {
			setDead();
		}
	}

	/**
	 * Sanity check for gravity - return true if the entity can stay alive.
	 * Note that it will be killed anyway once it surpasses y=255.
	 */
	protected boolean gravityCheck() {
		return getGravityVelocity() > 0.0F || ticksExisted < 60;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("throwerId", (getThrower() == null ? -1 : getThrower().getEntityId()));
		compound.setFloat("damage", damage);
		compound.setFloat("gravity", gravity);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		throwerId = compound.getInteger("throwerId");
		damage = compound.getFloat("damage");
		gravity = compound.getFloat("gravity");
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(gravity);
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		gravity = buffer.readFloat();
	}
}
