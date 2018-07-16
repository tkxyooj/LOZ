/*
 * This class is a copy of the Minotaur class from the Twilight Forest mod.
 * Source Code in github:
 * https://github.com/TeamTwilight/twilightforest
 * 
 */
package tkxyooj.LOZ.entities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

interface ICharger
{
	public boolean isCharging();
	public void setCharging(boolean flag);	
}

public class EntityBeastGanon extends EntityMob implements ICharger{
	private static final DataParameter<Boolean> CHARGING = EntityDataManager.createKey(EntityBeastGanon.class, DataSerializers.BOOLEAN);
	
	public EntityBeastGanon(World par1World) {
		super(par1World);
		this.setSize(3.0F, 3.0F);
		this.experienceValue = 100;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIChargeAttack(this, 2.0F, this instanceof EntityBeastGanon));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(800.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CHARGING, false);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		return data;
	}

	@Override
	public boolean isCharging() {
		return dataManager.get(CHARGING);
	}

	@Override
	public void setCharging(boolean flag) {
		dataManager.set(CHARGING, flag);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		boolean success = super.attackEntityAsMob(par1Entity);

		if (success && this.isCharging()) {
			par1Entity.motionY += 0.4;
			playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
		}

		return success;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (isCharging()) {
			this.limbSwingAmount += 0.6;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block par4) {
		playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 0.8F);
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F;
	}
	
	//Charging AI
    static class EntityAIChargeAttack extends EntityAIBase {

    	private static final double MIN_RANGE_SQ = 16.0D;
    	private static final double MAX_RANGE_SQ = 64.0D;
    	private static final int FREQ = 1;

    	private EntityCreature charger;
    	private EntityLivingBase chargeTarget;
    	private double chargeX;
    	private double chargeY;
    	private double chargeZ;

    	protected float speed;

    	private final boolean canBreak;

    	private int windup;

    	private boolean hasAttacked;

    	public EntityAIChargeAttack(EntityCreature entityLiving, float f, boolean canBreak) {
    		this.charger = entityLiving;
    		this.speed = f;
    		this.canBreak = canBreak;
    		this.windup = 0;
    		this.hasAttacked = false;
    		this.setMutexBits(3);
    	}

    	@Override
    	public boolean shouldExecute() {
    		this.chargeTarget = this.charger.getAttackTarget();

    		if (this.chargeTarget == null) {
    			return false;
    		} else {
    			double distance = this.charger.getDistanceSq(this.chargeTarget);
    			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
    				return false;
    			} else if (!this.charger.onGround) {
    				return false;
    			} else {
    				Vec3d chargePos = findChargePoint(charger, chargeTarget, 2.1);
    				boolean canSeeTargetFromDest = charger.getEntitySenses().canSee(chargeTarget);
    				if (!canSeeTargetFromDest) {
    					return false;
    				} else {
    					chargeX = chargePos.x;
    					chargeY = chargePos.y;
    					chargeZ = chargePos.z;

    					return this.charger.getRNG().nextInt(FREQ) == 0;
    				}
    			}

    		}
    	}

    	@Override
    	public void startExecuting() {
    		this.windup = 15 + this.charger.getRNG().nextInt(30);
    	}

    	@Override
    	public boolean shouldContinueExecuting() {
    		return windup > 0 || !this.charger.getNavigator().noPath();
    	}

    	@Override
    	public void updateTask() {
    		// look where we're going
    		this.charger.getLookHelper().setLookPosition(chargeX, chargeY - 1, chargeZ, 10.0F, this.charger.getVerticalFaceSpeed());

    		if (windup > 0) {
    			if (--windup == 0) {
    				// actually charge!

    				this.charger.getNavigator().tryMoveToXYZ(chargeX, chargeY, chargeZ, this.speed);
    			} else {
    				this.charger.limbSwingAmount += 0.8;

    				if (this.charger instanceof ICharger) {
    					((ICharger) charger).setCharging(true);
    				}
    			}
    		} else if (canBreak) {
    			if (!charger.world.isRemote && charger.world.getGameRules().getBoolean("mobGriefing")) {
    				AxisAlignedBB bb = charger.getEntityBoundingBox();
    				int minx = MathHelper.floor(bb.minX - 0.75D);
    				int miny = MathHelper.floor(bb.minY + 0.0D);
    				int minz = MathHelper.floor(bb.minZ - 0.75D);
    				int maxx = MathHelper.floor(bb.maxX + 0.75D);
    				int maxy = MathHelper.floor(bb.maxY + 0.15D);
    				int maxz = MathHelper.floor(bb.maxZ + 0.75D);
    				if (charger.world.isAreaLoaded(new BlockPos(minx, miny, minz), new BlockPos(maxx, maxy, maxz))) {
    					for (int dx = minx; dx <= maxx; dx++) {
    						for (int dy = miny; dy <= maxy; dy++) {
    							for (int dz = minz; dz <= maxz; dz++) {
    								BlockPos pos = new BlockPos(dx, dy, dz);
    								IBlockState state = charger.world.getBlockState(pos);
    								float hardness = state.getBlockHardness(charger.world, pos);
    								if (hardness >= 0 && hardness < 50 && charger.world.getTileEntity(pos) == null)
    									charger.world.destroyBlock(pos, true);
    							}
    						}
    					}
    				}
    			}
    		}

    		// attack the target when we get in range
    		double var1 = this.charger.width * 2.1F * this.charger.width * 2.1F;

    		if (this.charger.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.getEntityBoundingBox().minY, this.chargeTarget.posZ) <= var1) {
    			if (!this.hasAttacked) {
    				this.hasAttacked = true;
    				this.charger.attackEntityAsMob(this.chargeTarget);
    			}
    		}

    	}

    	@Override
    	public void resetTask() {
    		this.windup = 0;
    		this.chargeTarget = null;
    		this.hasAttacked = false;

    		if (this.charger instanceof ICharger) {
    			((ICharger) charger).setCharging(false);
    		}
    	}


    	/**
    	 * Finds a point that is overshoot blocks "beyond" the target from our position.
    	 */
    	protected Vec3d findChargePoint(Entity attacker, Entity target, double overshoot) {

    		// compute angle
    		double vecx = target.posX - attacker.posX;
    		double vecz = target.posZ - attacker.posZ;
    		float rangle = (float) (Math.atan2(vecz, vecx));

    		double distance = MathHelper.sqrt(vecx * vecx + vecz * vecz);

    		// figure out where we're headed from the target angle
    		double dx = MathHelper.cos(rangle) * (distance + overshoot);
    		double dz = MathHelper.sin(rangle) * (distance + overshoot);

    		// add that to the target entity's position, and we have our destination
    		return new Vec3d(attacker.posX + dx, target.posY, attacker.posZ + dz);
    	}
    }
}