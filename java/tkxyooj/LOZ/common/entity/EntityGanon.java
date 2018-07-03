package tkxyooj.LOZ.common.entity;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tkxyooj.LOZ.common.entity.ai.EntityAIChargeAndSummon;
import tkxyooj.LOZ.items.weapons.ItemSageSword;
import tkxyooj.LOZ.items.weapons.ItemPrincessRapier;

public class EntityGanon extends EntityMob 
{
	private EntityGanon ganon;
	private int attackCooldown;
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6);
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation("lozmod", "entities/ganon");
	private static final DataParameter<Byte> DATA_MINIONSLEFT = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_ATTACKTYPE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
		 
	public static final int MAX_ACTIVE_MINIONS = 4;
	public static final int INITIAL_MINIONS_TO_SUMMON = 6;
	public static final int MAX_HEALTH = 900;

	public EntityGanon(World worldIn) 
	{
		super(worldIn);
		this.setSize(0.9F, 2.7F);
		this.isImmuneToFire = true;
		this.experienceValue = 500;
	}
	
	public EntityGanon getganon()
	{
		return ganon;
	}

	public int getAttackCooldown() 
	{
		return attackCooldown;
	}

	public void setAttackCooldown(int cooldown) 
	{
		attackCooldown = cooldown;
	}
	
	@Override
	public void setCustomNameTag(String name) 
	{
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAIFindEntityNearestPlayer(this));
		this.tasks.addTask(1, new EntityGanon.AIChargeAttack()
		{
			@Override
			public boolean shouldExecute() 
			{
				return getPhase() == 1 && super.shouldExecute();
			}

			@Override
			public void startExecuting() 
			{
				super.startExecuting();
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemPrincessRapier.block));
			}
		});
        this.tasks.addTask(2, new EntityGanon.AIMoveRandom()		
        {
			@Override
			public boolean shouldExecute() 
			{
				return getPhase() == 1 && super.shouldExecute();
			}

			@Override
			public void startExecuting() 
			{
				super.startExecuting();
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemPrincessRapier.block));
			}
		});
		this.tasks.addTask(3, new EntityAIChargeAndSummon(this,5.0F,false)		
		{
			@Override
			public boolean shouldExecute() 
			{
				return getPhase() == 2 && super.shouldExecute();
			}
		});
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true) 
		{
			@Override
			public boolean shouldExecute() 
			{
				return getPhase() == 3 && super.shouldExecute();
			}

			@Override
			public void startExecuting() 
			{
				super.startExecuting();
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemSageSword.block));
			}
		});

	this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
	}

	@Override
	protected void entityInit() 
	{
		super.entityInit();
		dataManager.register(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
		dataManager.register(DATA_ATTACKTYPE, (byte) 0);
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.0D);
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) 
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) 
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	protected boolean canDespawn() 
	{
		return false;
	}

	public int getPhase() 
	{
		//if health is more than 2/3
		if (this.getHealth() > 600 ) 
		{
			return 1;
		} 
		//if health is less than 2/3
		else if (this.getHealth() < 600 && this.getHealth() > 300) 
		{
			return 2;
		} 
		//if health is less than 1/3
		else if (this.getHealth() < 300) 
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void onLivingUpdate() 
	{
		if (!world.isRemote) 
		{
			bossInfo.setOverlay(BossInfo.Overlay.PROGRESS);
			bossInfo.setPercent(getHealth() / getMaxHealth());
			
			//body forms
			if (this.getPhase() == 1) 
			{
				//Dark Zelda Form
				bossInfo.setColor(BossInfo.Color.GREEN);
				this.setSize(0.6F, 1.8F);
			}
			else if (this.getPhase() == 2)
			{
				//Beast Ganon Form
				bossInfo.setColor(BossInfo.Color.PURPLE);
				this.setSize(3.0F, 3.0F);
			}
			else if (this.getPhase() == 3)
			{
				//Ganondorf Form
				bossInfo.setColor(BossInfo.Color.RED);
				this.setSize(0.9F, 2.7F);
			}
		}
		super.onLivingUpdate();
	}

	@SideOnly(Side.CLIENT)
	public class RenderGanonPhases extends RenderLiving<EntityGanon>
	{
	    public RenderGanonPhases(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) 
	    {
			super(rendermanagerIn, modelbaseIn, shadowsizeIn);

			if (getPhase()==1)
			{
				modelbaseIn = new ModelPlayer(shadowsizeIn, renderMarker);
				shadowsizeIn = 0.6F;
			}
			else if (getPhase()==2)
			{
				modelbaseIn = new ModelPig();
				shadowsizeIn = 3.0F;
			}
			else
			{
				modelbaseIn = new ModelPlayer(shadowsizeIn, renderMarker);
				shadowsizeIn = 0.9F;
			}

	    }
	    ResourceLocation GanonPhase1 = new ResourceLocation("lozmod: textures/entity/dark_princess.png");
		ResourceLocation GanonPhase2 = new ResourceLocation("lozmod: textures/entity/beast_ganon.png");
		ResourceLocation GanonPhase3 = new ResourceLocation("lozmod: textures/entity/ganondorf.png");
		@Override
		protected ResourceLocation getEntityTexture(EntityGanon entity) 
		{
			if(entity.getPhase() == 1)
				{return GanonPhase1;}
			else if(entity.getPhase() == 2) 
				{return GanonPhase2;}
			else 
				{return GanonPhase3;}
		}
	}

	
	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) 
	{
		// immune to projectile
		Entity entity = src.getImmediateSource();
		if (entity instanceof EntityArrow)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}

	@Override
	protected void updateAITasks() 
	{
		super.updateAITasks();
		if (getAttackTarget() == null) 
		{
			return;
		}

		//cooldown
		if (attackCooldown > 0) 
		{
			attackCooldown--;
		}

		// always watch our target
		this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 100F, 100F);
	}

	public void launchStaticFairy() 
	{
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = posX + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = posY + (height * 0.82);
		double sz = posZ + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().posX - sx;
		double ty = (getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F)) - (posY + height / 2.0F);
		double tz = getAttackTarget().posZ - sz;

		playSound(SoundEvents.ENTITY_GHAST_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityStaticFairy projectile = new EntityStaticFairy(world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.setThrowableHeading(tx, ty, tz, 0.35F, 1.0F);

		world.spawnEntity(projectile);
	}
	 
	public boolean wantsNewMinion(EntityGanonMinion minion) 
	{
		return countMyMinions() < EntityGanon.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() 
	{
		return (int) world.getEntitiesWithinAABB(EntityGanonMinion.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D))
				.stream()
				.filter(m -> m.master == this)
				.count();
	}

	public void teleportToSightOfEntity(Entity entity) 
	{
		Vec3d dest = findVecInLOSOf(entity);
		double srcX = posX;
		double srcY = posY;
		double srcZ = posZ;

		if (dest != null) 
		{
			teleportToNoChecks(dest.x, dest.y, dest.z);
			this.getLookHelper().setLookPositionWithEntity(entity, 100F, 100F);
			this.renderYawOffset = this.rotationYaw;

			if (!this.getEntitySenses().canSee(entity)) 
			{
				teleportToNoChecks(srcX, srcY, srcZ);
			}
		}
	}

	/**
	* Returns coords that would be good to teleport to.
	* Returns null if we can't find anything
	*/
	public Vec3d findVecInLOSOf(Entity targetEntity) 
	{
		if (targetEntity == null) 
			return null;
		double origX = posX;
		double origY = posY;
		double origZ = posZ;

		int tries = 100;
		for (int i = 0; i < tries; i++) 
		{
			// we abuse EntityLivingBase.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
			double tx = targetEntity.posX + rand.nextGaussian() * 16D;
			double ty = targetEntity.posY;
			double tz = targetEntity.posZ + rand.nextGaussian() * 16D;

			boolean destClear = attemptTeleport(tx, ty, tz);
			boolean canSeeTargetAtDest = canEntityBeSeen(targetEntity); // Don't use senses cache because we're in a temporary position
			setPositionAndUpdate(origX, origY, origZ);

			if (destClear && canSeeTargetAtDest) 
			{
				return new Vec3d(tx, ty, tz);
			}
		}

		return null;
	}

	/**
	* Does not check that the teleport destination is valid, we just go there
	*/
	private void teleportToNoChecks(double destX, double destY, double destZ) 
	{
		// save original position
		double srcX = posX;
		double srcY = posY;
		double srcZ = posZ;

		// change position
		setPositionAndUpdate(destX, destY, destZ);

		this.world.playSound(null, srcX, srcY, srcZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
		this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

		// sometimes we need to do this
		this.isJumping = false;
	}

	public byte getMinionsToSummon() 
	{
		return dataManager.get(DATA_MINIONSLEFT);
	}

	public void setMinionsToSummon(int minionsToSummon) 
	{
		dataManager.set(DATA_MINIONSLEFT, (byte) minionsToSummon);
	}

	public byte getNextAttackType() 
	{
		return dataManager.get(DATA_ATTACKTYPE);
	}

	public void setNextAttackType(int attackType) 
	{
		dataManager.set(DATA_ATTACKTYPE, (byte) attackType);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setByte("MinionsToSummon", getMinionsToSummon());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{
		super.readEntityFromNBT(nbttagcompound);
		setMinionsToSummon(nbttagcompound.getByte("MinionsToSummon"));
		if (this.hasCustomName()) 
		{
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) 
	{
		super.onDeath(par1DamageSource);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() 
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public boolean isNonBoss() 
	{
		return false;
	}
	
	//AI copied from Vex class
	protected static final DataParameter<Byte> GANON_FLAGS = EntityDataManager.<Byte>createKey(EntityGanon.class, DataSerializers.BYTE);

	@Nullable
    private BlockPos boundOrigin;
	@Nullable
    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }
	
	public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
    {
        this.boundOrigin = boundOriginIn;
    }
	private boolean getGanonFlag(int mask)
    {
        int i = ((Byte)this.dataManager.get(GANON_FLAGS)).byteValue();
        return (i & mask) != 0;
    }

    private void getGanonFlag(int mask, boolean value)
    {
        int i = ((Byte)this.dataManager.get(GANON_FLAGS)).byteValue();

        if (value)
        {
            i = i | mask;
        }
        else
        {
            i = i & ~mask;
        }

        this.dataManager.set(GANON_FLAGS, Byte.valueOf((byte)(i & 255)));
    }
	public boolean isCharging()
    {
        return this.getGanonFlag(1);
    }

    public void setCharging(boolean charging)
    {
        this.getGanonFlag(1, charging);
    }
	
	
	class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack()
        {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityGanon.this.getAttackTarget() != null && !EntityGanon.this.getMoveHelper().isUpdating() && EntityGanon.this.rand.nextInt(7) == 0)
            {
                return EntityGanon.this.getDistanceSq(EntityGanon.this.getAttackTarget()) > 4.0D;
            }
            else
            {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return EntityGanon.this.getMoveHelper().isUpdating() && EntityGanon.this.isCharging() && EntityGanon.this.getAttackTarget() != null && EntityGanon.this.getAttackTarget().isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            EntityLivingBase entitylivingbase = EntityGanon.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            EntityGanon.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityGanon.this.setCharging(true);
            EntityGanon.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
        	EntityGanon.this.setCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = EntityGanon.this.getAttackTarget();

            if (EntityGanon.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox()))
            {
            	EntityGanon.this.attackEntityAsMob(entitylivingbase);
            	EntityGanon.this.setCharging(false);
            }
            else
            {
                double d0 = EntityGanon.this.getDistanceSq(entitylivingbase);

                if (d0 < 9.0D)
                {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    EntityGanon.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
        }
    }
	class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom()
        {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return !EntityGanon.this.getMoveHelper().isUpdating() && EntityGanon.this.rand.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            BlockPos blockpos = EntityGanon.this.getBoundOrigin();

            if (blockpos == null)
            {
                blockpos = new BlockPos(EntityGanon.this);
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(EntityGanon.this.rand.nextInt(15) - 7, EntityGanon.this.rand.nextInt(11) - 5, EntityGanon.this.rand.nextInt(15) - 7);

                if (EntityGanon.this.world.isAirBlock(blockpos1))
                {
                	EntityGanon.this.moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityGanon.this.getAttackTarget() == null)
                    {
                    	EntityGanon.this.getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}