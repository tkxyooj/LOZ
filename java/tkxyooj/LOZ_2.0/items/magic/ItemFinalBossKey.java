package tkxyooj.LOZ.items.magic;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;

public class ItemFinalBossKey extends Item 
{

	public ItemFinalBossKey() 
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) 
	{
		return true;
	}
}
