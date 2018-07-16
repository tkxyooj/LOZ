package tkxyooj.LOZ.items.magic;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;

public class ItemSpike extends Item 
{
	public ItemSpike() 
	{
		setMaxDamage(0);
		maxStackSize = 1;
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
