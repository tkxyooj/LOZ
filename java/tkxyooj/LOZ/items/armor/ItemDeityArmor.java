package tkxyooj.LOZ.items.armor;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDeityArmor {
	
    public ItemDeityArmor() {
    }
    
    public static Item helmet;
    public static Item body;
    public static Item legs;
    public static Item boots;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmet, 0, new ModelResourceLocation("LOZmod:deityarmorhelmet", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(body, 0, new ModelResourceLocation("LOZmod:deityarmorbody", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(legs, 0, new ModelResourceLocation("LOZmod:deityarmorlegs", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(boots, 0, new ModelResourceLocation("LOZmod:deityarmorboots", "inventory"));
        }
        helmet.setCreativeTab(LOZ.tab);
        body.setCreativeTab(LOZ.tab);
        legs.setCreativeTab(LOZ.tab);
        boots.setCreativeTab(LOZ.tab);
    }

    public void serverLoad(FMLServerStartingEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void registerRenderers() {
    }
    
    static {
        ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("DEITYARMOR", "deity_tunic", 9999, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.0f);
        int armorPreffix = 0;
        
        helmet = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.HEAD) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {
                entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, (int) 3600, (int) 1));
                entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,(int)3600,(int) 1));
                entity.setAir(300);
                entity.getFoodStats().addStats(20, 20F);
            }
        }).setUnlocalizedName("deityarmorhelmet");
        helmet.setMaxStackSize(1);
        helmet.setMaxDamage(0);
        
        

        body = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.CHEST) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {
                entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, (int)3600, (int) Double.POSITIVE_INFINITY));
                entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int) 3600, (int) 1));
                entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, (int) 3600, (int) 1));
                entity.setHealth((float) Double.POSITIVE_INFINITY);
                entity.capabilities.allowFlying = true;
                List<PotionEffect> effects = Lists.newArrayList(entity.getActivePotionEffects());
                for (PotionEffect potion : Collections2.filter(effects, potion -> potion.getPotion().isBadEffect())) {
                	entity.removePotionEffect(potion.getPotion());
                }
            }
        }).setUnlocalizedName("deityarmorbody");
        body.setMaxStackSize(1);
        body.setMaxDamage(0);

        legs = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.LEGS) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {
            	//entity.setAbsorptionAmount((float) Double.POSITIVE_INFINITY);
            	if (entity.isBurning()) {
            		entity.extinguish();
                }
            }
        }).setUnlocalizedName("deityarmorlegs");
        legs.setMaxStackSize(1);
        legs.setMaxDamage(0);

        boots = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.FEET) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {}
        }).setUnlocalizedName("deityarmorboots");
        boots.setMaxStackSize(1);
        boots.setMaxDamage(0);
        
        helmet.setRegistryName("deityarmorhelmet");
        body.setRegistryName("deityarmorbody");
        legs.setRegistryName("deityarmorlegs");
        boots.setRegistryName("deityarmorboots");
        
        ForgeRegistries.ITEMS.register(helmet);
        ForgeRegistries.ITEMS.register(body);
        ForgeRegistries.ITEMS.register(legs);
        ForgeRegistries.ITEMS.register(boots);
    }
}
