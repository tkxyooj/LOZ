package tkxyooj.LOZ.items.music;

import tkxyooj.LOZ.creativeTab.StartupCommon;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.state.IBlockState;

public class ItemOcarinaWind extends Item {

	public ItemOcarinaWind() {
		setMaxDamage(0);
		maxStackSize = 1;
		setCreativeTab(StartupCommon.LOZTab);
		}
		
		public static final String INSTR_COOLDOWN_KEY = "cooldown";
		public static final String worldwarpmessage = "Bed was not set. You have been teleported back to world spawn!";
		public static final String bedwarpmessage = "You have been teleported back to your bed!";
		public static final String dimmessage = "Unable to teleport across dimensions!";
		
		ResourceLocation location = new ResourceLocation("LOZmod", "lozmod.music.sound.soaring");
	    SoundEvent sound = new SoundEvent(location);
	    
		@Override
	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	    {
			player.world.playSound(player, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
	        world.spawnParticle(EnumParticleTypes.NOTE, true, player.posX, player.posY + 1.2, player.posZ, 6.0, 0.5, 0.5, 0);
			
	            ItemStack stack = player.getHeldItem(hand);
	        
	        int spawnDim = player.getSpawnDimension();
    		int playerDim = player.dimension;
    		
    		if(playerDim != spawnDim && !world.isRemote)
    		{
    			player.sendMessage(new TextComponentString(dimmessage));
    			return new ActionResult <> (EnumActionResult.FAIL, stack);
    		}
    			
	        if (!world.isRemote)
	        {
	        	useInstrument(stack, player, 6000);
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
	                        doMagic(stack, entity);
	                }
	                tag.setInteger(INSTR_COOLDOWN_KEY, cooldown);
	            }
	    }

		private void doMagic(ItemStack stack, Entity entity) 
	    {
	    	EntityPlayer player = (EntityPlayer) entity;
	    	BlockPos pos = player.getBedLocation(player.getSpawnDimension());
	    	String message = "Teleported!";
	    	
			if (pos != null) 
			{
				pos = EntityPlayer.getBedSpawnLocation(player.world, pos, player.isSpawnForced(player.getSpawnDimension()));
				message = bedwarpmessage;
			}
			if (pos == null) 
			{
				pos = player.world.getSpawnPoint();
				message = worldwarpmessage;
			}
			if (pos != null) 
			{
				if (player.isRidingOrBeingRiddenBy(null))
				{
					player.dismountEntity(null);
				}
				player.setPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D);
				while (!player.world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) 
				{
					player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
				}
				player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
				player.sendMessage(new TextComponentString(message));
			}
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
