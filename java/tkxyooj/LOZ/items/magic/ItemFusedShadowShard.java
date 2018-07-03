package tkxyooj.LOZ.items.magic;

import tkxyooj.LOZ.LOZ;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincess;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;

public class ItemFusedShadowShard {

	public ItemFusedShadowShard() {
	}

	public static Item block;
	public static Object instance;

	public void load(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.register(block, 0, new ModelResourceLocation("LOZmod:fusedshadowshard", "inventory"));
		block.setCreativeTab(LOZ.tab);
	}

	static {
		block = (new ItemfusedshawdowShard());
	}

	static class ItemfusedshawdowShard extends Item {

		public ItemfusedshawdowShard() {
			setMaxDamage(0);
			maxStackSize = 16;
			setUnlocalizedName("fusedshadowshard");
			setRegistryName("fusedshadowshard");
			ForgeRegistries.ITEMS.register(this);
			setCreativeTab(LOZ.tab);
		}
		
		@Nonnull
		@Override
		public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
			ItemStack stack = player.getHeldItem(hand);

			if(player.isSneaking() == true)
				return EntityTwilightPrincess.spawn(player, stack, world, pos, stack.getItemDamage() == 14) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
//			else if(stack.getItemDamage() == 20 && net.minecraft.item.ItemDye.applyBonemeal(stack, world, pos, player, hand)) {
//				if(!world.isRemote)
//					world.playEvent(2005, pos, 0);
//
//				return EnumActionResult.SUCCESS;
//			}

			return super.onItemUse(player, world, pos, hand, side, par8, par9, par10);
		}
		
		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack par1ItemStack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1.0F;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack par1ItemStack) {
			return true;
		}
	}
}
