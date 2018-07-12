public class ItemHeartsContainer extends Item
{
	public ItemHeartsContainer() 
  	{
		super();
		setMaxStackSize(1);
		setUnlocalizedName("heartcontainer");
		setRegistryName("heartcontainer");
		ForgeRegistries.ITEMS.register(this);
	}

  	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
	{
		if (player.capabilities.isCreativeMode) 
		{
			return stack;
		}
		if (!world.isRemote && Config.enabledHealthModification()) 
		{
			if (Config.getMaxHearts() == 20 && player.getMaxHealth() < 20 )
			{
				--stack.stackSize;
				updateHealth(player);
			}
			if (Config.getMaxHearts() == 40 && player.getMaxHealth() < 40 )
			{
				--stack.stackSize;
				updateHealth(player);
			}
			if (Config.getMaxHearts() == (int)Double.POSITIVE_INFINITY )
			{
				--stack.stackSize;
				updateHealth(player);
			}
		}
		return stack;
	}
	
	public void updateHealth(EntityPlayer player, int amount)
	{
		int amount = 2; //2 health points to give player a full heart
		player.setMaxHealth(maxHealth + amount);
		player.heal(player.getMaxHealth());
	}
}
