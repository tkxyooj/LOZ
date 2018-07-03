package tkxyooj.LOZ.items.music;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;

public class ItemSunSongSheet {

	public ItemSunSongSheet() {
	}

	public static Item block;
	public static Object instance;

	public void load(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.register(block, 0, new ModelResourceLocation("LOZmod:SunSongSheet", "inventory"));
		block.setCreativeTab(LOZ.tab);
	}

	static {
		block = (new ItemsunSongSheet());
	}

	static class ItemsunSongSheet extends Item {

		public ItemsunSongSheet() {
			setMaxDamage(0);
			maxStackSize = 64;
			setUnlocalizedName("sunsongsheet");
			setRegistryName("sunsongsheet");
			ForgeRegistries.ITEMS.register(this);
			setCreativeTab(LOZ.tab);
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
