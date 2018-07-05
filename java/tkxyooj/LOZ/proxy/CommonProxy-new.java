public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) 
    {
        ModItems.init();
        ModBlocks.init();
        ModEntities.init();
	ModSounds.init();
    
    //Sounf registration
    ResourceLocation healing = new ResourceLocation("lozmod", "lozmod.music.sound.healing");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(healing).setRegistryName(healing));
		ResourceLocation soaring = new ResourceLocation("lozmod", "lozmod.music.sound.soaring");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(soaring).setRegistryName(soaring));
		ResourceLocation storm = new ResourceLocation("lozmod", "lozmod.music.sound.storm");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(storm).setRegistryName(storm));
		ResourceLocation sun = new ResourceLocation("lozmod", "lozmod.music.sound.sun");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sun).setRegistryName(sun));
		ResourceLocation lullaby = new ResourceLocation("lozmod", "lozmod.music.sound.lullaby");
    ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(lullaby).setRegistryName(lullaby));
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
