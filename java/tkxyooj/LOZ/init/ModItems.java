public class ModItems
{
	declare variables
    //armor
    public static ItemDeityArmor deityarmor;
    public static ItemGoronArmor goronarmor;
    public static ItemHeroArmor heroarmor;
    public static ItemZoraArmor zoraarmor;
	//magic
	public static ItemAncientTablet ancienttablet;
	public static ItemCourageShard courageshard;
	public static ItemFlameDin flamedin;
	public static ItemFlameFarore flamefarore;
	public static ItemFlameNayru flamenayru;
	public static ItemFlameTwilight flametwilight;
	public static ItemFusedShadowShard fusedshadow;
	public static ItemMagicalTablet magictablet;
	public static ItemMiniTriforce minitriforce;
	public static ItemPowerShard powershard;
	public static ItemWisdomShard wisdomshard;
	//material
	public static ItemPorcelainClay porcelainclay;
	//medallions
	public static ItemFireMedallion firemedal;
	public static ItemLightMedallion lightmedal;
	public static ItemMeadowMedallion meadowmedal;
	public static ItemShadowMedallion shadowmedal;
	public static ItemSpiritMedallion spiritmedal;
	public static ItemWaterMedallion watermedal;
	//music
	public static ItemANote anote;
	public static ItemBlankMusicSheet blankmusicsheet;
	public static ItemDownNote downnote;
	public static ItemHealingSongSheet healingsong;
	public static ItemLeftNote leftnote;
	public static ItemOcarinaBase ocarinabase;
	public static ItemOcarinaFairy ocarinafairy;
	public static ItemOcarinaLunar ocarinalunar;
	public static ItemOcarinaThunder ocarinathunder;
	public static ItemOcarinaTime ocarinatime;
	public static ItemOcarinaWind ocarinawind;
	public static ItemRightNote rightnote;
	public static ItemSoaringSongSheet soaringsong;
	public static ItemStormSongSheet stormsong;
	public static ItemSunSongSheet sunsong;
	public static ItemUnfiredOcarina;
	public static ItemUpNote upnote;
	public static ItemWWBaton baton;
	public static ItemZeldaSongSheet zeldasong;
	//weapons
	public static ItemBulblinClub bulblinclub;
	public static ItemGoddessSword goddesssword;
	public static ItemMasterSword mastersword;
	public static ItemPrincessRapier princessrapier;
    public static ItemSageSword sagesword;
    
    public static void init()
    {
        //item-armor
        deityarmor = registerItem(new ItemDeityArmor();
        goronarmor = registerItem(new ItemGoronArmor();
        heroarmor = registerItem(new ItemHeroArmor();
        zoraarmor = registerItem(new ItemZoraArmor();
	    //magic
	    ancienttablet = registerItem(new ItemAncientTablet();
	    courageshard = registerItem(new ItemCourageShard();
	    flamedin = registerItem(new ItemFlameDin();
	    flamefarore = registerItem(new ItemFlameFarore();
	    flamenayru = registerItem(new ItemFlameNayru();
	    flametwilight = registerItem(new ItemFlameTwilight();
	    fusedshadow = registerItem(new ItemFusedShadowShard();
	    magictablet = registerItem(new ItemMagicalTablet();
	    minitriforce = registerItem(new ItemMiniTriforce();
	    powershard = registerItem(new ItemPowerShard();
	    wisdomshard = registerItem(new ItemWisdomShard();
	    //material
	    porcelainclay = registerItem(new ItemPorcelainClay();
	    //medallions
	    firemedal = registerItem(new ItemFireMedallion();
	    lightmedal = registerItem(new ItemLightMedallion();
	    meadowmedal = registerItem(new ItemMeadowMedallion();
	    shadowmedal = registerItem(new ItemShadowMedallion();
	    spiritmedal = registerItem(new ItemSpiritMedallion();
	    watermedal = registerItem(new ItemWaterMedallion();
	    //music
	    anote = registerItem(new ItemANote();
	    blankmusicsheet = registerItem(new ItemBlankMusicSheet();
	    downnote = registerItem(new ItemDownNote();
	    healingsong = registerItem(new ItemHealingSongSheet();
	    leftnote = registerItem(new ItemLeftNote();
	    ocarinabase = registerItem(new ItemOcarinaBase();
	    ocarinafairy = registerItem(new ItemOcarinaFairy();
	    ocarinalunar = registerItem(new ItemOcarinaLunar();
	    ocarinathunder = registerItem(new ItemOcarinaThunder();
	    ocarinatime = registerItem(new ItemOcarinaTime();
	    ocarinawind = registerItem(new ItemOcarinaWind();
	    rightnote = registerItem(new ItemRightNote();
	    soaringsong = registerItem(new ItemSoaringSongSheet();
	    stormsong = registerItem(new ItemStormSongSheet();
	    sunsong = registerItem(new ItemSunSongSheet();
	    unfiredocarina = registerItem(new ItemUnfiredOcarina();
	    upnote = registerItem(new ItemUpNote();
	    baton = registerItem(new ItemWWBaton();
	    zeldasong = registerItem(new ItemZeldaSongSheet();
	    //weapons
	    bulblinclub = registerItem(new ItemBulblinClub();
	    goddesssword = registerItem(new ItemGoddessSword();
	    mastersword = registerItem(new ItemMasterSword();
	    princessrapier = registerItem(new ItemPrincessRapier();
        sagesword = registerItem(new ItemSageSword();
    }
    public static <V extends Item> V registerItem(V item) 
    {
        registerImpl(item, ForgeRegistries.ITEMS::register);
        return item;
    }

    public static <V extends IForgeRegistryEntry<V>> V registerImpl(V registryObject, Consumer<V> registerCallback) 
    {
        registerCallback.accept(registryObject);

        if (registryObject instanceof IModelRegister) 
        {
            LOZ.proxy.addModelRegister((IModelRegister) registryObject);
        }

        return registryObject;
    }
}
