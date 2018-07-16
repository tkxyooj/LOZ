package tkxyooj.LOZ.items.weapons;

import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import tkxyooj.LOZ.creativeTab.StartupCommon;

public class ItemPrincessRapier extends ItemSword
{
	public static final ToolMaterial material = EnumHelper.addToolMaterial("PRINCESSRAPIER", 2, 251, 6F, 6, 14);
    public ItemPrincessRapier() 
    {
    	super(material);
    	setCreativeTab(StartupCommon.LOZTab);
    }
}