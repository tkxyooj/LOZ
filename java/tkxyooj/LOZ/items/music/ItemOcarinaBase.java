package tkxyooj.LOZ.items.music;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;

public class ItemOcarinaBase {

	public ItemOcarinaBase() {
	}

	public static Item block;
	public static Object instance;

	public void load(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.register(block, 0, new ModelResourceLocation("LOZmod:OcarinaBase", "inventory"));
		block.setCreativeTab(LOZ.tab);
	}

	static {
		block = (new ItemocarinaBase());
	}

	static class ItemocarinaBase extends Item {

		public ItemocarinaBase() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("ocarinabase");
			setRegistryName("ocarinabase");
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
	}
}
