public class ItemHearts extends Item
{
	public ItemPickupOnly() 
  {
		super();
		setMaxStackSize(1);
	}

  @Override
	public boolean onPickupItem(ItemStack stack, EntityPlayer player) 
  {
			if (player.getHealth() < player.getMaxHealth() || Config.alwaysPickupHearts()) 
      {
			  player.heal(1.0F);
			  --stack.stackSize;
				return true;
			}
			return false;
	}
}
