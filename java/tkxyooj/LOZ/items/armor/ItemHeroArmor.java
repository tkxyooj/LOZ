package tkxyooj.LOZ.items.armor;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.util.EnumHelper;

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

public class ItemHeroArmor {

    public ItemHeroArmor() {
    }

    public static Item helmet;
    public static Item body;
    public static Item legs;
    public static Item boots;

    public void load(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmet, 0, new ModelResourceLocation("LOZmod:heroarmorhelmet", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(body, 0, new ModelResourceLocation("LOZmod:heroarmorbody", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(legs, 0, new ModelResourceLocation("LOZmod:heroarmorlegs", "inventory"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(boots, 0, new ModelResourceLocation("LOZmod:heroarmorboots", "inventory"));
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
        ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("HEROARMOR", "hero_tunic", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
        int armorPreffix = 0;

        helmet = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.HEAD) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {
                entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, (int) 3600, (int) 1));
            }
        }).setUnlocalizedName("heroarmorhelmet");
        helmet.setMaxStackSize(1);
        helmet.setMaxDamage(0);
        
        body = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.CHEST) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {
                entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int) 3600, (int) 1));
            }
        }).setUnlocalizedName("heroarmorbody");
        body.setMaxStackSize(1);
        body.setMaxDamage(0);
        
        legs = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.LEGS) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {}
        }).setUnlocalizedName("heroarmorlegs");
        legs.setMaxStackSize(1);
        legs.setMaxDamage(0);
        
        boots = (new ItemArmor(enuma, armorPreffix, EntityEquipmentSlot.FEET) {
            public void onArmorTick(World world, EntityPlayer entity, ItemStack itemstack) {}
        }).setUnlocalizedName("heroarmorboots");
        boots.setMaxStackSize(1);
        boots.setMaxDamage(0);
        
        helmet.setRegistryName("heroarmorhelmet");
        body.setRegistryName("heroarmorbody");
        legs.setRegistryName("heroarmorlegs");
        boots.setRegistryName("heroarmorboots");
        
        ForgeRegistries.ITEMS.register(helmet);
        ForgeRegistries.ITEMS.register(body);
        ForgeRegistries.ITEMS.register(legs);
        ForgeRegistries.ITEMS.register(boots);
    }
}
