/*
 * Adapted from TeamTwilight's mod Twilight Forest Lich Boss class.
 * This includes not just the mob class but the mob ai classes as well.
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
	//Loot table
	@Override
	public ResourceLocation LOOT_TABLE = new ResourceLocation(LOZ.MODID, "entities/twilightprincess");

	//Variables
	public static final float ARENA_RANGE = 12F;
	private static final int SPAWN_TICKS = 160;
	private static final float MAX_HP = 600F;

	private static final int MOB_SPAWN_START_TICKS = 20;
	private static final int MOB_SPAWN_END_TICKS = 80;
	private static final int MOB_SPAWN_BASE_TICKS = 800;
	private static final int MOB_SPAWN_TICKS = MOB_SPAWN_BASE_TICKS + MOB_SPAWN_START_TICKS + MOB_SPAWN_END_TICKS;
	private static final int MOB_SPAWN_WAVES = 10;
	private static final int MOB_SPAWN_WAVE_TIME = MOB_SPAWN_BASE_TICKS / MOB_SPAWN_WAVES;

	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourcesZ";
	private static final String TAG_MOB_SPAWN_TICKS = "mobSpawnTicks";
	private static final String TAG_HARD_MODE = "hardMode";
	private static final String TAG_PLAYER_COUNT = "playerCount";
	
	public static final int MAX_ACTIVE_MINIONS = 4;
	public static final int INITIAL_MINIONS_TO_SUMMON = 4;
	
	private static final DataParameter<Boolean> DATA_ISCLONE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> DATA_MINIONSLEFT = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> DATA_ATTACKTYPE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BYTE);
	private static final DataParameter<Integer> INVUL_TIME = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> HARD_MODE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BOOLEAN);
	private static final DataParameter<BlockPos> SOURCE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<UUID>> BOSSINFO_ID = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private EntityTwilightPrincess realMidna;
	private boolean spawnLandmines = false;
	private boolean spawnPixies = false;
	private boolean aggro = false;
	private int attackCooldown;
	private int tpDelay = 0;
	private int mobSpawnTicks = 0;
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.EntityTwilightPrincess.name"), BossInfo.Color.BLACK, BossInfo.Overlay.PROGRESS).setCreateFog(true);;
	public EntityPlayer trueKiller = null;

    //Custom name tags
    @Override
	public void setCustomNameTag(String name) 
	{
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}
    
    //Main functions
    public EntityTwilightPrincess(World world) 
	{
		super(world);
		this.setSize(0.6F, 1.8F);
		this.realMidna = null;
		this.isImmuneToFire = true;
		this.experienceValue = 300;
	}
    
    public EntityTwilightPrincess(World world, EntityTwilightPrincess shadowMidna) 
    {
		this(world);
		setShadowClone(true);
		this.realMidna = shadowMidna;
    }
    
	public EntityTwilightPrincess getrealMidna()
	{
		return realMidna;
	}

	public int getAttackCooldown() 
	{
		return attackCooldown;
	}

	public void setAttackCooldown(int cooldown) 
	{
		attackCooldown = cooldown;
	}
    
    //Returns number of active bosses. Used for spawn info so if a boss exist, another one cannot be summoned
    private static int getBossesAround(World world, BlockPos source) 
    {
		float range = 15F;
		List l = world.getEntitiesWithinAABB(EntityTwilightPrincess.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
		return l.size();
	}
    
    //Retrun list of players
    private List<EntityPlayer> getPlayersAround() 
    {
		BlockPos source = getSource();
		float range = 15F;
		return world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
	}
    
    //Check to see if player is real
    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");
	public static boolean isTruePlayer(Entity e) 
    {
		if(!(e instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}
    
    //Spawn info
    public static boolean spawn(EntityPlayer player, ItemStack stack, World world, BlockPos pos, boolean hard) 
    {
        if(world.getTileEntity(pos) instanceof TileEntityBeacon && isTruePlayer(player)) 
        {
            int bosses = getBossesAround(world, pos);
            if(bosses > 0)
                return false;
            
            if(world.isRemote)
                return true;
            stack.shrink(1);
            EntityTwilightPrincess e = new EntityTwilightPrincess(world);
            e.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            e.setInvulTime(SPAWN_TICKS);
            e.setHealth(1F);
            e.setSource(pos);
            e.mobSpawnTicks = MOB_SPAWN_TICKS;
            e.setHardMode(hard);

            int playerCount = (int) e.getPlayersAround().stream().filter(EntityTwilightPrincess::isTruePlayer).count();
            e.setPlayerCount(playerCount);
            e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HP).setBaseValue(MAX_HP * playerCount);
            if (hard)
                e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).setBaseValue(15);
            e.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 10F, 0.1F);
            e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), null);
            world.spawnEntity(e);
            return true;
        }
        return false;
	}
    
    //Initialize entity AI
    @Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, ARENA_RANGE * 1.5F));
		this.tasks.addTask(3, new EntityAIRangeAttack(this));
		this.tasks.addTask(4, new EntityAIRangeAndSummon(this));
        this.tasks.addTask(5, new EntityAIShadows(this));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
	}
    
    //Initialize entity by registering data manager
    @Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_ISCLONE, false);
		dataManager.register(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
		dataManager.register(DATA_ATTACKTYPE, (byte) 0);

		dataManager.register(INVUL_TIME, 0);
		dataManager.register(SOURCE, BlockPos.ORIGIN);
		dataManager.register(HARD_MODE, false);
		dataManager.register(PLAYER_COUNT, 0);
		dataManager.register(BOSSINFO_ID, Optional.absent());
	}
    
    //Data manager sets
    public boolean isShadowClone() {return dataManager.get(DATA_ISCLONE);}
	public byte getMinionsToSummon() {return dataManager.get(DATA_MINIONSLEFT);}
	public byte getNextAttackType() {return dataManager.get(DATA_ATTACKTYPE);}
	public int getInvulTime() {return dataManager.get(INVUL_TIME);}
	public int getPlayerCount() {return dataManager.get(PLAYER_COUNT);}
	public BlockPos getSource() {return dataManager.get(SOURCE);}    
	public boolean isHardMode() {return dataManager.get(HARD_MODE);}
    
  	public void setShadowClone(boolean par1) {bossInfo.setVisible(!par1);dataManager.set(DATA_ISCLONE, par1);}
 	public void setMinionsToSummon(int minionsToSummon) {dataManager.set(DATA_MINIONSLEFT, (byte) minionsToSummon);}
 	public void setNextAttackType(int attackType) {dataManager.set(DATA_ATTACKTYPE, (byte) attackType);}
	public void setInvulTime(int time) {dataManager.set(INVUL_TIME, time);}
	public void setPlayerCount(int count) {dataManager.set(PLAYER_COUNT, count);}
	public void setSource(BlockPos pos) {dataManager.set(SOURCE, pos);}
	public void setHardMode(boolean hardMode) {dataManager.set(HARD_MODE, hardMode);}

    //Apply entity attributes
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HP).setBaseValue(MAX_HP);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);

	}

    //Add player tracking
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) 
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

    //Remove player tracking
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) 
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

    //Make entity unable to despawn naturally
	@Override
	protected boolean canDespawn() 
	{
		return false;
	}

    //Get the battle phase
	public int getPhase() 
	{
		//if health is more than 2/3
		if (this.getHealth() > 400 ) 
		{
			return 1;
		} 
		//if health is less than 2/3
		else if (this.getHealth() < 400 && this.getHealth() > 200) 
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

    //While entity is alive
	@Override
	public void onLivingUpdate() 
	{
		//Recolors the health bar
        if (!world.isRemote) 
		{
			bossInfo.setOverlay(BossInfo.Overlay.PROGRESS);
			bossInfo.setPercent(getHealth() / getMaxHealth());
			if (this.getPhase() == 1) 
			{
				bossInfo.setColor(BossInfo.Color.GREEN);
			} 
			else if (this.getPhase() == 2)
			{
				bossInfo.setColor(BossInfo.Color.ORANGE);
			}
			else if (this.getPhase() == 3)
			{
				bossInfo.setColor(BossInfo.Color.RED);
			}
		}
		super.onLivingUpdate();

		BlockPos source = getSource();
		int invul = getInvulTime();

		dataManager.set(BOSSINFO_ID, Optional.of(bossInfo.getUniqueId()));

        //Dismount player
		if(!getPassengers().isEmpty())
			dismountRidingEntity();

        //Kill entity is world is set to peaceful
		if(world.getDifficulty() == EnumDifficulty.PEACEFUL)
			setDead();

        //Is world on hardmode
		boolean hard = isHardMode();
        //List all players in the area
		List<EntityPlayer> players = getPlayersAround();
        //Count the number of players in the area
		int playerCount = getPlayerCount();

        //if all players are dead in the area, entity dies
		if(players.isEmpty() && !world.playerEntities.isEmpty())
			setDead();
		else {
			for(EntityPlayer player : players) 
            {
				clearPotions(player);
				keepInsideArena(player);
				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.isCreativeMode;
			}
		}

		if(isDead)
			return;

		boolean spawnMissiles = hard && ticksExisted % 15 < 4;
        //Initial entity spawn
		if(invul > 0 && mobSpawnTicks == MOB_SPAWN_TICKS) 
        {
			if(invul < SPAWN_TICKS)  {
				if(invul > SPAWN_TICKS / 2 && world.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
					for(int i = 0; i < 2; i++)
						spawnExplosionParticle();
			}

			setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);

			motionY = 0;
		} 
        else 
        {
            //Teleport randomly
            tpDelay = 10;
            if(tpDelay > 0) 
            {
				if(invul > 0)
					setInvulTime(invul - 1);
					tpDelay--;
					if(tpDelay == 0 && getHealth() > 0) 
                    {
						int tries = 0;
						while(!teleportRandomly() && tries < 50)
							tries++;
						if(tries >= 50)
							teleportTo(source.getX() + 0.5, source.getY() + 1.6, source.getZ() + 0.5);
                    }
            /*
            //If entity is mad
			if(aggro) 
            {
                //health is less than 15%
				boolean dying = getHealth() / getMaxHealth() < 0.15;
				if(dying && mobSpawnTicks > 0) 
                {
					motionX = 0;
					motionY = 0;
					motionZ = 0;

					int reverseTicks = MOB_SPAWN_TICKS - mobSpawnTicks;
					if(reverseTicks < MOB_SPAWN_START_TICKS) 
                    {
						motionY = 0.2;
						setInvulTime(invul + 1);
					}
                    //Spawn horde of mobs or if in hardmode, spawn missiles
					if(reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobSpawnTicks > MOB_SPAWN_END_TICKS && mobSpawnTicks % MOB_SPAWN_WAVE_TIME == 0) 
                    {
						spawnMobs(players);

						if(hard && ticksExisted % 3 < 2) {
							for(int i = 0; i < playerCount; i++)
								spawnMissile();
							spawnMissiles = false;
						}
					}

					mobSpawnTicks--;
					tpDelay = 10;
				} 

                else if(tpDelay > 0) 
                {
					if(invul > 0)
						setInvulTime(invul - 1);

					tpDelay--;
					if(tpDelay == 0 && getHealth() > 0) 
                    {
						int tries = 0;
						while(!teleportRandomly() && tries < 50)
							tries++;
						if(tries >= 50)
							teleportTo(source.getX() + 0.5, source.getY() + 1.6, source.getZ() + 0.5);

						if(spawnLandmines) 
                        {
							int count = dying && hard ? 7 : 6;
							for(int i = 0; i < count; i++) 
                            {
								int x = source.getX() - 10 + rand.nextInt(20);
								int z = source.getZ() - 10 + rand.nextInt(20);
								int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, -1, z)).getY();

								EntityMagicLandmine landmine = new EntityMagicLandmine(world);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								world.spawnEntity(landmine);
							}

						}

						if(!players.isEmpty())
							for(int pl = 0; pl < playerCount; pl++)
								for(int i = 0; i < (spawnPixies ? world.rand.nextInt(hard ? 6 : 3) : 1); i++) 
                                {
									EntityPixie pixie = new EntityPixie(world);
									pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
									pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
									pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);
									world.spawnEntity(pixie);
								}

						tpDelay = hard ? dying ? 35 : 45 : dying ? 40 : 60;
						spawnLandmines = true;
						spawnPixies = false;
                        
					}
				}

				if(spawnMissiles)
					spawnMissile();
			} 
            else 
            {
				if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
            */
		}
	}
    
    //Sub-functions for the onLiving function
	private void clearPotions(EntityPlayer player) 
    {
		int posXInt = MathHelper.floor(posX);
		int posZInt = MathHelper.floor(posZ);

		List<Potion> potionsToRemove = player.getActivePotionEffects().stream()
				.filter(effect -> effect.getDuration() < 160 && effect.getIsAmbient() && !effect.getPotion().isBadEffect())
				.map(PotionEffect::getPotion)
				.distinct()
				.collect(Collectors.toList());

		potionsToRemove.forEach(potion -> 
        {
			player.removePotionEffect(potion);
			((WorldServer) world).getPlayerChunkMap().getEntry(posXInt >> 4, posZInt >> 4).sendPacket(new SPacketRemoveEntityEffect(player.getEntityId(), potion));
		});
	}

	private void keepInsideArena(EntityPlayer player) 
    {
		BlockPos source = getSource();
		if(tkxyooj.LOZ.common.core.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) 
        {
			Vector3 sourceVector = new Vector3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
			Vector3 playerVector = Vector3.fromEntityCenter(player);
			Vector3 motion = sourceVector.subtract(playerVector).normalize();

			player.motionX = motion.x;
			player.motionY = 0.2;
			player.motionZ = motion.z;
			player.velocityChanged = true;
		}
	}

	private void spawnMobs(List<EntityPlayer> players) 
    {
		int playerCount = getPlayerCount();
		for(int pl = 0; pl < playerCount; pl++) {
			for(int i = 0; i < 3 + world.rand.nextInt(2); i++) {
				EntityLiving entity = null;
				switch (world.rand.nextInt(2)) {
					case 0: {
						entity = new EntityZombie(world);
						if(world.rand.nextInt(isHardMode() ? 3 : 12) == 0) {
							entity = new EntityWitch(world);
						}
						break;
					}
					case 1: {
						entity = new EntitySkeleton(world);
						if(world.rand.nextInt(8) == 0) {
							entity = new EntityWitherSkeleton(world);
						}
						break;
					}
					case 3: {
						if(!players.isEmpty()) {
							for(int j = 0; j < 1 + world.rand.nextInt(isHardMode() ? 8 : 5); j++) {
								EntityPixie pixie = new EntityPixie(world);
								pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
								pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
								pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);
								world.spawnEntity(pixie);
							}
						}
						break;
					}
				}
                //If entities are not immune to fire, make them immune
				if(entity != null) {
					if(!entity.isImmuneToFire())
						entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
					float range = 6F;
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                    //If in hardmode, give wither skeletons the goddess sword
					if(entity instanceof EntityWitherSkeleton && isHardMode()) 
                    {
						entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(LOZ.goddesssword.block));
					}
					world.spawnEntity(entity);
				}
			}
		}
	}
    
    //Teleporting functions
    private boolean teleportRandomly() 
    {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	private boolean teleportTo(double x, double y, double z) 
    {
		boolean flag = this.attemptTeleport(x, y, z);
		if (flag)
		{
			this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}
		return flag;
	}

	@Override
	public boolean attemptTeleport(double x, double y, double z) 
    {
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(this);
		World world = this.world;
		Random random = this.getRNG();

		if (world.isBlockLoaded(blockpos))
		{
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0)
			{
				BlockPos blockpos1 = blockpos.down();
				IBlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement())
				{
					flag1 = true;
				}
				else
				{
					--this.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1)
			{
				this.setPositionAndUpdate(this.posX, this.posY, this.posZ);

				if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox()))
				{
					flag = true;
				}

				// From Botania - Prevent out of bounds teleporting
				BlockPos source = getSource();
				if(tkxyooj.LOZ.common.core.MathHelper.pointDistanceSpace(posX, posY, posZ, source.getX(), source.getY(), source.getZ()) > 12)
					flag = false;
			}
		}
		if (!flag)
		{
			this.setPositionAndUpdate(d0, d1, d2);
			return false;
		}
		else
		{
			int i = 128;

			for (int j = 0; j < 128; ++j)
			{
				double d6 = (double)j / 127.0D;
				float f = (random.nextFloat() - 0.5F) * 0.2F;
				float f1 = (random.nextFloat() - 0.5F) * 0.2F;
				float f2 = (random.nextFloat() - 0.5F) * 0.2F;
				double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * (double)this.height;
				double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2, new int[0]);
			}
            
            // From Botania - damage any players in our way
			Vec3d origPos = new Vec3d(d0, d1 + height / 2, d2);
			Vec3d newPos = new Vec3d(posX, posY + height / 2, posZ);

			if(origPos.squareDistanceTo(newPos) > 1) {
				for(EntityPlayer player : getPlayersAround()) {
					RayTraceResult rtr = player.getEntityBoundingBox().grow(0.25).calculateIntercept(origPos, newPos);
					if(rtr != null)
						player.attackEntityFrom(DamageSource.causeMobDamage(this), 6);
				}
			}

			return true;
		}
	}
    
    //Spawning missiles
    private void spawnMissile() 
    {
		EntityMagicMissile missile = new EntityMagicMissile(this, true);
		missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1), posZ + (Math.random() - 0.5 * 0.1));
		if(missile.findTarget()) {
			world.spawnEntity(missile);
		}
	}
    
    //Set entity immunity
    @Override
	public boolean attackEntityFrom(DamageSource src, float damage) 
	{
		// Teleport if in a wall
        BlockPos source = getSource();
		if ("inWall".equals(src.getDamageType()) && getAttackTarget() != null) 
		{
            int tries = 0;
			while(!teleportRandomly() && tries < 50)
				tries++;
				if(tries >= 50)
					teleportTo(source.getX() + 0.5, source.getY() + 1.6, source.getZ() + 0.5);
		}

		//Immune to bow/arrow
		Entity entity = src.getImmediateSource();
		if (entity instanceof EntityArrow)
		{
			return false;
		}
        
        //Cap off attack damage to 50 damage points
		Entity e = source.getTrueSource();
		if (e instanceof EntityPlayer && isTruePlayer(e) && getInvulTime() == 0) 
        {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			int cap = 50;
			return super.attackEntityFrom(source, Math.min(cap, damage));
		}
		return false;
	}
    
    //Attacking entity makes it mad
    @Override
	protected void damageEntity(@Nonnull DamageSource par1DamageSource, float par2) {
		super.damageEntity(par1DamageSource, par2);

		Entity attacker = par1DamageSource.getImmediateSource();
		if(attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.subtract(playerVector).normalize().multiply(0.75);

			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
				tpDelay = 4;
				spawnPixies = aggro;
			}

			aggro = true;
		}
	}
    
    //Updating the AI
	@Override
	protected void updateAITasks() 
	{
		super.updateAITasks();

		if (getAttackTarget() == null) 
		{
			return;
		}

		//Cooldown of attacks
		if (attackCooldown > 0) 
		{
			attackCooldown--;
		}

		//Always watch our target
		this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 100F, 100F);
	}
    
    //functions needed for phases
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
    public boolean wantsNewClone(EntityTwilightPrincess clone) 
    {
		return clone.isShadowClone() && countMyClones() < EntityTwilightPrincess.MAX_SHADOW_CLONES;
	}

	public void setMaster(EntityTwilightPrincess midna) 
    {
		realMidna = midna;
	}

	public int countMyClones() 
    {
		// check if there are enough clones.  we check a 32x16x32 area
		int count = 0;

		for (EntityTwilightPrincess nearbyBoss : getNearbyBosses()) {
			if (nearbyBoss.isShadowClone() && nearbyBoss.getrealMidna() == this) {
				count++;
			}
		}

		return count;
	}

	public List<EntityTwilightPrincess> getNearbyBosses() 
    {
		return world.getEntitiesWithinAABB(getClass(), new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D));
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
    
    //Write to NBT data
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setByte("MinionsToSummon", getMinionsToSummon());
	}
    
    //Read from NBT data
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
    
    //On death
	@Override
	public void onDeath(DamageSource par1DamageSource) 
	{
		super.onDeath(par1DamageSource);
        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1D, 0D, 0D);

	}
    
    //Set death
    @Override
	public void setDead() 
    {
		world.playEvent(1010, getSource(), 0);
		super.setDead();
	}
    
    //Drop loot
    @Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, @Nonnull DamageSource source)
	{
		// Save true killer, they get extra loot
		if ("player".equals(source.getDamageType()) && source.getTrueSource() instanceof EntityPlayer) 
        {
			trueKiller = (EntityPlayer) source.getTrueSource();
		}

		// Drop equipment and clear it so multiple calls to super don't do it again
		super.dropEquipment(wasRecentlyHit, lootingModifier);

		for (EntityEquipmentSlot e : EntityEquipmentSlot.values()) 
        {
			setItemStackToSlot(e, ItemStack.EMPTY);
		}

		// Generate loot table for every single attacking player
		for (UUID u : playersWhoAttacked) 
        {
			EntityPlayer player = world.getPlayerEntityByUUID(u);
			if (player == null)
				continue;

			EntityPlayer saveLastAttacker = attackingPlayer;
			double savePosX = posX;
			double savePosY = posY;
			double savePosZ = posZ;

			attackingPlayer = player; // Fake attacking player as the killer
			posX = player.posX;       // Spoof pos so drops spawn at the player
			posY = player.posY;
			posZ = player.posZ;
			super.dropLoot(wasRecentlyHit, lootingModifier, DamageSource.causePlayerDamage(player));
			posX = savePosX;
			posY = savePosY;
			posZ = savePosZ;
			attackingPlayer = saveLastAttacker;
		}
		trueKiller = null;
	}
    
    //Returns that entity is a boss
	@Override
	public boolean isNonBoss() 
	{
		return false;
	}	
}
