/*
 * Adapted from TeamTwilight's mod Twilight Forest Lich Boss class.
 * This inculdes not just the mob class but the mob ai classes as well.
 */

package tkxyooj.LOZ.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import tkxyooj.LOZ.common.entity.ai.EntityAIRangeAndSummon;
import tkxyooj.LOZ.common.entity.ai.EntityAIRangeAttack;
import tkxyooj.LOZ.items.weapons.ItemGoddessSword;

public class EntityTwilightPrincess extends EntityMob 
{
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation("LOZ", "entities/twilightprincess");
	private static final DataParameter<Byte> DATA_MINIONSLEFT = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_ATTACKTYPE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
	private static final DataParameter<BlockPos> SOURCE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BLOCK_POS);
 
	public BlockPos getSource() 
	{
		return dataManager.get(SOURCE);
	}
	 
	public static final int MAX_ACTIVE_MINIONS = 4;
	public static final int INITIAL_MINIONS_TO_SUMMON = 8;
	public static final int MAX_HEALTH = 600;

	private EntityTwilightPrincess midna;
	private int attackCooldown;
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_6);

	public EntityTwilightPrincess(World world) 
	{
		super(world);
		this.setSize(0.6F, 1.8F);

		this.midna = null;
		this.isImmuneToFire = true;
		this.experienceValue = 300;
	}

	public EntityTwilightPrincess getmidna()
	{
		return midna;
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
		this.tasks.addTask(1, new EntityAIRangeAttack(this));
		this.tasks.addTask(2, new EntityAIRangeAndSummon(this));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, true) 
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
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemGoddessSword.block));
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
		dataManager.register(SOURCE, BlockPos.ORIGIN);
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45000001788139344D); // Same speed as an angry enderman
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

	/**
	 * What phase of the fight are we on?
	 * 1 - shoot in the dark
	 * 2 - summoning minions
	 * 3 - melee
	 **/
	public int getPhase() 
	{
		//if health is more than 2/3
		if (this.getHealth() > 400 ) 
		{
			return 1;
		} 
		//if health is less than 2/3
		else if (this.getHealth() < 400) 
		{
			return 2;
		} 
		//if health is less than 1/3
		else if (this.getHealth() < 200) 
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
			if (this.getPhase() == 1) 
			{
				float range = 2.5F;
				List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				for(EntityPlayer player : players) 
				{
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, midna), 10);
					player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40, 0));
				}
				
				BlockPos source = getSource();
				int count = 6;
				for(int i = 0; i < count; i++) 
				{
					int x = source.getX() - 10 + rand.nextInt(20);
					int z = source.getZ() - 10 + rand.nextInt(20);
					int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, -1, z)).getY();

					EntityTwilightLandmine landmine = new EntityTwilightLandmine(world);
					landmine.setPosition(x + 0.5, y, z + 0.5);
					landmine.summoner = this;
					world.spawnEntity(landmine);
				}
	
				bossInfo.setColor(BossInfo.Color.GREEN);
			} 
			else if (this.getPhase() == 2)
			{
				float range = 2.5F;
				List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				for(EntityPlayer player : players) 
				{
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, midna), 10);
					player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40, 0));
				}
				bossInfo.setColor(BossInfo.Color.PURPLE);
			}
			else if (this.getPhase() == 3)
			{
				bossInfo.setColor(BossInfo.Color.RED);
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) 
	{
		// if we're in a wall, teleport for gosh sakes
		if ("inWall".equals(src.getDamageType()) && getAttackTarget() != null) 
		{
			teleportToSightOfEntity(getAttackTarget());
		}

		if (this.getPhase() == 1)
		{
			Entity entity = src.getImmediateSource();
			if (entity instanceof EntityArrow)
			{
				return true;
			}
		}

		if (this.getPhase() == 2 || this.getPhase() == 3)
		{
			Entity entity = src.getImmediateSource();
			if (entity instanceof EntityArrow)
			{
				return false;
			}
		}


		if (super.attackEntityFrom(src, damage)) 
		{
			if (this.getPhase() < 3 || rand.nextInt(4) == 0) 
			{
				this.teleportToSightOfEntity(getAttackTarget());
			}
 
			return true;
		} 
		else 
		{
			return false;
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

	public void launchBombFairy() 
	{
		float bodyFacingAngle = ((renderYawOffset * 3.141593F) / 180F);
		double sx = posX + (MathHelper.cos(bodyFacingAngle) * 0.65);
		double sy = posY + (height * 0.82);
		double sz = posZ + (MathHelper.sin(bodyFacingAngle) * 0.65);

		double tx = getAttackTarget().posX - sx;
		double ty = (getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F)) - (posY + height / 2.0F);
		double tz = getAttackTarget().posZ - sz;

		playSound(SoundEvents.ENTITY_GHAST_SHOOT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

		EntityBombFairy projectile = new EntityBombFairy(world, this);
		projectile.setLocationAndAngles(sx, sy, sz, rotationYaw, rotationPitch);
		projectile.setThrowableHeading(tx, ty, tz, 0.35F, 1.0F);

		world.spawnEntity(projectile);
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
 
	public boolean wantsNewMinion(EntityTwilightPrincessMinion minion) 
	{
		return countMyMinions() < EntityTwilightPrincess.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() 
	{
		return (int) world.getEntitiesWithinAABB(EntityTwilightPrincessMinion.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D))
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
	 **/
	public Vec3d findVecInLOSOf(Entity targetEntity) 
	{
		if (targetEntity == null) return null;
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
	 **/
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
}