package tkxyooj.LOZ.items.armor;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.common.util.EnumHelper;

public class ItemZorasFlippers extends ItemArmor
{
    public static final ArmorMaterial zorasflippers = EnumHelper.addArmorMaterial("ZORASFLIPPERS", "zora_flippers", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
    public final EntityEquipmentSlot slot;
    
    public ItemZorasFlippers(EntityEquipmentSlot slot) 
    {
        super(zorasflippers, 0, slot);
        this.slot = slot;
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        setCreativeTab(StartupCommon.LOZTab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }
    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) 
    {
    	if (armorType == EntityEquipmentSlot.FEET)
    	{
    		player.setAir(300);
    		if (player.isInsideOfMaterial(Material.WATER) || player.isInWater())
    		{
    			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, (int)3600, 5));
    		}
    		else
    		{
    			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, (int)3600, -1));
    		}
    	}
    	else
    	{
    		player.removePotionEffect(MobEffects.SPEED);
    	}
    }
    
    @Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) 
    {
		if (isInCreativeTab(tab)) 
		{
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
			list.add(istack);
		}
    }
    
    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) 
    {
        return false;
    }

}