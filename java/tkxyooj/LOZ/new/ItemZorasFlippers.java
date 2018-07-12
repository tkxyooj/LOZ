public class ItemZorasFlippers extends ItemArmor
{
    public static final ArmorMaterial zorasflippers = EnumHelper.addArmorMaterial("ZORASFLIPPERS", "zoras_flippers", (int)Double.POSITIVE_INFINITY, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f);
    public final EntityEquipmentSlot slot;
    
    public ItemZorasFlippers(EntityEquipmentSlot slot) 
    {
        super(zorasflippers, 0, slot);
        this.slot = slot;
	this.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
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
    	player.addPotionEffect(new PotionEffect(MobEffects.AQUA_AFFINITY, (int) 3600, (int) 1));
	player.setAir(300);
        boolean flying = player.capabilities.isFlying;
        boolean swimming = player.isInsideOfMaterial(Material.water) || player.isInWater();
        
        if (swimming) 
        {
		    float speed = 0.15f * (swimming ? 1.2f : 1.0f); 
							
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
