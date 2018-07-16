package tkxyooj.LOZ.items.armor;

import tkxyooj.LOZ.creativeTab.StartupCommon;

import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDeityArmor extends ItemArmor 
{
	public static final ArmorMaterial deityarmor = EnumHelper.addArmorMaterial("DEITYARMOR", "deity_tunic", 9999, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0f);
	public final EntityEquipmentSlot slot;
	
    public ItemDeityArmor(EntityEquipmentSlot slot) 
    {
    	super(deityarmor, 0, slot);
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
    		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1, 1);
    		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1 ,1);
    		player.setAir(300);
    		player.getFoodStats().addStats(20, 20F);
    	}
    	else if (armorType == EntityEquipmentSlot.CHEST)
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1, 2);
    		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1, 2);
    		player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1, 2);
    		player.setHealth((float) Double.POSITIVE_INFINITY);
//    		player.capabilities.allowFlying = true;
            List<PotionEffect> effects = Lists.newArrayList(player.getActivePotionEffects());
            for (PotionEffect potion : Collections2.filter(effects, potion -> potion.getPotion().isBadEffect())) 
            {
            	player.removePotionEffect(potion.getPotion());
            }
    	}
    	else if (armorType == EntityEquipmentSlot.LEGS)
    	{
    		if (player.isBurning()) 
    		{
        		player.extinguish();
            }
    	}
    }
}
