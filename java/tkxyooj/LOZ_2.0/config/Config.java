package tkxyooj.LOZ.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tkxyooj.LOZ.LOZ;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the configuration information and synchronises the various copies of it.
 * The configuration information is stored in three places:
 * 1) in the configuration file on disk, as text.
 * 2) in the Configuration object config (accessed by the mod GUI), as text.
 * 3) in the MBEConfiguration variables (fields), as native values (integer, double, etc).
 *
 * Setup:
 * (1) During your mod preInit(), call MBEConfiguration.preInit() to:
 *     a) set up the format of the configuration file.
 *     b) load the settings from the existing file, or if it doesn't exist yet - create it with default values.
 * (2) On the client proxy (not dedicated server), call clientPreInit() to register an OnConfigChangedEvent handler-
 *     your GUI will modify the config object, and when it is closed it will trigger a OnConfigChangedEvent,
 *     which should call syncFromGUI().
 *
 * Usage:
 * (3) You can read the fields such as myInteger directly.
 * (4) If you modify the configuration fields, you can save them to disk using syncFromFields().
 * (5) To reload the values from disk, call syncFromFile().
 * (6) If you have used a GUI to alter the config values, call syncFromGUI()
 *     (If you called clientPreInit(), this will happen automatically).
 *
 *  => See ForgeModContainer for more examples.
 */
public class Config {

	// Declare all configuration fields used by the mod here
	  
	/** Allow modification of health **/
	private static boolean healthModification;
	  
	/** Always pick up small hearts regardless of health */
	private static boolean alwaysHeartsPickup;
	  
	/** Classic Zelda Start: Start with 3 hearts (can be disabled) **/
	private static boolean enableClassicMode;
	  
	/** Max number of hearts (NONE, MINECRAFT, CLASSIC) **/
	private static String maxHeart;
	
	/** Weight of heart pieces found in vanilla chests **/
	private static int heartpieceweight;
	
	/** Chance of grass dropping hearts/rupees (set to zero to disable) **/
	private static int grassDropChance;

	/** Chance of Creepers dropping bombs (set to zero to disable) **/
	private static int creeperLoot;
	  
	/** Frequency of small heart and rupee drops from mobs [zero to disable; 1 = rare, 10 = very common] */
	private static int mobLootFrequency;
	
	public static final String CATEGORY_NAME_GENERAL = "category_general";
	public static final String CATEGORY_NAME_LOOT = "category_loot";

	public static void preInit()
	{
		/*
		 * Here is where you specify the location from where your config file
		 * will be read, or created if it is not present.
		 *
		 * Loader.instance().getConfigDir() returns the default config directory
		 * and you specify the name of the config file, together this works
		 * similar to the old getSuggestedConfigurationFile() function.
		 */
		File configFile = new File(Loader.instance().getConfigDir(), "LOZmod.cfg");

		// initialize your configuration object with your configuration file values.
		config = new Configuration(configFile);

		// load config from file (see mbe70 package for more info)
		syncFromFile();
	}

	public static void clientPreInit() {
		/*
		 * Register the save config handler to the Forge event bus, creates an
		 * instance of the static class ConfigEventHandler and has it listen on
		 * the core Forge event bus (see Notes and ConfigEventHandler for more information).
		 */
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static Configuration getConfig() {
		return config;
	}

	/**
	 * load the configuration values from the configuration file
	 */
	public static void syncFromFile() {
		syncConfig(true, true);
	}

	/**
	 * save the GUI-altered values to disk
	 */
	public static void syncFromGUI() {
		syncConfig(false, true);
	}

	/**
	 * save the variables (fields) to disk
	 */
	public static void syncFromFields() {
		syncConfig(false, false);
	}

	/**
	 * Synchronise the three copies of the data
	 * 1) loadConfigFromFile && readFieldsFromConfig -> initialise everything from the disk file.
	 * 2) !loadConfigFromFile && readFieldsFromConfig --> copy everything from the config file (altered by GUI).
	 * 3) !loadConfigFromFile && !readFieldsFromConfig --> copy everything from the native fields.
	 *
	 * @param loadConfigFromFile if true, load the config field from the configuration file on disk.
	 * @param readFieldsFromConfig if true, reload the member variables from the config field.
	 */

	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig)
	{
		/*
		 * ---- step 1 - load raw values from config file (if loadFromFile true) -------------------
		 *
		 * Check if this configuration object is the main config file or a child
		 * configuration For simple configuration setups, this only matters if
		 * you enable global configuration for your configuration object by
		 * using config.enableGlobalConfiguration(), this will cause your config
		 * file to be 'global.cfg' in the default configuration directory and
		 * use it to read/write your configuration options
		 */
		if (loadConfigFromFile) 
		{
			config.load();
		}

		/*
		 * Using language keys are a good idea if you are using a config GUI
		 * This allows you to provide "pretty" names for the config properties
		 * in a .lang file as well as allow others to provide other
		 * localizations for your mod.
		 *
		 * The language key is also used to get the tooltip for your property,
		 * the language key for each properties tooltip is langKey + ".tooltip"
		 * If no tooltip lang key is specified in a .lang file, it will default
		 * to the property's comment field.
		 *
		 * prop.setRequiresWorldRestart(true); and
		 * prop.setRequiresMcRestart(true); can be used to tell Forge if that
		 * specific property requires a world or complete Minecraft restart,
		 * respectively.
		 *
		 * Note: if a property requires a world restart it cannot be edited in
		 * the in-world mod settings (which hasn't been implemented yet by
		 * Forge), only when a world isn't loaded.
		 *
		 * -See the function definitions for more info
		 */

		/*
		 * ---- step 2 - define the properties in the configuration file -------------------
		 *
		 * The following code is used to define the properties in the
		 * configuration file: their name, type, default / min / max values, a
		 * comment. These affect what is displayed on the GUI. If the file
		 * already exists, the property values will already have been read from
		 * the file, otherwise they will be assigned the default value.
		 */

		// health modification
		final boolean HEALTHMODIFICATION_DEFAULT_VALUE = true;
		Property propHealthModification = config.get(CATEGORY_NAME_GENERAL, 
				"Allow LOZ mod to modify health. Set to false if you have another mod that modifies player health.", 
				HEALTHMODIFICATION_DEFAULT_VALUE);
		propHealthModification.setRequiresMcRestart(true);

		//heart pick ups
		final boolean ALWAYSHEARTPICKUP_DEFAULT_VALUE = true;
		Property propAlwaysHeartPickup = config.get(CATEGORY_NAME_GENERAL, 
				"Always pick up small heart drops regardless of health.", 
				ALWAYSHEARTPICKUP_DEFAULT_VALUE);
		
		//classic Zelda mode
		final boolean CLASSICMODE_DEFAULT_VALUE = true;
		Property propClassicMode = config.get(CATEGORY_NAME_GENERAL, 
				"Classic Zelda Mode: Start with only 3 hearts and it can be enabled/disabled at any time.", 
				CLASSICMODE_DEFAULT_VALUE);
		propClassicMode.setRequiresMcRestart(true);
		
		//max heart that can be obtained
		final String MAXHEART_DEFAULT_VALUE = "CLASSIC";
		final String[] MAXHEART_CHOICES = { "NONE", "CLASSIC", "MINECRAFT" };
		Property propMaxHeart = config.get(CATEGORY_NAME_GENERAL, 
				"Max Health Obtainable. None is for unlimited health.", 
				MAXHEART_DEFAULT_VALUE);
		propMaxHeart.setRequiresWorldRestart(true);
		propMaxHeart.setValidValues(MAXHEART_CHOICES);
		
		//heart piece weight
		final int HEARTPIECE_INT_MIN_VALUE = 1;
		final int HEARTPIECE_INT_MAX_VALUE = 10;
		final int HEARTPIECE_INT_DEFAULT_VALUE = 2;
		Property propHeartPieceWeight = config.get(CATEGORY_NAME_LOOT, 
				"Weight of heart pieces found in vanilla chests.", 
				HEARTPIECE_INT_DEFAULT_VALUE,
				"[1-10]", HEARTPIECE_INT_MIN_VALUE, HEARTPIECE_INT_MAX_VALUE);
		propHeartPieceWeight.setRequiresMcRestart(true);
		
		//grass drop chance
		final int GRASSDROP_INT_MIN_VALUE = 0;
		final int GRASSDROP_INT_MAX_VALUE = 100;
		final int GRASSDROP_INT_DEFAULT_VALUE = 15;
		Property propGrassDropChance = config.get(CATEGORY_NAME_LOOT, 
				"Chance (out of 100) of loot dropping from grass.", 
				GRASSDROP_INT_DEFAULT_VALUE,
				"[0-100]", GRASSDROP_INT_MIN_VALUE, GRASSDROP_INT_MAX_VALUE);
		
		//creeper loot
		final int CREEPERLOOT_INT_MIN_VALUE = 0;
		final int CREEPERLOOT_INT_MAX_VALUE = 100;
		final int CREEPERLOOT_INT_DEFAULT_VALUE = 10;
		Property propCreeperLootChance = config.get(CATEGORY_NAME_LOOT, 
				"Chance (out of 100) for creepers to drop bombs.", 
				CREEPERLOOT_INT_DEFAULT_VALUE,
				"[0-100]", CREEPERLOOT_INT_MIN_VALUE, CREEPERLOOT_INT_MAX_VALUE);
		
		//mob loot frequency
		final int MOBLOOTFREQ_INT_MIN_VALUE = 0;
		final int MOBLOOTFREQ_INT_MAX_VALUE = 10;
		final int MOBLOOTFREQ_INT_DEFAULT_VALUE = 4;
		Property propMobLootFreq = config.get(CATEGORY_NAME_LOOT, 
				"Frequency of small heart and magic jar drops from mobs.", 
				MOBLOOTFREQ_INT_DEFAULT_VALUE,
				"[zero to disable; 1 = rare, 10 = very common]", MOBLOOTFREQ_INT_MIN_VALUE, MOBLOOTFREQ_INT_MAX_VALUE);
		

		// By defining a property order we can control the order of the
		// properties in the config file and GUI. This is defined on a per config-category basis.

		List<String> propOrderGeneral = new ArrayList<String>();
		propOrderGeneral.add(propHealthModification.getName()); // push the config value's name into the ordered list
		propOrderGeneral.add(propAlwaysHeartPickup.getName());
		propOrderGeneral.add(propClassicMode.getName());
		propOrderGeneral.add(propMaxHeart.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_GENERAL, propOrderGeneral);

		List<String> propOrderLoot = new ArrayList<String>();
		propOrderLoot.add(propHeartPieceWeight.getName());
		propOrderLoot.add(propGrassDropChance.getName());
		propOrderLoot.add(propCreeperLootChance.getName());
		propOrderLoot.add(propMobLootFreq.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_LOOT, propOrderLoot);

		/*
		 * ---- step 3 - read the configuration property values into the class's  -------------------
		 *               variables (if readFieldsFromConfig)
		 *
		 * As each value is read from the property, it should be checked to make
		 * sure it is valid, in case someone has manually edited or corrupted
		 * the value. The get() methods don't check that the value is in range
		 * even if you have specified a MIN and MAX value of the property.
		 */

		if (readFieldsFromConfig)
		{
			// If getInt() cannot get an integer value from the config file
			// value of myInteger (e.g. corrupted file).
			// It will set it to the default value passed to the function.

			healthModification = propHealthModification.getBoolean(HEALTHMODIFICATION_DEFAULT_VALUE);
			alwaysHeartsPickup = propAlwaysHeartPickup.getBoolean(ALWAYSHEARTPICKUP_DEFAULT_VALUE);
			enableClassicMode = propClassicMode.getBoolean(CLASSICMODE_DEFAULT_VALUE);
			maxHeart = propMaxHeart.getString();
			boolean matched = false;
			for (String entry : MAXHEART_CHOICES) 
			{
				if (entry.equals(maxHeart)) 
				{
					matched = true;
					break;
				}
			}
			if (!matched) 
			{
				maxHeart = MAXHEART_DEFAULT_VALUE;
			}
			
			heartpieceweight = propHeartPieceWeight.getInt(HEARTPIECE_INT_DEFAULT_VALUE);
			if (heartpieceweight > HEARTPIECE_INT_MAX_VALUE || heartpieceweight < HEARTPIECE_INT_MIN_VALUE) 
			{
				heartpieceweight = HEARTPIECE_INT_DEFAULT_VALUE;
			}
			
			grassDropChance = propGrassDropChance.getInt(GRASSDROP_INT_DEFAULT_VALUE);
			if (grassDropChance > GRASSDROP_INT_MAX_VALUE || grassDropChance < GRASSDROP_INT_MIN_VALUE) 
			{
				grassDropChance = GRASSDROP_INT_DEFAULT_VALUE;
			}
			
			creeperLoot = propCreeperLootChance.getInt(CREEPERLOOT_INT_DEFAULT_VALUE);
			if (creeperLoot > CREEPERLOOT_INT_MAX_VALUE || creeperLoot < CREEPERLOOT_INT_MIN_VALUE) 
			{
				creeperLoot = CREEPERLOOT_INT_DEFAULT_VALUE;
			}
			
			mobLootFrequency = propMobLootFreq.getInt(MOBLOOTFREQ_INT_DEFAULT_VALUE);
			if (mobLootFrequency > MOBLOOTFREQ_INT_MAX_VALUE || mobLootFrequency < MOBLOOTFREQ_INT_MIN_VALUE) 
			{
				mobLootFrequency = MOBLOOTFREQ_INT_DEFAULT_VALUE;
			}

		/*
		 * ---- step 4 - write the class's variables back into the config  -------------------
		 *               properties and save to disk
		 *
		 * This is done even for a 'loadFromFile==true', because some of the
		 * properties may have been assigned default values if the file was empty or corrupt.
		 */	
			propHealthModification.set(healthModification);
			propAlwaysHeartPickup.set(alwaysHeartsPickup);
			propClassicMode.set(enableClassicMode);
			propMaxHeart.set(maxHeart);
			propHeartPieceWeight.set(heartpieceweight);
			propGrassDropChance.set(grassDropChance);
			propCreeperLootChance.set(creeperLoot);
			propMobLootFreq.set(mobLootFrequency);
						
			if (config.hasChanged()) 
			{
				config.save();
			}
		}
	}

	// Define your configuration object
	private static Configuration config = null;

	public static class ConfigEventHandler
	{
		/*
	     * This class, when instantiated as an object, will listen on the Forge
	     * event bus for an OnConfigChangedEvent
	     */
		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if (LOZ.MODID.equals(event.getModID()) && !event.isWorldRunning())
			{
				if (event.getConfigID().equals(CATEGORY_NAME_GENERAL) || event.getConfigID().equals(CATEGORY_NAME_LOOT))
				{
					syncFromGUI();
				}
			}
		}
	}
}
