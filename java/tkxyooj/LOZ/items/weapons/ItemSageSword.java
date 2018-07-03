package tkxyooj.LOZ.items.weapons;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import tkxyooj.LOZ.LOZ;

public class ItemSageSword {

    public ItemSageSword() {
    }

    public static Item block;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT)
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation("lozmod:guardiansword", "inventory"));
        block.setCreativeTab(LOZ.tab);
    }

    static {
        Item.ToolMaterial enumt = EnumHelper.addToolMaterial("GUARDIANSWORD", 3, 1561, 8F, 8, 10);
        block = (Item) (new ItemSword(enumt) {

            public Set<String> getToolClasses(ItemStack stack) {
                HashMap<String, Integer> ret = new HashMap<String, Integer>();
                ret.put("sword", 1);
                return ret.keySet();
            }
        }).setUnlocalizedName("guardiansword");
        block.setRegistryName("guardiansword");
        
        ForgeRegistries.ITEMS.register(block);
    }
}