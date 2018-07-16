package tkxyooj.LOZ.items.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPegasusBoots extends ItemArmor
{
    public static final ArmorMaterial pegasusboots = EnumHelper.addArmorMaterial("PEGASUSBOOTS", "pegasus_boots", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
    public final EntityEquipmentSlot slot;
    
    public ItemPegasusBoots(EntityEquipmentSlot slot) 
    {
        super(pegasusboots, 0, slot);
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
    	if (armorType == EntityEquipmentSlot.FEET)
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, (int)3600, 5));
    		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, (int)3600, 5));
    	}
    }
    
    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) 
    {
        return false;
    }

}