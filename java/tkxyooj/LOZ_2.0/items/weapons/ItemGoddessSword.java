package tkxyooj.LOZ.items.weapons;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.item.ItemSword;

public class ItemGoddessSword extends ItemSword
{
	public static final ToolMaterial material = EnumHelper.addToolMaterial("GODDESSSWORD", 3, 3124, 12F, 14, 10);
    public ItemGoddessSword() 
    {
    	super(material);
    	setCreativeTab(StartupCommon.LOZTab);
    }
}
