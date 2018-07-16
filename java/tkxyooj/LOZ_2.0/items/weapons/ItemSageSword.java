package tkxyooj.LOZ.items.weapons;

import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import tkxyooj.LOZ.creativeTab.StartupCommon;

public class ItemSageSword extends ItemSword
{
	public static final ToolMaterial material = EnumHelper.addToolMaterial("SAGESWORD", 3, 1561, 8F, 8, 10);
    public ItemSageSword() 
    {
    	super(material);
		setCreativeTab(StartupCommon.LOZTab);
    }
}