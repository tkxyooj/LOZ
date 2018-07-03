package tkxyooj.LOZ;

import tkxyooj.LOZ.client.render.entity.RenderTwilightPrincess;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincess;
import tkxyooj.LOZ.common.entity.ModEntities;
import tkxyooj.LOZ.items.armor.*;
import tkxyooj.LOZ.items.magic.*;
import tkxyooj.LOZ.items.material.*;
import tkxyooj.LOZ.items.medallions.*;
import tkxyooj.LOZ.items.music.*;
import tkxyooj.LOZ.items.weapons.*;
import tkxyooj.LOZ.proxy.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


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
    
    //blocks
    //public static BlockBoundChest boundchest = new BlockBoundChest();
    //item-armor
    public static ItemDeityArmor deityarmor = new ItemDeityArmor();
    public static ItemGoronArmor goronarmor = new ItemGoronArmor();
    public static ItemHeroArmor heroarmor = new ItemHeroArmor();
    public static ItemZoraArmor zoraarmor = new ItemZoraArmor();
	//magic
	public static ItemAncientTablet ancienttablet = new ItemAncientTablet();
	public static ItemCourageShard courageshard = new ItemCourageShard();
	public static ItemFlameDin flamedin = new ItemFlameDin();
	public static ItemFlameFarore flamefarore = new ItemFlameFarore();
	public static ItemFlameNayru flamenayru = new ItemFlameNayru();
	public static ItemFlameTwilight flametwilight = new ItemFlameTwilight();
	public static ItemFusedShadowShard fusedshadow = new ItemFusedShadowShard();
	public static ItemMagicalTablet magictablet = new ItemMagicalTablet();
	public static ItemMiniTriforce minitriforce = new ItemMiniTriforce();
	public static ItemPowerShard powershard = new ItemPowerShard();
	public static ItemWisdomShard wisdomshard = new ItemWisdomShard();
	//material
	public static ItemPorcelainClay porcelainclay = new ItemPorcelainClay();
	//medallions
	public static ItemFireMedallion firemedal = new ItemFireMedallion();
	public static ItemLightMedallion lightmedal = new ItemLightMedallion();
	public static ItemMeadowMedallion meadowmedal = new ItemMeadowMedallion();
	public static ItemShadowMedallion shadowmedal = new ItemShadowMedallion();
	public static ItemSpiritMedallion spiritmedal = new ItemSpiritMedallion();
	public static ItemWaterMedallion watermedal = new ItemWaterMedallion();
	//music
	public static ItemANote anote = new ItemANote();
	public static ItemBlankMusicSheet blankmusicsheet = new ItemBlankMusicSheet();
	public static ItemDownNote downnote = new ItemDownNote();
	public static ItemHealingSongSheet healingsong = new ItemHealingSongSheet();
	public static ItemLeftNote leftnote = new ItemLeftNote();
	public static ItemOcarinaBase ocarinabase = new ItemOcarinaBase();
	public static ItemOcarinaFairy ocarinafairy = new ItemOcarinaFairy();
	public static ItemOcarinaLunar ocarinalunar = new ItemOcarinaLunar();
	public static ItemOcarinaThunder ocarinathunder = new ItemOcarinaThunder();
	public static ItemOcarinaTime ocarinatime = new ItemOcarinaTime();
	public static ItemOcarinaWind ocarinawind = new ItemOcarinaWind();
	public static ItemRightNote rightnote = new ItemRightNote();
	public static ItemSoaringSongSheet soaringsong = new ItemSoaringSongSheet();
	public static ItemStormSongSheet stormsong = new ItemStormSongSheet();
	public static ItemSunSongSheet sunsong = new ItemSunSongSheet();
	public static ItemUnfiredOcarina unfiredocarina = new ItemUnfiredOcarina();
	public static ItemUpNote upnote = new ItemUpNote();
	public static ItemWWBaton baton = new ItemWWBaton();
	public static ItemZeldaSongSheet zeldasong = new ItemZeldaSongSheet();
	//weapons
	public static ItemBulblinClub bulblinclub = new ItemBulblinClub();
	public static ItemGoddessSword goddesssword = new ItemGoddessSword();
	public static ItemMasterSword mastersword = new ItemMasterSword();
	public static ItemPrincessRapier princessrapier = new ItemPrincessRapier();
	public static ItemSageSword sagesword = new ItemSageSword();
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		//boundchest.load(event);
		deityarmor.load(event);
		goronarmor.load(event);
		heroarmor.load(event);
		zoraarmor.load(event);
		ancienttablet.load(event);
		courageshard.load(event);
		flamedin.load(event);
		flamefarore.load(event);
		flamenayru.load(event);
		flametwilight.load(event);
		fusedshadow.load(event);
		magictablet.load(event);
		minitriforce.load(event);
		powershard.load(event);
		wisdomshard.load(event);
		porcelainclay.load(event);
		firemedal.load(event);
		lightmedal.load(event);
		meadowmedal.load(event);
		shadowmedal.load(event);
		spiritmedal.load(event);
		watermedal.load(event);
		anote.load(event);
		blankmusicsheet.load(event);
		downnote.load(event);
		healingsong.load(event);
		leftnote.load(event);
		ocarinabase.load(event);
		ocarinafairy.load(event);
		ocarinalunar.load(event);
		ocarinathunder.load(event);
		ocarinatime.load(event);
		ocarinawind.load(event);
		rightnote.load(event);
		soaringsong.load(event);
		stormsong.load(event);
		sunsong.load(event);
		unfiredocarina.load(event);
		upnote.load(event);
		baton.load(event);
		zeldasong.load(event);
		bulblinclub.load(event);
		goddesssword.load(event);
		mastersword.load(event);
		bulblinclub.load(event);
		princessrapier.load(event);
		sagesword.load(event);
		
		proxy.registerRenderers(this);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			OBJLoader.INSTANCE.addDomain("lozmod");
		}
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTwilightPrincess.class, RenderTwilightPrincess::new);
		
		ModEntities.init();
		
		ResourceLocation sound0 = new ResourceLocation("lozmod", "lozmod.music.sound.healing");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound0).setRegistryName(sound0));
		ResourceLocation sound1 = new ResourceLocation("lozmod", "lozmod.music.sound.soaring");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound1).setRegistryName(sound1));
		ResourceLocation sound2 = new ResourceLocation("lozmod", "lozmod.music.sound.storm");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound2).setRegistryName(sound2));
		ResourceLocation sound3 = new ResourceLocation("lozmod", "lozmod.music.sound.sun");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound3).setRegistryName(sound3));
		ResourceLocation sound4 = new ResourceLocation("lozmod", "lozmod.music.sound.lullaby");
		ForgeRegistries.SOUND_EVENTS.register(new net.minecraft.util.SoundEvent(sound4).setRegistryName(sound4));
	}
	
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
