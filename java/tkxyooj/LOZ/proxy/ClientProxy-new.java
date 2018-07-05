public class ClientProxy extends CommonProxy 
{

    @Override
    public void preInit(FMLPreInitializationEvent event) 
    {

        super.preInit(event);
        
        TextureUtils.addIconRegister(new AvaritiaTextures());
        MinecraftForge.EVENT_BUS.register(new AvaritiaClientEventHandler());

        {
            ModelResourceLocation dietyhelmet = new ModelResourceLocation(LOZ.MODID, "location");
            ModelLoader.registerItemVariants(ModItems.infinity_helmet, dietyhelmet);
            ModelLoader.setCustomMeshDefinition(ModItems.infinity_helmet, (ItemStack stack) -> dietyhelmet);
        }
        registerRenderers();
    }

    @SuppressWarnings ("unchecked")
    @Override
    public void postInit(FMLPostInitializationEvent event) 
    {
        super.postInit(event);

        RenderManager manager = Minecraft.getMinecraft().getRenderManager();

        Render<EntityItem> render = (Render<EntityItem>) manager.entityRenderMap.get(EntityItem.class);
        if (render == null) 
        {
            throw new RuntimeException("EntityItem does not have a Render bound... This is likely a bug..");
        }
        manager.entityRenderMap.put(EntityItem.class, new WrappedEntityItemRenderer(manager, render));
    }

    private void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTwilightPrincess.class, RenderTwilightPrincess::new);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }
}
