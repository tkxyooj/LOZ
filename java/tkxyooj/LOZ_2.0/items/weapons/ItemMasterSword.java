package tkxyooj.LOZ.items.weapons;

import net.minecraftforge.common.util.EnumHelper;
import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemMasterSword extends ItemSword
{
	public static final ToolMaterial material = EnumHelper.addToolMaterial("MASTERSWORD", 32, (int)Double.POSITIVE_INFINITY, 9999F, Float.POSITIVE_INFINITY, 1);
    public ItemMasterSword() 
    {
    	super(material);
    	this.setMaxDamage(0);
    	setCreativeTab(StartupCommon.LOZTab);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		if (entity instanceof EntityPlayer)
		{
			entity.setDead();
			return true;
		}
		else
		{
			return false;
		}
    }
}
