package tkxyooj.LOZ.items.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tkxyooj.LOZ.creativeTab.StartupCommon;

public class ItemRocsFeather extends Item
{
    public ItemRocsFeather() 
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
        setCreativeTab(StartupCommon.LOZTab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		EntityPlayer player = (EntityPlayer) entityIn;
		boolean isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
		if((player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack) && !player.onGround) 
		{
    			if (isJumping && player.motionY < 0) 
        		{
				player.motionY = Math.min(player.motionY + 0.1, player.motionY * 0.5);
				player.fallDistance = 0;
			}
		}
	}
}