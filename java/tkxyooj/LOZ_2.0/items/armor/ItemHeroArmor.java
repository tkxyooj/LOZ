package tkxyooj.LOZ.items.armor;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemHeroArmor extends ItemArmor 
{
	public static final ArmorMaterial heroarmor = EnumHelper.addArmorMaterial("HEROARMOR", "hero_tunic", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
	public final EntityEquipmentSlot slot;
	
    public ItemHeroArmor(EntityEquipmentSlot slot) 
    {
    	super(heroarmor, 0, slot);
    	this.slot = slot;
    	this.setMaxDamage(0);
    	this.setMaxStackSize(1);
    	setCreativeTab(StartupCommon.LOZTab);
    }
    
    @Override
    public void setDamage(ItemStack stack, int damage) 
    {
        super.setDamage(stack, 0);
    }
    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) 
    {
    	if (armorType == EntityEquipmentSlot.HEAD)
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.HASTE, (int) 3600, (int) 1));
    	}
    	else if (armorType == EntityEquipmentSlot.CHEST)
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int)3600, (int) 1));
    	}
    }
}