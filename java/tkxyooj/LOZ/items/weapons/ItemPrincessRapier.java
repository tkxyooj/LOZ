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

public class ItemPrincessRapier {

    public ItemPrincessRapier() {
    }

    public static Item block;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT)
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation("lozmod:princessrapier", "inventory"));
        block.setCreativeTab(LOZ.tab);
        }

    static {
        Item.ToolMaterial enumt = EnumHelper.addToolMaterial("PRINCESSRAPIER", 2, 251, 6F, 6, 14);
        block = (Item) (new ItemSword(enumt) {

            public Set<String> getToolClasses(ItemStack stack) {
                HashMap<String, Integer> ret = new HashMap<String, Integer>();
                ret.put("sword", 1);
                return ret.keySet();
            }
        }).setUnlocalizedName("princessrapier");
        block.setRegistryName("princessrapier");
        
        ForgeRegistries.ITEMS.register(block);
    }
}