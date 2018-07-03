package tkxyooj.LOZ.items.weapons;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.world.World;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;

import java.util.Set;
import java.util.HashMap;

public class ItemMasterSword {

    public ItemMasterSword() {
    }

    public static Item block;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT)
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation("LOZmod:mastersword", "inventory"));
    block.setCreativeTab(LOZ.tab);
    }

    public boolean onBlockDestroyed(ItemStack item, World world, Block block, int x, int y, int z, EntityLivingBase player)
    {
          item.damageItem(0, player);
          return true;
    }
    
    static {
        Item.ToolMaterial enumt = EnumHelper.addToolMaterial("MASTERSWORD", 32, (int)Double.POSITIVE_INFINITY, 9999F, Float.POSITIVE_INFINITY, 1);
        block = (Item) (new ItemSword(enumt) {

            public Set<String> getToolClasses(ItemStack stack) {
                HashMap<String, Integer> ret = new HashMap<String, Integer>();
                ret.put("sword", 1);
                return ret.keySet();
            }
        }).setUnlocalizedName("mastersword");
        block.setMaxDamage(0);
        block.setRegistryName("mastersword");
        
        ForgeRegistries.ITEMS.register(block);
    }

    
}
