@Mod(modid = LOZ.MODID, name = LOZ.NAME, version = LOZ.VERSION)
public class LOZ
{
    public static final String MODID = "lozmod";
    public static final String NAME = "LOZ Mod";
    public static final String VERSION = "2.0.5";
    
    @SidedProxy(clientSide = "tkxyooj.LOZ.proxy.ClientProxy", serverSide = "tkxyooj.LOZ.proxy.CommonProxy")
	  public static CommonProxy proxy;
    
    @Mod.Instance(MODID)
    public static LOZ instance;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);
        
        //register ore dictionary as needed
        
        //creative tab
        public static final CreativeTabs tab = new CreativeTabs(LOZ.MODID)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() 
            {
                return new ItemStack(ItemMiniTriforce.block);
            }
        };
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeManager.init();
    }
}
