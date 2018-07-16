package tkxyooj.LOZ.items.weapons;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.item.ItemAxe;

public class ItemBulblinClub extends ItemAxe
{
	public static final ToolMaterial material = EnumHelper.addToolMaterial("CLUB", 2, 251, 20F, 9, 10);
    public ItemBulblinClub() 
    {
    	super(material, 9F, 20F);
    	setCreativeTab(StartupCommon.LOZTab);
    }
}