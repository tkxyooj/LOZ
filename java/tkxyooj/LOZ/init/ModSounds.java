public class ModSounds
{
    public static void init()
    {
        //Resource location
        ResourceLocation healing = new ResourceLocation("lozmod", "lozmod.music.sound.healing");
	ResourceLocation soaring = new ResourceLocation("lozmod", "lozmod.music.sound.soaring");
	ResourceLocation storm = new ResourceLocation("lozmod", "lozmod.music.sound.storm");
	ResourceLocation sun = new ResourceLocation("lozmod", "lozmod.music.sound.sun");
	ResourceLocation lullaby = new ResourceLocation("lozmod", "lozmod.music.sound.lullaby");
        
        //Registers sound
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(healing).setRegistryName(healing));
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(soaring).setRegistryName(soaring));
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(storm).setRegistryName(storm));
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sun).setRegistryName(sun));
        ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(lullaby).setRegistryName(lullaby));
    }
}
