package tkxyooj.LOZ.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EntityAISwitchWeapons extends EntityAIBase
    {
    	EntitySkeleton mob;
    	EntityLivingBase target;
    	double minDistance;
    	double maxDistance;
    	ItemStack weaponOne;
    	ItemStack weaponTwo;
    	
    	public EntityAISwitchWeapons(EntitySkeleton entitymob, double minDistance, double maxDistance, ItemStack weaponOne, ItemStack weaponTwo) {
    		mob = entitymob;
    		this.minDistance = minDistance;
    		this.maxDistance = maxDistance;
    		this.weaponOne = weaponOne;
    		this.weaponTwo = weaponTwo;
		}

		/**
		* Returns whether the EntityAIBase should begin execution.
		*/
		public boolean shouldExecute()
		{
	        this.target = this.mob.getAttackTarget();

	        if (target == null)
	        {
	            return false;
	        }
	        else if (!target.isEntityAlive())
	        {
	            return false;
	        }
	        else
	        {
	        	if(((this.mob.getDistance(this.target) < minDistance && this.mob.getHeldItemMainhand() != weaponOne) || 
	        			(this.mob.getDistance(this.target) > maxDistance && this.mob.getHeldItemMainhand() != weaponTwo)) && this.mob.canEntityBeSeen(this.target))
	        	{
	        		return true;
	        	}
	        	
	        	return false;
	        }
		}
		
		/**
	    * Returns whether an in-progress EntityAIBase should continue executing
		*/
		public boolean continueExecuting()
	    {
			return shouldExecute();
	    }

	    /**
	     * Resets the task
	     */
	    public void resetTask()
	    {
	    	target = null;
	    }
	    
	    /**
	     * Updates the task
	     */
	    public void updateTask()
	    {
	    	if(this.mob.getDistance(this.target) < minDistance)
	    	{
	    		this.mob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponOne);
	    	}
	    	else if(this.mob.getDistance(this.target) > maxDistance)
	    	{
	    		this.mob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponTwo);
	    	}
	    }
    }