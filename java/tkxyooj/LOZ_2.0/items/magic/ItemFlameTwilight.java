package tkxyooj.LOZ.items.magic;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;

public class ItemFlameTwilight extends Item 
{
	public ItemFlameTwilight() 
	{
		setMaxDamage(0);
		maxStackSize = 64;
		setCreativeTab(StartupCommon.LOZTab);
	}

	@Override
	public int getItemEnchantability() 
	{
		return 0;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) 
	{
		return 0;
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) 
	{
			return 1.0F;
	}
}
