package tkxyooj.LOZ.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tkxyooj.LOZ.entities.projectiles.EntityStaticFairy;
import tkxyooj.LOZ.items.StartupCommon;

public class EntityPuppetPrincess extends EntityGhast
	 {
	     public EntityPuppetPrincess(World worldIn)
	     {
	         super(worldIn);
	         this.setSize(0.6F, 1.8F);
	         this.isImmuneToFire = true;
	         this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(StartupCommon.princessrapier));
	         this.setHealth(20.0F);
	         this.experienceValue = 100;
	     }
	     
	     @Override
	     protected void entityInit()
	     {
	    	 super.entityInit();
	    	 this.dataManager.register(ATTACKING, Boolean.valueOf(false));
	     }
	     
	     @Override
	     protected void initEntityAI()
	     {
	         this.tasks.addTask(5, new AIRandomFly(this));
	         this.tasks.addTask(7, new AILookAround(this));
	         this.tasks.addTask(7, new AIFireballAttack(this));
	         this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
	     }

	     /**
	      * Called when the mob's health reaches 0.
	      */
	     public void onDeath(DamageSource cause)
	     {
	         super.onDeath(cause);
	     }

	     @Override
	     public float getEyeHeight()
	     {
	         return 1.62F;
	     }

	     @Override
	     public boolean isImmuneToExplosions()
	     {
	    	 return true;
	     }
	     
	     @Override
	     public boolean canDropLoot()
	     {
	    	 return false;
	     }
	     
	     //Internal AI
	     //Ghast AI
	     @SideOnly(Side.CLIENT)
	     public boolean isAttacking()
	     {
	         return ((Boolean)this.dataManager.get(ATTACKING)).booleanValue();
	     }

	     private static final DataParameter<Boolean> ATTACKING = EntityDataManager.<Boolean>createKey(EntityPuppetPrincess.class, DataSerializers.BOOLEAN);
	     /** The explosion radius of spawned fireballs. */
	     private int explosionStrength = 1;
	     public void setAttacking(boolean attacking)
	     {
	    	 this.dataManager.set(ATTACKING, Boolean.valueOf(attacking));
	     }

	     public int getFireballStrength()
	     {
	    	 return this.explosionStrength;
	     }
	     static class AIRandomFly extends EntityAIBase
	     {
            private final EntityPuppetPrincess parentEntity;

            public AIRandomFly(EntityPuppetPrincess ghast)
            {
                this.parentEntity = ghast;
                this.setMutexBits(1);
            }

            /**
             * Returns whether the EntityAIBase should begin execution.
             */
            public boolean shouldExecute()
            {
                EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

                if (!entitymovehelper.isUpdating())
                {
                    return true;
                }
                else
                {
                    double d0 = entitymovehelper.getX() - this.parentEntity.posX;
                    double d1 = entitymovehelper.getY() - this.parentEntity.posY;
                    double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    return d3 < 1.0D || d3 > 3600.0D;
                }
            }

            /**
             * Returns whether an in-progress EntityAIBase should continue executing
             */
            public boolean shouldContinueExecuting()
            {
                return false;
            }

            /**
             * Execute a one shot task or start executing a continuous task
             */
            public void startExecuting()
            {
                Random random = this.parentEntity.getRNG();
                double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
            }
        }
	    
	    static class AILookAround extends EntityAIBase
        {
            private final EntityPuppetPrincess parentEntity;

            public AILookAround(EntityPuppetPrincess ghast)
            {
                this.parentEntity = ghast;
                this.setMutexBits(2);
            }

            /**
             * Returns whether the EntityAIBase should begin execution.
             */
            public boolean shouldExecute()
            {
                return true;
            }

            /**
             * Keep ticking a continuous task that has already been started
             */
            public void updateTask()
            {
                if (this.parentEntity.getAttackTarget() == null)
                {
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
                else
                {
                    EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
//                    double d0 = 64.0D;

                    if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D)
                    {
                        double d1 = entitylivingbase.posX - this.parentEntity.posX;
                        double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                        this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                        this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                    }
                }
            }
        }
	    static class AIFireballAttack extends EntityAIBase
        {
            private final EntityPuppetPrincess parentEntity;
            public int attackTimer;

            public AIFireballAttack(EntityPuppetPrincess ghast)
            {
                this.parentEntity = ghast;
            }

            /**
             * Returns whether the EntityAIBase should begin execution.
             */
            public boolean shouldExecute()
            {
                return this.parentEntity.getAttackTarget() != null;
            }

            /**
             * Execute a one shot task or start executing a continuous task
             */
            public void startExecuting()
            {
                this.attackTimer = 0;
            }

            /**
             * Reset the task's internal state. Called when this task is interrupted by another one
             */
            public void resetTask()
            {
                this.parentEntity.setAttacking(false);
            }

            /**
             * Keep ticking a continuous task that has already been started
             */
            public void updateTask()
            {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
 //               double d0 = 64.0D;

                if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(entitylivingbase))
                {
                    World world = this.parentEntity.world;
                    ++this.attackTimer;

                    if (this.attackTimer == 10)
                    {
                        world.playEvent((EntityPlayer)null, 1015, new BlockPos(this.parentEntity), 0);
                    }

                    if (this.attackTimer == 20)
                    {
//                        double d1 = 4.0D;
                        Vec3d vec3d = this.parentEntity.getLook(1.0F);
                        double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.x * 4.0D);
                        double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F));
                        double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.z * 4.0D);
                        world.playEvent((EntityPlayer)null, 1016, new BlockPos(this.parentEntity), 0);
                        EntityStaticFairy entitylargefireball = new EntityStaticFairy(world, this.parentEntity, d2, d3, d4);
                        entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                        entitylargefireball.posX = this.parentEntity.posX + vec3d.x * 4.0D;
                        entitylargefireball.posY = this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F) + 0.5D;
                        entitylargefireball.posZ = this.parentEntity.posZ + vec3d.z * 4.0D;
                        world.spawnEntity(entitylargefireball);
                        this.attackTimer = -40;
                    }
                }
                else if (this.attackTimer > 0)
                {
                    --this.attackTimer;
                }

                this.parentEntity.setAttacking(this.attackTimer > 10);
            }
        }
	    private int countdown = 200;
	    private int max_minions = 4;
	    public int countMyMinions() 
	    {
			return (int) world.getEntitiesWithinAABB(EntityVex.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(64.0D, 32.0D, 64.0D)).stream().count();
	    }
	    
	    public void onLivingUpdate()
	    {
	        super.onLivingUpdate();
	        
	        countdown--;
	        if (countdown == 0 && !world.isRemote)
	        {
	        	if (countMyMinions() < max_minions)
	        	{
	        		float range = 2F;
	        		EntityVex minion = new EntityVex(world);
	        		minion.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
	        		minion.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(minion)), null);
                   	world.spawnEntity(minion);
	        		countdown = 200;
	        	}
	        }
	    }
	    
	    private final List<UUID> playersWhoAttacked = new ArrayList<>();
	    @Override
	    public boolean attackEntityFrom(DamageSource source, float amount)
	    {
	    	Entity entity = source.getImmediateSource();
	    	//immune to explosion
	        if (source.isExplosion())
	        {
	            return false;
	        }
	        //Immune to fireball
			if (entity instanceof EntityFireball)
			{
				return false;
			}
	        //Immune to fire based damage
			if (source.isFireDamage())
			{
				return false;
			}
			//Immune to bow/arrow
			if (entity instanceof EntityArrow)
			{
				return super.attackEntityFrom(source, amount);
			}
	        
	        //Cap off attack damage to 2 damage points
			Entity e = source.getTrueSource();
			if (e instanceof EntityPlayer) 
	        {
				EntityPlayer player = (EntityPlayer) e;

				if(!playersWhoAttacked.contains(player.getUniqueID()))
					playersWhoAttacked.add(player.getUniqueID());

				int cap = 2;
				return super.attackEntityFrom(source, Math.min(cap, amount));
			}
			return false;
	    }

	 }
