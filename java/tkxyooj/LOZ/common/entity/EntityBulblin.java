package tkxyooj.LOZ.common.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import tkxyooj.LOZ.common.entity.ai.EntityAISwitch;
import tkxyooj.LOZ.common.entity.ai.EntityAISwitchWeapons;

public class EntityBulblin extends EntityTwilitBulblin {

	public EntityBulblin(World worldIn) {
		super(worldIn);
	}
	
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAISwitch(this, 1.2D, 20, 15.0F));
        this.tasks.addTask(3, new EntityAISwitchWeapons(this, 5D, 6D, new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BOW)));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, true));
    }
    
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty())
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }
        
        return livingdata;
    }
    
    public ItemStack getBackItem()
    {
    	if(this.getHeldItemMainhand().getItem() == Items.IRON_SWORD)
    	{
    		return new ItemStack(Items.BOW);
    	}
    	else
    	{
    		return new ItemStack(Items.IRON_SWORD);
    	}
    }
    
    /**
     * sets this entity's combat AI.
     */
    @Override
    public void setCombatTask()
    {
        
    }
}