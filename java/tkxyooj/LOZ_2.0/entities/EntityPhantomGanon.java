package tkxyooj.LOZ.entities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tkxyooj.LOZ.items.StartupCommon;

interface IParryModifier 
{
	float getOffensiveModifier(EntityLivingBase entity, ItemStack stack);
	float getDefensiveModifier(EntityLivingBase entity, ItemStack stack);
}

public class EntityPhantomGanon extends EntityMob implements IParryModifier
{
	/** Attack flag for model animations, updated via health update */
	private static final byte ATTACK_FLAG = 0x5;

	/** Flag for model to animate power attack charging up, updated via health update */
	private static final byte POWER_UP_FLAG = 0x6;

	/** Flag for model to animate power attack swing, updated via health update */
	private static final byte POWER_ATTACK_FLAG = 0x7;

	/** Flag for model to animate parry motion, updated via health update */
	private static final byte PARRY_FLAG = 0x8;

	/** Flag for model to animate spin attack motion, updated via health update */
	private static final byte SPIN_FLAG = 0x9;

	/** dataManager for armor health */
	private final static DataParameter<Integer> ARMOR_INDEX = EntityDataManager.createKey(EntityPhantomGanon.class, DataSerializers.VARINT);

	/** Replacement for removed 'attackTime' */
	protected int attackTime;

	/** Timer for attack animation; negative swings one way, positive the other */
	@SideOnly(Side.CLIENT)
	public int attackTimer;

	/** Timer for power attack charging animation */
	@SideOnly(Side.CLIENT)
	public int chargeTimer;

	/** Flag set when performing a power attack swing, so can use same attack timer */
	@SideOnly(Side.CLIENT)
	public boolean isPowerAttack;

	/** Highest parry chance when this timer is zero; set to 10 after each parry */
	public int parryTimer;

	/** Recently hit timer */
	private int recentHitTimer;
	
	/** Number of successive hits that fell within recent hit time */
	private int recentHits;

	/** Timer for spin attack */
	private int spinAttackTimer;

	public EntityPhantomGanon(World world) 
	{
		super(world);
		this.setSize(1.0F, 3.0F);
		this.experienceValue = 100;
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(StartupCommon.sagesword));
	}

	@Override
	protected void initEntityAI() 
	{
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
	}
	@Override
	public void entityInit() 
	{
		super.entityInit();
		dataManager.register(ARMOR_INDEX, 17);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(600.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D); // unarmed
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITHER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_WITHER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITHER_DEATH;
	}

	public static final boolean isTargetInFrontOf(Entity seeker, Entity target, float fov) {
		// thanks again to Battlegear2 for the following code snippet
		double dx = target.posX - seeker.posX;
		double dz;
		for (dz = target.posZ - seeker.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}
		while (seeker.rotationYaw > 360) { seeker.rotationYaw -= 360; }
		while (seeker.rotationYaw < -360) { seeker.rotationYaw += 360; }
		float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - seeker.rotationYaw;
		yaw = yaw - 90;
		while (yaw < -180) { yaw += 360; }
		while (yaw >= 180) { yaw -= 360; }
		return yaw < fov && yaw > -fov;
	}
	
	public static final void knockTargetBack(EntityLivingBase pushedEntity, EntityLivingBase pushingEntity) {
		if (pushedEntity.canBePushed()) {
			double dx = pushedEntity.posX - pushingEntity.posX;
			double dz;
			for (dz = pushedEntity.posZ - pushingEntity.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D){
				dx = (Math.random() - Math.random()) * 0.01D;
			}
			pushedEntity.knockBack(pushingEntity, 0, -dx, -dz);
		}
	}
	public static void dropHeldItem(EntityLivingBase entity) {
		if (!entity.world.isRemote && entity.getHeldItem(EnumHand.MAIN_HAND) != null) {
			EntityItem drop = new EntityItem(entity.world, entity.posX,
					entity.posY - 0.30000001192092896D + (double) entity.getEyeHeight(),
					entity.posZ, entity.getHeldItem(EnumHand.MAIN_HAND).copy());
			float f = 0.3F;
			float f1 = entity.world.rand.nextFloat() * (float) Math.PI * 2.0F;
			drop.motionX = (double)(-MathHelper.sin(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * f);
			drop.motionZ = (double)(MathHelper.cos(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * f);
			drop.motionY = (double)(-MathHelper.sin(entity.rotationPitch / 180.0F * (float) Math.PI) * f + 0.1F);
			f = 0.02F * entity.world.rand.nextFloat();
			drop.motionX += Math.cos((double) f1) * (double) f;
			drop.motionY += (double)((entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.1F);
			drop.motionZ += Math.sin((double) f1) * (double) f;
			drop.setPickupDelay(40);
			entity.world.spawnEntity(drop);
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
		}
	}
	
	public static void playSoundAtEntity(Entity entity, SoundEvent sound, float f, float add) 
	{
		playSoundAt(entity.world, entity.posX, entity.posY, entity.posZ, sound, f, add);
	}
	
	public static void playSoundAt(World world, double x, double y, double z, SoundEvent sound, float f, float add) {
		float volume = world.rand.nextFloat() * f + add;
		float pitch = 1.0F / (world.rand.nextFloat() * f + add);
		world.playSound(x, y, z, sound, SoundCategory.PLAYERS, volume, pitch, false);
	}

	/**
	 * Returns the total disarm chance modifier based on the two entities and their held items;
	 * includes all modifiers used by Parry except for the timing bonus and attacks parried.
	 * @param defender	Entity defending against an attack, possibly disarming the attacker
	 * @param attacker	Attacking entity who may be disarmed, possibly null
	 * @return	Combined total of all entity and item disarm modifiers
	 */
	public static float getDisarmModifier(EntityLivingBase defender, EntityLivingBase attacker) {
		ItemStack defStack = defender.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offStack = (attacker != null ? attacker.getHeldItem(EnumHand.MAIN_HAND) : null);
		int disarmPenalty = 10;
		int skillLevel = 2;
		float modifier = 0.0F;
		// DEFENDER
		if (defender instanceof EntityPlayer) {
			modifier += 0.1F * skillLevel;
		}
		if (defender instanceof IParryModifier) {
			modifier += ((IParryModifier) defender).getDefensiveModifier(defender, defStack);
		}
		if (defStack != null && defStack.getItem() instanceof IParryModifier) {
			modifier += ((IParryModifier) defStack.getItem()).getDefensiveModifier(defender, defStack);
		}
		// ATTACKER
		if (attacker instanceof EntityPlayer) {
			modifier -= disarmPenalty * skillLevel;
		}
		if (attacker instanceof IParryModifier) {
			modifier -= ((IParryModifier) attacker).getOffensiveModifier(attacker, offStack);
		}
		if (offStack != null && offStack.getItem() instanceof IParryModifier) {
			modifier -= ((IParryModifier) offStack.getItem()).getOffensiveModifier(attacker, offStack);
		}
		return modifier;
	}

	public static final List<EntityLivingBase> acquireAllLookTargets(EntityLivingBase seeker, int distance, double radius) {
		final int MAX_DISTANCE = 256;
		if (distance < 0 || distance > MAX_DISTANCE) {
			distance = MAX_DISTANCE;
		}
		List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
		Vec3d vec3 = seeker.getLookVec();
		double targetX = seeker.posX;
		double targetY = seeker.posY + seeker.getEyeHeight() - 0.10000000149011612D;
		double targetZ = seeker.posZ;
		double distanceTraveled = 0;

		while ((int) distanceTraveled < distance) {
			targetX += vec3.x;
			targetY += vec3.y;
			targetZ += vec3.z;
			distanceTraveled += vec3.lengthVector();
			AxisAlignedBB bb = new AxisAlignedBB(targetX-radius, targetY-radius, targetZ-radius, targetX+radius, targetY+radius, targetZ+radius);
			List<EntityLivingBase> list = seeker.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			for (EntityLivingBase target : list) {
				if (target != seeker && target.canBeCollidedWith() && isTargetInSight(vec3, seeker, target)) {
					if (!targets.contains(target)) {
						targets.add(target);
					}
				}
			}
		}

		return targets;
	}
	
	private static final boolean isTargetInSight(Vec3d vec3, EntityLivingBase seeker, Entity target) {
		return seeker.canEntityBeSeen(target) && isTargetInFrontOf(seeker, target, 60);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		boolean isPlayer = source.getImmediateSource() instanceof EntityPlayer;
		
		if (source == DamageSource.IN_WALL && ticksExisted > 10) 
		{
			breakEnclosingBlocks(); // fall through to allow damage
		} 
		else if (source.isUnblockable() || (isPlayer)) 
		{
			if (parryAttack(source)) 
			{
				return false;
			}
			return super.attackEntityFrom(source, amount);
		} 
		else if (source.getImmediateSource() == null || source.isMagicDamage()) 
		{
			if (source == DamageSource.CACTUS || source == DamageSource.FALLING_BLOCK)
			{
				return false; // no damage from cacti or falling blocks
			}
			return super.attackEntityFrom(source, amount);
		} 
		else if (source.isExplosion())
		{
			return false;
		} 
		else if (isTargetInFrontOf(this, source.getImmediateSource(), 120))
		{
			return false;
		} 
		else 
		{
			int cap = 20;
			return super.attackEntityFrom(source, Math.min(cap, amount));
			
		}
		return false;
	}

	/**
	 * Tries to break out of suffocating blocks
	 */
	@SuppressWarnings("deprecation")
	protected void breakEnclosingBlocks() {
		BlockPos eyePos = new BlockPos(posX, posY + getEyeHeight(), posZ);
		boolean flag = false;
		// smash all blocks in a 3x3x3 area around the head
		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				for (int k = -1; k < 2; ++k) {
					BlockPos pos = eyePos.add(i, j, k);
					Block block = world.getBlockState(pos).getBlock();
					float hardness = ((IBlockProperties) block.getBlockState()).getBlockHardness(world, pos);
					if (block.isOpaqueCube((IBlockState) block.getBlockState()) && hardness >= 0.0F && hardness < 20.0F && block.canEntityDestroy((IBlockState) block.getBlockState(), world, pos, this)) 
					{
						flag = true;
						if (!world.isRemote) {
							world.destroyBlock(pos, true);
						}
					}
				}
			}
		}
		if (flag) {
			swingArm();
			attackTime = 20;
			world.playSound(posX, posY, posZ, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
	}

	/**
	 * Returns true if the entity was able to parry the source of damage, and
	 * may also disarm the attacker, if any
	 */
	protected boolean parryAttack(DamageSource source) {
		Entity entity = source.getImmediateSource();
		if (entity == null || source.isExplosion()) {
			return false;
		} else if (isTargetInFrontOf(this, entity, 90) && rand.nextFloat() < (0.5F - (parryTimer * 0.05F))) {
			world.setEntityState(this, PARRY_FLAG);
			parryTimer = 10;
			super.swingArm(swingingHand);
			attackTime = Math.max(attackTime, 5); // don't allow attacks until parry animation finishes
			if (entity instanceof EntityLivingBase && !source.isProjectile()) {
				EntityLivingBase attacker = (EntityLivingBase) entity;
				if (attacker.getHeldItem(swingingHand) != null) {
					
					EntityPhantomGanon.playSoundAtEntity(this, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 0.4F, 0.5F);
					float disarmChance = getDisarmModifier(this, attacker);
					if (rand.nextFloat() < disarmChance) {
						dropHeldItem(attacker);
					}
				}
				knockTargetBack(attacker, this);
			}
			return true;
		}
		return false;
	}

//	@Override
	public float getOffensiveModifier(EntityLivingBase entity, ItemStack stack) {
		return (0.1F * (float) world.getDifficulty().getDifficultyId());
	}

//	@Override
	public float getDefensiveModifier(EntityLivingBase entity, ItemStack stack) {
		return (0.1F * (float) world.getDifficulty().getDifficultyId());
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (attackTime <= 0) {
			attackTime = 20;
			world.setEntityState(this, ATTACK_FLAG);
			if (isTargetInFrontOf(this, entity, 60) && attackEntity(entity, ATTACK_FLAG)) {
				EntityPhantomGanon.playSoundAtEntity(this, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 0.4F, 0.5F);
				return true;
			} else {
				EntityPhantomGanon.playSoundAtEntity(this, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.4F, 0.5F);
			}
		}
		return false;
	}

	/**
	 * Actually attacks the entity, performing all the attack and damage calculations
	 * @param flag	attack flag, e.g. POWER_ATTACK_FLAG
	 * @return		true if attack was successful (i.e. attackEntityFrom returned true
	 */
	protected boolean attackEntity(Entity entity, int flag) {
		float damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int k = (flag == POWER_ATTACK_FLAG || flag == SPIN_FLAG ? 1 : 0); // knockback
		if (entity instanceof EntityLivingBase) {
			damage += EnchantmentHelper.getModifierForCreature(this.getHeldItem(EnumHand.MAIN_HAND), ((EntityLivingBase) entity).getCreatureAttribute());
			k += EnchantmentHelper.getKnockbackModifier(this);
		}
		if (entity.attackEntityFrom(getDamageSource(flag), damage)) {
			if (k > 0) {
				entity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F) * (float) k * 0.5F), 0.1D, (double)(MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F) * (float) k * 0.5F));
				motionX *= 0.6D;
				motionZ *= 0.6D;
			}
			int j = EnchantmentHelper.getFireAspectModifier(this);
			if (j > 0) {
				entity.setFire(j * 4);
			}
			if (entity instanceof EntityLivingBase) {
				EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, this);
			}
			EnchantmentHelper.applyArthropodEnchantments(this, entity);
			return true;
		}
		return false;
	}

	/**
	 * Returns appropriate damage source based on attack flag
	 * @param flag	Same flag as passed to {@link #onMeleeImpact}
	 */
	protected DamageSource getDamageSource(int flag) {
		switch(flag) {
		case POWER_ATTACK_FLAG: return DamageSource.causeMobDamage(this).setDamageBypassesArmor();
		default: return DamageSource.causeMobDamage(this);
		}
	}

//	@Override
	public float onBackSliced(EntityPlayer attacker, int level, float damage) {
		return damage;
	}

//	@Override
	public void beginPowerAttack() {
		attackTime = getChargeTime(); // prevent regular attacks from occurring while charging up
		world.setEntityState(this, POWER_UP_FLAG);
	}

//	@Override
	public void cancelPowerAttack() {
		world.setEntityState(this, POWER_UP_FLAG);
	}

	/**
	 * 3 extra ticks included for animation of raising arms up to position
	 */
//	@Override
	public int getChargeTime() {
		return 28 - (world.getDifficulty().getDifficultyId() * 5);
	}

//	@Override
	public void onAttackMissed() {
		EntityPhantomGanon.playSoundAtEntity(this, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.4F, 0.5F);
	}

//	@Override
	public void swingArm() {} // don't allow item to swing as normal or it screws up the attack animations

	private int countdown = 200;
    private int max_minions = 6;
    public int countMyMinions() 
    {
		return (int) world.getEntitiesWithinAABB(EntityGanonMinion.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D)).stream().count();
    }
    
	@Override
	public void onLivingUpdate() 
	{
		super.onLivingUpdate();
		
		countdown--;
        if (countdown == 0 && !world.isRemote)
        {
        	if (countMyMinions() < max_minions)
        	{
        		float range = 6F;
        		EntityGanonMinion minion = new EntityGanonMinion(world);
        		minion.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY + 2, posZ + 0.5 + Math.random() * range - range / 2);
        		minion.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(minion)), null);
               	world.spawnEntity(minion);
        		countdown = 200;
        	}
        }
		
		if (attackTime > 0) 
		{
			--attackTime;
		}
		if (parryTimer > 0) 
		{
			--parryTimer;
		}
		if (recentHitTimer > 0) 
		{
			if (--recentHitTimer == 0) 
			{
				recentHits = 0;
			}
		}
		if (spinAttackTimer > 0) 
		{
			--spinAttackTimer;
			if (isEntityAlive()) 
			{
				rotationYaw += 30.0F;
				while (rotationYaw > 360.0F) { rotationYaw -= 360.0F; }
				while (rotationYaw < -360.0F) { rotationYaw += 360.0F; }
			}
		}
		if (world.isRemote) 
		{
			if (attackTimer > 0) 
			{
				--attackTimer;
			} else if (attackTimer < 0) 
			{
				++attackTimer;
			}
			if (chargeTimer > 0) 
			{
				--chargeTimer;
			}
	    }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte flag) {
		switch(flag) {
		case ATTACK_FLAG:
			isPowerAttack = false;
			if (attackTimer == 0) {
				attackTimer = (rand.nextFloat() < 0.5F ? 10 : -10);
			}
			break;
		case PARRY_FLAG:
			parryTimer = 10;
			super.swingArm(EnumHand.MAIN_HAND);
			break;
		case POWER_UP_FLAG:
			// cancel or begin charge; 3 ticks up already included in charge time, 3 ticks down
			chargeTimer = (chargeTimer > 0 ? 0 : getChargeTime());
			break;
		case POWER_ATTACK_FLAG:
			isPowerAttack = true;
			attackTimer = 7;
			chargeTimer = 0;
			break;
		case SPIN_FLAG:
			spinAttackTimer = 12;
			break;
		default:
			super.handleStatusUpdate(flag);
		}
	}
}