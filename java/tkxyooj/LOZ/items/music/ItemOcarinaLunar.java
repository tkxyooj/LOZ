package tkxyooj.LOZ.items.music;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;

public class ItemOcarinaLunar {

	public ItemOcarinaLunar() {
	}

	public static Item block;
	public static Object instance;

	public void load(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.register(block, 0, new ModelResourceLocation("LOZmod:OcarinaLunar", "inventory"));
		block.setCreativeTab(LOZ.tab);
	}

	static {
		block = (new ItemocarinaLunar());
	}

	static class ItemocarinaLunar extends Item {

		public ItemocarinaLunar() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("ocarinalunar");
			setRegistryName("ocarinalunar");
			ForgeRegistries.ITEMS.register(this);
			setCreativeTab(LOZ.tab);
		}
		
		public static final String INSTR_COOLDOWN_KEY = "cooldown";
	    	    
		@Override
	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	    {
	        ItemStack stack = player.getHeldItem(hand);
	        if (!world.isRemote)
	        {
	            useInstrument(stack, player, 600);

	        }
	        return new ActionResult <> (EnumActionResult.SUCCESS, stack);
	    }
		
	    protected void useInstrument(ItemStack stack, Entity entity, int cooldown)
	        {
	            if(!stack.hasTagCompound())
	                stack.setTagCompound(new NBTTagCompound());
	            NBTTagCompound tag = stack.getTagCompound();

	            if(tag.getInteger(INSTR_COOLDOWN_KEY) == 0)
	            {
	                if(!entity.world.isRemote)
	                {
	                        playMusic(stack, entity);
	                        doMagic(stack, entity);
	                }
	                tag.setInteger(INSTR_COOLDOWN_KEY, cooldown);
	            }
	    }
	    
	    public static final int day = 1000;
	    public static final int night = 13000;
	    public static final String daymessage = "You have brought forth the light of day!";
	    public static final String nightmessage = "You have brought forth the darkness of night!";

	    private void doMagic(ItemStack stack, Entity entity) 
	    {
	    	//if day, then night
	    	if(entity.world.isDaytime() == true)
	    	{
	    		entity.world.setWorldTime(night);
	    	   	entity.sendMessage(new TextComponentString(nightmessage));
	    	}
			//if night, then day
	    	else if (entity.world.isDaytime() == false)
	    	{
	    		entity.world.setWorldTime(day);
	    		entity.sendMessage(new TextComponentString(daymessage));
	    	}
		}

		ResourceLocation location = new ResourceLocation("LOZmod", "lozmod.music.sound.sun");
	    SoundEvent sound = new SoundEvent(location);
	    
	    protected void playMusic(ItemStack stack, Entity entity)
	    {
	    	spawnParticles((WorldServer) entity.world, entity.posX, entity.posY, entity.posZ, false);
	        //The first parameter to playSound has to be null rather than entity, otherwise they will be unable to hear it.
	        if(sound != null)
	            entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
	    }
	    
	    protected void spawnParticles(WorldServer world, double x, double y, double z, boolean firework)
	    {
	        world.spawnParticle(EnumParticleTypes.NOTE, x, y + 1.2, z, 6, 0.5, 0.0, 0.5, 0.0);
	        if(firework)
	            world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x, y + 1.2, z, 8, 0.5, 0.0, 0.5, 0.0);
	    }
	    
	    @Override
	    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected)
	    {
	        if(!world.isRemote && slot < InventoryPlayer.getHotbarSize())
	        {
	            NBTTagCompound tag = stack.getTagCompound();
	            if(tag == null)
	                return;

	            int cooldown = tag.getInteger(INSTR_COOLDOWN_KEY);
	            if(cooldown > 0)
	            {
	                cooldown--;
	                tag.setInteger(INSTR_COOLDOWN_KEY, cooldown);
	            }
	        }
	    }
	    
	    @Override
	    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	    {
	        return slotChanged || !ItemStack.areItemsEqual(oldStack, newStack);
	    }

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack par1ItemStack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1.0F;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean hasEffect(ItemStack par1ItemStack) {
			return true;
		}
	}
}
