package tkxyooj.LOZ.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tkxyooj.LOZ.items.StartupCommon;

// This creative tab is very similar to the basic CreativeTab, but overrides displayAllReleventItems to
//  customise the list of displayed items - filters through all the items looking for ones whose name starts
//  with "mbe"

public class AllLOZItemsTab extends CreativeTabs {
  public AllLOZItemsTab(String label) {
    super(label);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public ItemStack getTabIconItem() {
    return new ItemStack(StartupCommon.minitriforce);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void displayAllRelevantItems(NonNullList<ItemStack> itemsToShowOnTab)
  {
    for (Item item : Item.REGISTRY) {
      if (item != null) {
        if (item.getUnlocalizedName().contains(".loz")) {
          item.getSubItems(CreativeTabs.SEARCH, itemsToShowOnTab);  // CreativeTabs.SEARCH will find all items even if they belong to another tab
                                                                    //   except if the item has no tab (item.getCreativeTab() == NULL)
        }
      }
    }
  }

}
