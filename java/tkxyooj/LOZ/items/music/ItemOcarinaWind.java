package tkxyooj.LOZ.items.music;

import tkxyooj.LOZ.LOZ;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.WorldServer;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;

public class ItemOcarinaWind {

	public ItemOcarinaWind() {
	}

	public static Item block;
	public static Object instance;

	public void load(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
					.register(block, 0, new ModelResourceLocation("LOZmod:OcarinaWind", "inventory"));
		block.setCreativeTab(LOZ.tab);
	}

	static {
		block = (new ItemocarinaWind());
	}

	static class ItemocarinaWind extends Item {

		public ItemocarinaWind() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("ocarinawind");
			setRegistryName("ocarinawind");
			ForgeRegistries.ITEMS.register(this);
			setCreativeTab(LOZ.tab);
		}
		
		public static final String INSTR_COOLDOWN_KEY = "cooldown";
		public static final String setmessage = "Warp point has been set!";
		public static final String warpmessage = "You have been teleported!";
		public static final String dimmessage = "Unable to teleport across dimensions!";
		public static double px;
		public static double py;
		public static double pz;
		public static int dim;
		public static BlockPos warppoint;
	    
		public void writePosToNBT(NBTTagCompound nbt) 
		{
			this.writePosToNBT(nbt);
			if(warppoint != null)
			{
				nbt.setDouble("WarpDim", (double) dim);
				nbt.setDouble("x", px);
				nbt.setDouble("y", py);
				nbt.setDouble("z", pz);
			}
		}

		public void readPosFromNBT(NBTTagCompound nbt) 
		{
			this.readPosFromNBT(nbt);
			dim = (int) setwarp(nbt.getDouble("WarpDim"));
			px = setwarp(nbt.getDouble("x"));
			py = setwarp(nbt.getDouble("y"));
			pz = setwarp(nbt.getDouble("z"));
		}
		
		public double setwarp(double data) 
		{
			return data;
		}
		
		@Override
	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	    {
	        ItemStack stack = player.getHeldItem(hand);
	        if (!world.isRemote)
	        {
	            if(player.isSneaking() == true)
	            {
	            	setWarpPoint(stack, player);
	            }
	            
	            if(player.isSneaking() == false)
	            {
	        	useInstrument(stack, player, 6000);
	            }
	        }
	        return new ActionResult <> (EnumActionResult.SUCCESS, stack);
	    }
		
	    protected void setWarpPoint(ItemStack stack, Entity entity) 
	    {
			//get player dimension and position
	    	World world = entity.world;
	    	dim = world.provider.getDimension();
	    	px = Math.round(entity.posX - .5);
	        py = Math.round(entity.posY - .5);
	        pz = Math.round(entity.posZ - .5);
	        warppoint = new BlockPos(px,py,pz);
	        //warp confirmation message
	        entity.sendMessage(new TextComponentString(setmessage));
	        return;
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

	    private void doMagic(ItemStack stack, Entity entity) 
	    {
	    	Entity player = entity;
	    	GoToWarpPoint(stack, (EntityPlayer) player);
		}
	    
	    public static void GoToWarpPoint(ItemStack stack, EntityPlayer player) 
	    {
	    	//check to see if warp point has been set
	    	if(warppoint == null)
	    	{
	    		player.sendMessage(new TextComponentString("Warp point has not been set! Set a warp point by sneaking and using the ocarina."));
	    		return;
	    	}
	    	
	    	if(warppoint != null)
	    	{
	    		//check dimension
	    		World world = player.world;
	    		int currentDim = world.provider.getDimension();
	    		if(currentDim == dim)
	    		{
	    			MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
	    			s.getCommandManager().executeCommand(s,  "/gamerule sendCommandFeedback false");
					s.getCommandManager().executeCommand( s, "/tp " + player.getName() + " " + px + " " + py + " " + pz );
					player.sendMessage(new TextComponentString(warpmessage));
		        	return;
	    		}
	    		//change to correct dimension
	    		else
	    		{
	    			player.sendMessage(new TextComponentString(dimmessage));
	    			return;
	    		}
	    		//teleport to coordinates
	    	}

	      }
	    
		ResourceLocation location = new ResourceLocation("LOZmod", "lozmod.music.sound.soaring");
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
	            if ((cooldown > 0) && warppoint == null)
	            {
	            	tag.setInteger(INSTR_COOLDOWN_KEY, 0);
	            }
	            
	            if((cooldown > 0) && warppoint != null)
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
