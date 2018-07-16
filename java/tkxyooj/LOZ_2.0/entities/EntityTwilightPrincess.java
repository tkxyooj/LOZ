/*
 * Adapted from TeamTwilight's mod Twilight Forest Lich Boss class.
 * This includes not just the mob class but the mob ai classes as well.
 */

package tkxyooj.LOZ.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.entities.helper.Vector3;
import tkxyooj.LOZ.entities.projectiles.*;

public class EntityTwilightPrincess extends EntityMob 
{
	//Loot table
	public ResourceLocation LOOT_TABLE = new ResourceLocation(LOZ.MODID, "twilightprincess");

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
	
	private static final DataParameter<Integer> INVUL_TIME = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> HARD_MODE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BOOLEAN);
	private static final DataParameter<BlockPos> SOURCE = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<UUID>> BOSSINFO_ID = EntityDataManager.createKey(EntityTwilightPrincess.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private boolean aggro = false;
	private int tpDelay = 0;
	private int mobSpawnTicks = 0;
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.EntityTwilightPrincess.name"), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS).setCreateFog(true);;
	public long updateTick = 0;

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
		this.isImmuneToFire = true;
		this.experienceValue = 300;
	}
    
    //Returns number of active bosses. Used for spawn info so if a boss exist, another one cannot be summoned
    private static int getBossesAround(World world, BlockPos source) 
    {
		float range = 15F;
		List<EntityTwilightPrincess> l = world.getEntitiesWithinAABB(EntityTwilightPrincess.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
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
            e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP * playerCount);
            if (hard)
                e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).setBaseValue(15);
            e.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 10F, 0.1F);
            e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), null);
            world.spawnEntity(e);
            return true;
        }
        return false;
	}
    
    //Return loot table
    @Override
	public ResourceLocation getLootTable() 
    {
		return LOOT_TABLE;
    }
    
    //Initialize entity AI
    @Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, ARENA_RANGE));
		}
    
    //Initialize entity by registering data manager
    @Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(INVUL_TIME, 0);
		dataManager.register(SOURCE, BlockPos.ORIGIN);
		dataManager.register(HARD_MODE, false);
		dataManager.register(PLAYER_COUNT, 0);
		dataManager.register(BOSSINFO_ID, Optional.absent());
	}
    
    //Data manager sets
	public int getInvulTime() {return dataManager.get(INVUL_TIME);}
	public int getPlayerCount() {return dataManager.get(PLAYER_COUNT);}
	public BlockPos getSource() {return dataManager.get(SOURCE);}    
	public boolean isHardMode() {return dataManager.get(HARD_MODE);}
    
 	public void setInvulTime(int time) {dataManager.set(INVUL_TIME, time);}
	public void setPlayerCount(int count) {dataManager.set(PLAYER_COUNT, count);}
	public void setSource(BlockPos pos) {dataManager.set(SOURCE, pos);}
	public void setHardMode(boolean hardMode) {dataManager.set(HARD_MODE, hardMode);}

    //Apply entity attributes
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP);
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
			this.setDead();
			return 0;
		}
	}

	private int puppetprincesscount = 0;
	private int beastganoncount = 0;
	private int phantomganoncount = 0;
	private int pinmissiletimer = 100;
	
    //While entity is alive
	@Override
	public void onLivingUpdate() 
	{
		pinmissiletimer--;
		if (pinmissiletimer == 0)
		{
			this.explode();
			pinmissiletimer = 100;
		}
		//Recolors the health bar and summon based on health
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
				bossInfo.setColor(BossInfo.Color.PURPLE);
			}
			else if (this.getPhase() == 3)
			{
				bossInfo.setColor(BossInfo.Color.RED);
			}
		}
		super.onLivingUpdate();
		this.updateTick++;
		
		BlockPos source = getSource();
		int invul = getInvulTime();

		dataManager.set(BOSSINFO_ID, Optional.of(bossInfo.getUniqueId()));

        //Dismount player
		if(!getPassengers().isEmpty())
			dismountRidingEntity();

        //Kill entity is world is set to peaceful
		if(world.getDifficulty() == EnumDifficulty.PEACEFUL)
			this.setDead();

        //List all players in the area
		List<EntityPlayer> players = getPlayersAround();

        //if all players are dead in the area, entity dies
		if(players.isEmpty() && !world.playerEntities.isEmpty())
			this.setDead();
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
            //If entity is mad
			if(aggro) 
            {
				//health is less than 90%
				boolean pissed = getHealth() / getMaxHealth() < 0.9;
				if(pissed && mobSpawnTicks > 0 && puppetprincesscount == 0) 
				{
                    //Spawn puppet princess
					float range = 6F;
					EntityLiving entity = new EntityPuppetPrincess(world);
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY + 2, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                   	world.spawnEntity(entity);
					puppetprincesscount = 1;
				}
				//health is less than 60%
				boolean hurt = getHealth() / getMaxHealth() < 0.6;
				if(hurt && mobSpawnTicks > 0 && beastganoncount == 0) 
				{
                    //Spawn beast ganon
					float range = 6F;
					EntityLiving entity = new EntityBeastGanon(world);
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY + 2, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                   	world.spawnEntity(entity);
                    beastganoncount = 1;
				}
				//health is less than 30%
				boolean inpain = getHealth() / getMaxHealth() < 0.3;
				if(inpain && mobSpawnTicks > 0 && phantomganoncount == 0) 
				{
                    //Spawn phantomganon
					float range = 6F;
					EntityLiving entity = new EntityPhantomGanon(world);
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY + 2, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                   	world.spawnEntity(entity);
                    phantomganoncount = 1;
				}
                //health is less than 10%
				boolean dying = getHealth() / getMaxHealth() < 0.1;
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
                    //Spawn horde of mobs
					if(reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobSpawnTicks > MOB_SPAWN_END_TICKS && mobSpawnTicks % MOB_SPAWN_WAVE_TIME == 0) 
                    {
						spawnMobs(players);
					}
					mobSpawnTicks--;
					tpDelay = 10;
					setInvulTime(0);
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
					}
				}
			} 
            else 
            {
            	float range = 3F;
				players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
            	if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
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
		if(tkxyooj.LOZ.entities.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) 
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
		for(int pl = 0; pl < playerCount; pl++) 
		{
			for(int i = 0; i < 3 + world.rand.nextInt(2); i++) 
			{
				EntityLiving entity = null;
				switch (world.rand.nextInt(2)) {
					case 0: 
					{
						entity = new EntityZombie(world);
						if(world.rand.nextInt(isHardMode() ? 3 : 12) == 0) 
						{
							entity = new EntityVindicator(world);
						}
						break;
					}
					case 1: 
					{
						entity = new EntityTwilightPrincessMinion(world);
						if(world.rand.nextInt(8) == 0) 
						{
							entity = new EntityWitherSkeleton(world);
						}
						break;
					}
					case 3: 
					{
						entity = new EntityBlaze(world);
						if(world.rand.nextInt() == 0)
						{
							entity = new EntityIllusionIllager(world);
						}
						break;
					}
				}
                //If entities are not immune to fire, make them immune
				if(entity != null) 
				{
					if(!entity.isImmuneToFire())
						entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
					float range = 6F;
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                    //If in hardmode, give wither skeletons a diamond sword
					if(entity instanceof EntityWitherSkeleton && isHardMode()) 
                    {
						entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
					}
					world.spawnEntity(entity);
				}
			}
		}
	}
	
	private void explode()
    {
        if (!this.world.isRemote)
        {
        	this.arrowExplosionB();
            this.arrowExplosion(this.posX, this.posY + (double)(this.height / 16.0F), this.posZ);
        }
    }
	
	public void arrowExplosion(double x, double y, double z)
    {
    	this.world.playSound((EntityPlayer)null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x, y, z, 1.0D, 0.0D, 0.0D);
        
        for(int i = 0; i <= 20; i++)
        {
        	EntitySpike spike = new EntitySpike(world);
        	spike.setDamage(4);
        	
        	spike.motionX = (rand.nextDouble() * 6D - 3D) * 0.3D;
        	spike.motionZ = (rand.nextDouble() * 6D - 3D) * 0.3D;
        	
        	spike.setPosition(x, y + 0.85D, z);
        	
        	world.spawnEntity(spike);
        }
    }

    public void arrowExplosionB()
    {
    	float f3 = 4.0F * 2.0F;
        int k1 = MathHelper.floor(this.posX - (double)f3 - 1.0D);
        int l1 = MathHelper.floor(this.posX + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(this.posY - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(this.posY + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(this.posZ - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(this.posZ + (double)f3 + 1.0D);
    	List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions())
            {
                double d12 = entity.getDistance(this.posX, this.posY, this.posZ) / (double)f3;

                if (d12 <= 1.0D)
                {
                    double d5 = entity.posX - this.posX;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - this.posY;
                    double d9 = entity.posZ - this.posZ;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double)this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeThornsDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f3 + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase)
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityplayer = (EntityPlayer)entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            	Maps.<EntityPlayer, Vec3d>newHashMap().put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                        }
                    }
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
				if(tkxyooj.LOZ.entities.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.getX(), source.getY(), source.getZ()) > 12)
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
        
    //Set entity immunity
    @Override
	public boolean attackEntityFrom(@Nonnull DamageSource src, float damage) 
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
		Entity e = src.getTrueSource();
		if (e instanceof EntityPlayer && isTruePlayer(e) && getInvulTime() == 0) 
        {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			int cap = 5000;
			return super.attackEntityFrom(src, Math.min(cap, damage));
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
			}

			aggro = true;
		}
	}
        
    //Write to NBT data
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger(TAG_INVUL_TIME, getInvulTime());
		nbttagcompound.setBoolean(TAG_AGGRO, aggro);
		nbttagcompound.setInteger(TAG_MOB_SPAWN_TICKS, mobSpawnTicks);

		BlockPos source = getSource();
		nbttagcompound.setInteger(TAG_SOURCE_X, source.getX());
		nbttagcompound.setInteger(TAG_SOURCE_Y, source.getY());
		nbttagcompound.setInteger(TAG_SOURCE_Z, source.getZ());

		nbttagcompound.setBoolean(TAG_HARD_MODE, isHardMode());
		nbttagcompound.setInteger(TAG_PLAYER_COUNT, getPlayerCount());
	}
    
    //Read from NBT data
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{
		super.readEntityFromNBT(nbttagcompound);
		setInvulTime(nbttagcompound.getInteger(TAG_INVUL_TIME));
		aggro = nbttagcompound.getBoolean(TAG_AGGRO);
		mobSpawnTicks = nbttagcompound.getInteger(TAG_MOB_SPAWN_TICKS);

		int x = nbttagcompound.getInteger(TAG_SOURCE_X);
		int y = nbttagcompound.getInteger(TAG_SOURCE_Y);
		int z = nbttagcompound.getInteger(TAG_SOURCE_Z);
		setSource(new BlockPos(x, y, z));

		setHardMode(nbttagcompound.getBoolean(TAG_HARD_MODE));
		if(nbttagcompound.hasKey(TAG_PLAYER_COUNT))
			setPlayerCount(nbttagcompound.getInteger(TAG_PLAYER_COUNT));
		else setPlayerCount(1);

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
        
    //Returns that entity is a boss
	@Override
	public boolean isNonBoss() 
	{
		return false;
	}	
}