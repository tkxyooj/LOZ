public class ItemRocsFeather
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
    
	public int getMaxFlightTime() 
    {
	    return 100;
	}

	boolean isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
	if (!player.onGround) 
    {
		if (!isJumping) 
        {
			hasHadBreakTime = true;
		}
		if (!hasHadBreakTime) 
        {
			if (tickFlight > 0) 
            {
				tickFlight = MathHelper.clamp(tickFlight - 4, 0, getMaxFlightTime());
			}
		}
    	if (ClientPower.isPowered() && isJumping && hasHadBreakTime && player.motionY < 0 && tickFlight < getMaxFlightTime()) 
        {
			tickFlight++;
			player.motionY = Math.min(player.motionY + 0.1, player.motionY * 0.5);
			player.fallDistance = 0;
			NetworkHandler.sendPacketToServer(new PacketFlightData(player));
		}
	} 
	
    @Override
	public void tick() 
    {
		if (getPlayer().onGround && tickFlight > 0)
	    tickFlight = MathHelper.clamp(tickFlight - 4, 0, getMaxFlightTime());
    }
}
