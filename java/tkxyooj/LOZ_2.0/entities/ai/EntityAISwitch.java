package tkxyooj.LOZ.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import tkxyooj.LOZ.entities.EntityTwilitBulblin;

public class EntityAISwitch extends EntityAIAttackMelee
{
	private final EntitySkeleton entity;
	private final double moveSpeedAmp;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public EntityAISwitch(EntityTwilitBulblin twilitbulblin, double speedAmplifier, int delay, float maxDistance)
	{
		super(twilitbulblin, speedAmplifier, false);
		this.entity = twilitbulblin;
		this.moveSpeedAmp = speedAmplifier;
		this.attackCooldown = delay;
		this.maxAttackDistance = maxDistance * maxDistance;;
	}

	public void setAttackCooldown(int cooldown)
	{
		this.attackCooldown = cooldown;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		return this.entity.getAttackTarget() != null;
	}

	protected boolean isBowInMainhand()
	{
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemBow;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand() || super.shouldContinueExecuting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		super.startExecuting();
		this.entity.setSwingingArms(true);
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		super.resetTask();
		this.entity.setSwingingArms(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

		if (entitylivingbase != null) { 
			if(this.isBowInMainhand()) {
				double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
				boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
				boolean flag1 = this.seeTime > 0;

				if (flag != flag1)
				{
					this.seeTime = 0;
				}

				if (flag)
				{
					++this.seeTime;
				}
				else
				{
					--this.seeTime;
				}

				if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
				{
					this.entity.getNavigator().clearPath();
					++this.strafingTime;
				}
				else
				{
					this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
					this.strafingTime = -1;
				}

				if (this.strafingTime >= 20)
				{
					if ((double)this.entity.getRNG().nextFloat() < 0.3D)
					{
						this.strafingClockwise = !this.strafingClockwise;
					}

					if ((double)this.entity.getRNG().nextFloat() < 0.3D)
					{
						this.strafingBackwards = !this.strafingBackwards;
					}

					this.strafingTime = 0;
				}

				if (this.strafingTime > -1)
				{
					if (d0 > (double)(this.maxAttackDistance * 0.75F))
					{
						this.strafingBackwards = false;
					}
					else if (d0 < (double)(this.maxAttackDistance * 0.25F))
					{
						this.strafingBackwards = true;
					}

					this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
					this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
				}
				else
				{
					this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
				}

				if (this.entity.isHandActive())
				{
					if (!flag && this.seeTime < -60)
					{
						this.entity.resetActiveHand();
					}
					else if (flag)
					{
						int i = this.entity.getItemInUseMaxCount();

						if (i >= 20)
						{
							this.entity.resetActiveHand();
							this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
							this.attackTime = this.attackCooldown;
						}
					}
				}
				else if (--this.attackTime <= 0 && this.seeTime >= -60)
				{
					this.entity.setActiveHand(EnumHand.MAIN_HAND);
				}
			}
			else
			{
				this.entity.setMoveStrafing(0);
				super.updateTask();
			}
		}
		
	}
}