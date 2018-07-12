public class ItemPegasusBoots extends ItemArmor
{
    public static final ArmorMaterial pegasusboots = EnumHelper.addArmorMaterial("PEGASUSBOOTS", "pegasus_boots", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
    public final EntityEquipmentSlot slot;
    
    public ItemPegasusBoots(EntityEquipmentSlot slot) 
    {
        super(pegasusboots, 0, slot);
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
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) 
    {
        boolean flying = player.capabilities.isFlying;
		if (player.onGround || flying) 
        {
			boolean sneaking = player.isSneaking();
			float speed = 0.15f 
			* (flying ? 1.1f : 1.0f) 
			* (sneaking ? 0.1f : 1.0f); 
							
			if (player.moveForward > 0f) 
            {
				player.moveFlying(0f, 1f, speed);
			} 
            else if (player.moveForward < 0f) 
            {
				player.moveFlying(0f, 1f, -speed * 0.3f);
			}
            
			if (player.moveStrafing != 0f) 
            {
				player.moveFlying(1f, 0f, speed * 0.5f * Math.signum(player.moveStrafing));
			}
        }
    }
    
    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) 
    {
        return false;
    }

}
