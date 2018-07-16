package tkxyooj.LOZ.items.magic;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import tkxyooj.LOZ.entities.EntityTwilightPrincess;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;

public class ItemFusedShadowShard extends Item 
{
	public ItemFusedShadowShard() 
	{
		setMaxDamage(0);
		maxStackSize = 16;
		setCreativeTab(StartupCommon.LOZTab);
	}
		
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) 
	{
		ItemStack stack = player.getHeldItem(hand);

		if(player.isSneaking() == true)
		{
			return EntityTwilightPrincess.spawn(player, stack, world, pos, stack.getItemDamage() == 14) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
			return super.onItemUse(player, world, pos, hand, side, par8, par9, par10);
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
