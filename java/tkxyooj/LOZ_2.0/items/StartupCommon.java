package tkxyooj.LOZ.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tkxyooj.LOZ.items.armor.*;
import tkxyooj.LOZ.items.magic.*;
import tkxyooj.LOZ.items.material.*;
import tkxyooj.LOZ.items.medallions.*;
import tkxyooj.LOZ.items.music.*;
import tkxyooj.LOZ.items.tools.*;
import tkxyooj.LOZ.items.weapons.*;

public class StartupCommon
{
	// this holds the unique instance of your block
	//public static ItemSimple itemSimple;
		
	//armor
	public static ItemDeityArmor deityarmorhelmet;
	public static ItemDeityArmor deityarmorbody;
	public static ItemDeityArmor deityarmorlegs;
	public static ItemDeityArmor deityarmorboots;
	public static ItemGoronArmor goronarmorhelmet;
	public static ItemGoronArmor goronarmorbody;
	public static ItemGoronArmor goronarmorlegs;
	public static ItemGoronArmor goronarmorboots;
	public static ItemHeroArmor heroarmorhelmet;
	public static ItemHeroArmor heroarmorbody;
	public static ItemHeroArmor heroarmorlegs;
	public static ItemHeroArmor heroarmorboots;
	public static ItemZoraArmor zoraarmorhelmet;
	public static ItemZoraArmor zoraarmorbody;
	public static ItemZoraArmor zoraarmorlegs;
	public static ItemZoraArmor zoraarmorboots;
	public static ItemPegasusBoots pegasusboots;
	public static ItemZorasFlippers zorasflippers;
	//magic
	public static ItemAncientTablet ancienttablet;
	public static ItemBombFairy bombfairy;
	public static ItemCourageShard courageshard;
	public static ItemFinalBossKey bosskey;
	public static ItemFlameDin flamedin;
	public static ItemFlameFarore flamefarore;
	public static ItemFlameNayru flamenayru;
	public static ItemFlameTwilight flametwilight;
	public static ItemFusedShadowShard fusedshadow;
	public static ItemMagicalTablet magictablet;
	public static ItemMiniTriforce minitriforce;
	public static ItemPowerShard powershard;
	public static ItemSpike spike;
	public static ItemStaticFairy staticfairy;
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
	public static ItemUnfiredOcarina unfiredocarina;
	public static ItemUpNote upnote;
	public static ItemWWBaton baton;
	public static ItemZeldaSongSheet zeldasong;
	//tools
	public static ItemRocsFeather rocsfeather;
	//weapons
	public static ItemBulblinClub bulblinclub;
	public static ItemGoddessSword goddesssword;
	public static ItemMasterSword mastersword;
	public static ItemPrincessRapier princessrapier;
	public static ItemSageSword sagesword;


  public static void preInitCommon()
  {
    // each instance of your item should have two names:
    // 1) a registry name that is used to uniquely identify this item.  Should be unique within your mod.  use lower case.
    // 2) an 'unlocalised name' that is used to retrieve the text name of your item in the player's language.  For example-
    //    the unlocalised name might be "water", which is printed on the user's screen as "Wasser" in German or
    //    "aqua" in Italian.
    //
    //    Multiple items can have the same unlocalised name - for example
    //  +----RegistryName-------+----UnlocalisedName----+
    //  |  burning_candle       +       candle          |
    //  |  extinguished_candle  +       candle          |
    //  +-----------------------+-----------------------+
    //
    //itemSimple = (ItemSimple)(new ItemSimple().setUnlocalizedName("mbe10_item_simple_unlocalised_name"));
    //itemSimple.setRegistryName("mbe10_item_simple_registry_name");
    //ForgeRegistries.ITEMS.register(itemSimple);
	  
	  	//deity armor
  		deityarmorhelmet = new ItemDeityArmor(EntityEquipmentSlot.HEAD);
  		deityarmorhelmet.setUnlocalizedName("deityarmorhelmet");
  		deityarmorhelmet.setRegistryName("deityarmorhelmet");
  		ForgeRegistries.ITEMS.register(deityarmorhelmet);
      
  		deityarmorbody = new ItemDeityArmor(EntityEquipmentSlot.CHEST);
  		deityarmorbody.setUnlocalizedName("deityarmorbody");
  		deityarmorbody.setRegistryName("deityarmorbody");
  		ForgeRegistries.ITEMS.register(deityarmorbody);

  		deityarmorlegs = new ItemDeityArmor(EntityEquipmentSlot.LEGS);
  		deityarmorlegs.setUnlocalizedName("deityarmorlegs");
  		deityarmorlegs.setRegistryName("deityarmorlegs");
  		ForgeRegistries.ITEMS.register(deityarmorlegs);

  		deityarmorboots = new ItemDeityArmor(EntityEquipmentSlot.FEET);
  		deityarmorboots.setUnlocalizedName("deityarmorboots");
		deityarmorboots.setRegistryName("deityarmorboots");
		ForgeRegistries.ITEMS.register(deityarmorboots);
      
		//goron armor
		goronarmorhelmet = new ItemGoronArmor(EntityEquipmentSlot.HEAD);
		goronarmorhelmet.setUnlocalizedName("goronarmorhelmet");
		goronarmorhelmet.setRegistryName("goronarmorhelmet");
		ForgeRegistries.ITEMS.register(goronarmorhelmet);
      
		goronarmorbody = new ItemGoronArmor(EntityEquipmentSlot.CHEST);
		goronarmorbody.setUnlocalizedName("goronarmorbody");
		goronarmorbody.setRegistryName("goronarmorbody");
		ForgeRegistries.ITEMS.register(goronarmorbody);

		goronarmorlegs = new ItemGoronArmor(EntityEquipmentSlot.LEGS);
		goronarmorlegs.setUnlocalizedName("goronarmorlegs");
		goronarmorlegs.setRegistryName("goronarmorlegs");
      	ForgeRegistries.ITEMS.register(goronarmorlegs);

      	goronarmorboots = new ItemGoronArmor(EntityEquipmentSlot.FEET);
      	goronarmorboots.setUnlocalizedName("goronarmorboots");
		goronarmorboots.setRegistryName("goronarmorboots");
		ForgeRegistries.ITEMS.register(goronarmorboots);
		
		//hero armor
		heroarmorhelmet = new ItemHeroArmor(EntityEquipmentSlot.HEAD);
		heroarmorhelmet.setUnlocalizedName("heroarmorhelmet");
		heroarmorhelmet.setRegistryName("heroarmorhelmet");
		ForgeRegistries.ITEMS.register(heroarmorhelmet);
  
		heroarmorbody = new ItemHeroArmor(EntityEquipmentSlot.CHEST);
		heroarmorbody.setUnlocalizedName("heroarmorbody");
		heroarmorbody.setRegistryName("heroarmorbody");
		ForgeRegistries.ITEMS.register(heroarmorbody);

		heroarmorlegs = new ItemHeroArmor(EntityEquipmentSlot.LEGS);
		heroarmorlegs.setUnlocalizedName("heroarmorlegs");
		heroarmorlegs.setRegistryName("heroarmorlegs");
		ForgeRegistries.ITEMS.register(heroarmorlegs);

		heroarmorboots = new ItemHeroArmor(EntityEquipmentSlot.FEET);
		heroarmorboots.setUnlocalizedName("heroarmorboots");
		heroarmorboots.setRegistryName("heroarmorboots");
		ForgeRegistries.ITEMS.register(heroarmorboots);
		
		//zora armor
		zoraarmorhelmet = new ItemZoraArmor(EntityEquipmentSlot.HEAD);
		zoraarmorhelmet.setUnlocalizedName("zoraarmorhelmet");
		zoraarmorhelmet.setRegistryName("zoraarmorhelmet");
		ForgeRegistries.ITEMS.register(zoraarmorhelmet);
                      
		zoraarmorbody = new ItemZoraArmor(EntityEquipmentSlot.CHEST);
		zoraarmorbody.setUnlocalizedName("zoraarmorbody");
		zoraarmorbody.setRegistryName("zoraarmorbody");
		ForgeRegistries.ITEMS.register(zoraarmorbody);

		zoraarmorlegs = new ItemZoraArmor(EntityEquipmentSlot.LEGS);
		zoraarmorlegs.setUnlocalizedName("zoraarmorlegs");
		zoraarmorlegs.setRegistryName("zoraarmorlegs");
		ForgeRegistries.ITEMS.register(zoraarmorlegs);
      
		zoraarmorboots = new ItemZoraArmor(EntityEquipmentSlot.FEET);
		zoraarmorboots.setUnlocalizedName("zoraarmorboots");
		zoraarmorboots.setRegistryName("zoraarmorboots");
		ForgeRegistries.ITEMS.register(zoraarmorboots);
		
		pegasusboots = new ItemPegasusBoots(EntityEquipmentSlot.FEET);
		pegasusboots.setUnlocalizedName("pegasusboots");
		pegasusboots.setRegistryName("pegasusboots");
		ForgeRegistries.ITEMS.register(pegasusboots);
		
		zorasflippers = new ItemZorasFlippers(EntityEquipmentSlot.FEET);
		zorasflippers.setUnlocalizedName("zorasflippers");
		zorasflippers.setRegistryName("zorasflippers");
		ForgeRegistries.ITEMS.register(zorasflippers);
		
		//magic
	    ancienttablet = new ItemAncientTablet();
	    ancienttablet.setUnlocalizedName("ancienttablet");
	    ancienttablet.setRegistryName("ancienttablet");
	    ForgeRegistries.ITEMS.register(ancienttablet);
	    
	    bombfairy = new ItemBombFairy();
	    bombfairy.setUnlocalizedName("bombfairy");
	    bombfairy.setRegistryName("bombfairy");
	    ForgeRegistries.ITEMS.register(bombfairy);
	    
	    courageshard = new ItemCourageShard();
	    courageshard.setUnlocalizedName("courageshard");
	    courageshard.setRegistryName("courageshard");
	    ForgeRegistries.ITEMS.register(courageshard);
	    
	    bosskey = new ItemFinalBossKey();
	    bosskey.setUnlocalizedName("bosskey");
	    bosskey.setRegistryName("bosskey");
	    ForgeRegistries.ITEMS.register(bosskey);
	    
	    flamedin = new ItemFlameDin();
	    flamedin.setUnlocalizedName("flamedin");
	    flamedin.setRegistryName("flamedin");
	    ForgeRegistries.ITEMS.register(flamedin);
	    
	    flamefarore = new ItemFlameFarore();
	    flamefarore.setUnlocalizedName("flamefarore");
	    flamefarore.setRegistryName("flamefarore");
	    ForgeRegistries.ITEMS.register(flamefarore);
	    
	    flamenayru = new ItemFlameNayru();
	    flamenayru.setUnlocalizedName("flamenayru");
	    flamenayru.setRegistryName("flamenayru");
	    ForgeRegistries.ITEMS.register(flamenayru);
	    
	    flametwilight = new ItemFlameTwilight();
	    flametwilight.setUnlocalizedName("flametwilight");
	    flametwilight.setRegistryName("flametwilight");
	    ForgeRegistries.ITEMS.register(flametwilight);
	    
	    fusedshadow = new ItemFusedShadowShard();
	    fusedshadow.setUnlocalizedName("fusedshadow");
	    fusedshadow.setRegistryName("fusedshadow");
	    ForgeRegistries.ITEMS.register(fusedshadow);
	    
	    magictablet = new ItemMagicalTablet();
	    magictablet.setUnlocalizedName("magictablet");
	    magictablet.setRegistryName("magictablet");
	    ForgeRegistries.ITEMS.register(magictablet);
	    
	    minitriforce = new ItemMiniTriforce();
	    minitriforce.setUnlocalizedName("minitriforce");
	    minitriforce.setRegistryName("minitriforce");
	    ForgeRegistries.ITEMS.register(minitriforce);
	    
	    powershard = new ItemPowerShard();
	    powershard.setUnlocalizedName("powershard");
	    powershard.setRegistryName("powershard");
	    ForgeRegistries.ITEMS.register(powershard);
	    
	    spike = new ItemSpike();
	    spike.setUnlocalizedName("spike");
	    spike.setRegistryName("spike");
	    ForgeRegistries.ITEMS.register(spike);
	    
	    staticfairy = new ItemStaticFairy();
	    staticfairy.setUnlocalizedName("staticfairy");
	    staticfairy.setRegistryName("staticfairy");
	    ForgeRegistries.ITEMS.register(staticfairy);
	    
	    wisdomshard = new ItemWisdomShard();
	    wisdomshard.setUnlocalizedName("wisdomshard");
	    wisdomshard.setRegistryName("wisdomshard");
	    ForgeRegistries.ITEMS.register(wisdomshard);
	    
	    //material
	    porcelainclay = new ItemPorcelainClay();
	    porcelainclay.setUnlocalizedName("porcelainclay");
	    porcelainclay.setRegistryName("porcelainclay");
	    ForgeRegistries.ITEMS.register(porcelainclay);
	    
	    //medallions
	    firemedal = new ItemFireMedallion();
	    firemedal.setUnlocalizedName("firemedal");
	    firemedal.setRegistryName("firemedal");
	    ForgeRegistries.ITEMS.register(firemedal);
	    
	    lightmedal = new ItemLightMedallion();
	    lightmedal.setUnlocalizedName("lightmedal");
	    lightmedal.setRegistryName("lightmedal");
	    ForgeRegistries.ITEMS.register(lightmedal);
	    
	    meadowmedal = new ItemMeadowMedallion();
	    meadowmedal.setUnlocalizedName("meadowmedal");
	    meadowmedal.setRegistryName("meadowmedal");
	    ForgeRegistries.ITEMS.register(meadowmedal);
	    
	    shadowmedal = new ItemShadowMedallion();
	    shadowmedal.setUnlocalizedName("shadowmedal");
	    shadowmedal.setRegistryName("shadowmedal");
	    ForgeRegistries.ITEMS.register(shadowmedal);
	    
	    spiritmedal = new ItemSpiritMedallion();
	    spiritmedal.setUnlocalizedName("spiritmedal");
	    spiritmedal.setRegistryName("spiritmedal");
	    ForgeRegistries.ITEMS.register(spiritmedal);
	    
	    watermedal = new ItemWaterMedallion();
	    watermedal.setUnlocalizedName("watermedal");
	    watermedal.setRegistryName("watermedal");
	    ForgeRegistries.ITEMS.register(watermedal);
	    
	    //music
	    anote = new ItemANote();
	    anote.setUnlocalizedName("anote");
	    anote.setRegistryName("anote");
	    ForgeRegistries.ITEMS.register(anote);
	    
	    blankmusicsheet = new ItemBlankMusicSheet();
	    blankmusicsheet.setUnlocalizedName("blankmusicsheet");
	    blankmusicsheet.setRegistryName("blankmusicsheet");
	    ForgeRegistries.ITEMS.register(blankmusicsheet);
	    
	    downnote = new ItemDownNote();
	    downnote.setUnlocalizedName("downnote");
	    downnote.setRegistryName("downnote");
	    ForgeRegistries.ITEMS.register(downnote);
	    
	    healingsong = new ItemHealingSongSheet();
	    healingsong.setUnlocalizedName("healingsong");
	    healingsong.setRegistryName("healingsong");
	    ForgeRegistries.ITEMS.register(healingsong);
	    
	    leftnote = new ItemLeftNote();
	    leftnote.setUnlocalizedName("leftnote");
	    leftnote.setRegistryName("leftnote");
	    ForgeRegistries.ITEMS.register(leftnote);
	    
	    ocarinabase = new ItemOcarinaBase();
	    ocarinabase.setUnlocalizedName("ocarinabase");
	    ocarinabase.setRegistryName("ocarinabase");
	    ForgeRegistries.ITEMS.register(ocarinabase);
	    
	    ocarinafairy = new ItemOcarinaFairy();
	    ocarinafairy.setUnlocalizedName("ocarinafairy");
	    ocarinafairy.setRegistryName("ocarinafairy");
	    ForgeRegistries.ITEMS.register(ocarinafairy);
	    
	    ocarinalunar = new ItemOcarinaLunar();
	    ocarinalunar.setUnlocalizedName("ocarinalunar");
	    ocarinalunar.setRegistryName("ocarinalunar");
	    ForgeRegistries.ITEMS.register(ocarinalunar);
	    
	    ocarinathunder = new ItemOcarinaThunder();
	    ocarinathunder.setUnlocalizedName("ocarinathunder");
	    ocarinathunder.setRegistryName("ocarinathunder");
	    ForgeRegistries.ITEMS.register(ocarinathunder);
	    
	    ocarinatime = new ItemOcarinaTime();
	    ocarinatime.setUnlocalizedName("ocarinatime");
	    ocarinatime.setRegistryName("ocarinatime");
	    ForgeRegistries.ITEMS.register(ocarinatime);
	    
	    ocarinawind = new ItemOcarinaWind();
	    ocarinawind.setUnlocalizedName("ocarinawind");
	    ocarinawind.setRegistryName("ocarinawind");
	    ForgeRegistries.ITEMS.register(ocarinawind);
	    
	    rightnote = new ItemRightNote();
	    rightnote.setUnlocalizedName("rightnote");
	    rightnote.setRegistryName("rightnote");
	    ForgeRegistries.ITEMS.register(rightnote);
	    
	    soaringsong = new ItemSoaringSongSheet();
	    soaringsong.setUnlocalizedName("soaringsong");
	    soaringsong.setRegistryName("soaringsong");
	    ForgeRegistries.ITEMS.register(soaringsong);
	    
	    stormsong = new ItemStormSongSheet();
	    stormsong.setUnlocalizedName("stormsong");
	    stormsong.setRegistryName("stormsong");
	    ForgeRegistries.ITEMS.register(stormsong);
	    
	    sunsong = new ItemSunSongSheet();
	    sunsong.setUnlocalizedName("sunsong");
	    sunsong.setRegistryName("sunsong");
	    ForgeRegistries.ITEMS.register(sunsong);
	    
	    unfiredocarina = new ItemUnfiredOcarina();
	    unfiredocarina.setUnlocalizedName("unfiredocarina");
	    unfiredocarina.setRegistryName("unfiredocarina");
	    ForgeRegistries.ITEMS.register(unfiredocarina);
	    
	    upnote = new ItemUpNote();
	    upnote.setUnlocalizedName("upnote");
	    upnote.setRegistryName("upnote");
	    ForgeRegistries.ITEMS.register(upnote);
	    
	    baton = new ItemWWBaton();
	    baton.setUnlocalizedName("baton");
	    baton.setRegistryName("baton");
	    ForgeRegistries.ITEMS.register(baton);
	    
	    zeldasong = new ItemZeldaSongSheet();
	    zeldasong.setUnlocalizedName("zeldasong");
	    zeldasong.setRegistryName("zeldasong");
	    ForgeRegistries.ITEMS.register(zeldasong);
	    //tools
	    rocsfeather = new ItemRocsFeather();
	    rocsfeather.setUnlocalizedName("rocsfeather");
	    rocsfeather.setRegistryName("rocsfeather");
	    ForgeRegistries.ITEMS.register(rocsfeather);
	    
	    //weapons
	    bulblinclub = new ItemBulblinClub();
	    bulblinclub.setUnlocalizedName("bulblinclub");
	    bulblinclub.setRegistryName("bulblinclub");
	    ForgeRegistries.ITEMS.register(bulblinclub);
	    
	    goddesssword = new ItemGoddessSword();
	    goddesssword.setUnlocalizedName("goddesssword");
	    goddesssword.setRegistryName("goddesssword");
	    ForgeRegistries.ITEMS.register(goddesssword);
	    
	    mastersword = new ItemMasterSword();
	    mastersword.setUnlocalizedName("mastersword");
	    mastersword.setRegistryName("mastersword");
	    ForgeRegistries.ITEMS.register(mastersword);
	    
	    princessrapier = new ItemPrincessRapier();
	    princessrapier.setUnlocalizedName("princessrapier");
	    princessrapier.setRegistryName("princessrapier");
	    ForgeRegistries.ITEMS.register(princessrapier);
	    
        sagesword = new ItemSageSword();
        sagesword.setUnlocalizedName("sagesword");
        sagesword.setRegistryName("sagesword");
	    ForgeRegistries.ITEMS.register(sagesword);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
