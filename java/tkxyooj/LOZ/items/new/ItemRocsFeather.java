public class ItemRocsFeather extends Item
{
    public ItemRocsFeather() 
    {
        super();
        this.slot = slot;
        setCreativeTab(LOZ.tab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }
    
    @Override
    public void setMaxStackSize(int amount)
    {
        super.setMaxStackSize(1);
    }
    
    @Override
    public void setMaxDamage(int amount)
    {
        super.setMaxDamage(0);
    }
	
	@Override
	public void tick() 
	{
		boolean isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
		if((player.getHeldItemMainhand(this) || player.getHeldItemOffhand(this)) && !player.onGround) 
		{
    			if (isJumping && player.motionY < 0) 
        		{
				player.motionY = Math.min(player.motionY + 0.1, player.motionY * 0.5);
				player.fallDistance = 0;
			}
		}
	}
}
