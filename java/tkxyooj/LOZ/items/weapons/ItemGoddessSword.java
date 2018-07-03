package tkxyooj.LOZ.items.weapons;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.HashMap;

public class ItemGoddessSword {

    public ItemGoddessSword() {
    }

    public static Item block;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT)
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation("LOZmod:goddesssword", "inventory"));
        block.setCreativeTab(LOZ.tab);
        }

    static {
        Item.ToolMaterial enumt = EnumHelper.addToolMaterial("GODDESSSWORD", 3, 3124, 12F, 14, 10);
        block = (Item) (new ItemSword(enumt) {

            public Set<String> getToolClasses(ItemStack stack) {
                HashMap<String, Integer> ret = new HashMap<String, Integer>();
                ret.put("sword", 1);
                return ret.keySet();
            }
        }).setUnlocalizedName("goddesssword");
        block.setRegistryName("goddesssword");
        
        ForgeRegistries.ITEMS.register(block);
    }
}
