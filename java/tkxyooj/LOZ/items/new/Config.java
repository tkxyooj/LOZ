public class Config
{
	public static Configuration config;
  
  /** Allow modification of health **/
  private static boolean healthModification;
  
  /** Always pick up small hearts regardless of health */
  private static boolean alwaysHeartsPickup;
  
	/** Classic Zelda Start: Start with 3 hearts (can be disabled) **/
  private static boolean enableClassicMode;
  
	/** Max number of hearts (NONE, MINECRAFT, CLASSIC) **/
	private static int maxHeart;
	
	/** Chance of grass dropping hearts/rupees (set to zero to disable) **/
	private static float grassDropChance;

	/** Chance of Creepers dropping bombs (set to zero to disable) **/
	private static float creeperLoot;
  
	/** Frequency of small heart and rupee drops from mobs [zero to disable; 1 = rare, 10 = very common] */
	private static int mobLootFrequency;

	public static void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + ModInfo.CONFIG_PATH));
		config.load();
		ZSSItems.initConfig(config);
    String maxHeartString = config.get("General", "Max Health Obtainable. None is for unlimited health", "NONE");

		String category = "client";
		config.addCustomCategoryComment(category, "Configuration file for LOZ mod. These are game changing modifications that can be configured. Certain features will not be configurable and will need other mods, such as Crafttweaker, to modify.");
		
    healthModification = config.get("General", "Allow LOZ mod to modify health. Set to false if you have another mod that modifies player health.", true).getBoolean(true);
    alwaysHeartsPickup = config.get("General", "Always pick up small heart drops regardless of health", true).getBoolean(true);
		enableClassicMode = config.get("General", "Classic Zelda Mode: Start with only 3 hearts and it can be enabled/disabled at any time)", true).getBoolean(true);
		maxHeart = switch (maxHeartString.toLowerCase())
    {
      case "none":
      maxHeart = (int)Double.POSITIVE_INFINITY;
      break;
      case "minecraft":
      maxHeart = 20;
      break;
      case "classic":
      maxHeart = 40;
      break;
    }
    heartPieceWeight = MathHelper.clamp_int(config.get("Loot", "Weight: Heart Piece (vanilla chests only) [1-10]", 1).getInt(), 1, 10);
		grassDropChance = 0.01F * (float) MathHelper.clamp_int(config.get("Loot", "Chance (as a percent) of loot dropping from grass [0-100]", 15).getInt(), 0, 100);
		creeperLoot = 0.01F * (float) MathHelper.clamp_int(config.get("Loot", "Chance (as a percent) for creepers to drop bombs [0-100]", 10).getInt(), 0, 100);
		mobLootFrequency = MathHelper.clamp_int(config.get("Loot", "Frequency of small heart and magic jar drops from mobs [zero to disable; 1 = rare, 10 = very common]", 5).getInt(), 0, 10);
		
		config.save();
	}

	public static void postInit() 
  {
    public static boolean alwaysPickupHearts() { return alwaysHeartsPickup; }
	  public static boolean isClassicMode() { return enableClassicMode; }
	  public static int getMaxHearts() { return maxHeart; }
	  public static float getHeartPieceChance() { return heartPieceChance; }
	  public static int getHeartWeight() { return heartPieceWeight; }
	  public static float getGrassDropChance() { return grassDropChance; }
	  public static float getCreeperLootChance() { return creeperLoot; }
	  public static int getMobLootFrequency() { return mobLootFrequency; }
	}
}
