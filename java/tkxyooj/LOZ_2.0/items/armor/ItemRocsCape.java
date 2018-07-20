public class ItemRocsCape extends ItemBauble implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LOZ.MODID,"rocs_cape");

	@SideOnly(Side.CLIENT)
	private static ModelCloak model;

	public ItemRocsCape() {
		super();
        this.setMaxDamage(0);
    	this.setMaxStackSize(1);
        setCreativeTab(StartupCommon.LOZTab);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		player.capabilities.allowFlying = true;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}

	@SideOnly(Side.CLIENT)
	ResourceLocation getCloakTexture() {
		return texture;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Helper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.translate(0F, armor ? -0.07F : -0.01F, 0F);

			float s = 1F / 16F;
			GlStateManager.scale(s, s, s);
			if(model == null)
				model = new ModelCloak();

			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();

			Minecraft.getMinecraft().renderEngine.bindTexture(getCloakTexture());
			model.render(1F);
		}
	}
}
