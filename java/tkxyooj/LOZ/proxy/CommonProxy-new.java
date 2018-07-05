public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) 
    {
        ModItems.init();
        ModBlocks.init();
        ModEntities.init();
	ModSounds.init();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void addModelRegister(IModelRegister register) {

    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return true;
    }

    public World getClientWorld() {
        return null;
    }

}
