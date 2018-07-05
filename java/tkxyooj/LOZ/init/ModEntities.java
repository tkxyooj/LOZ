package tkxyooj.LOZ.common.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tkxyooj.LOZ.LOZ;

public class ModEntities 
{
	public static void init() {
		int id = 0;
//		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, MANA_BURST_REGISTRY), EntityManaBurst.class, LibEntityNames.MANA_BURST, id++, Botania.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.SIGNAL_FLARE_REGISTRY, EntitySignalFlare.class, LibEntityNames.SIGNAL_FLARE, id++, Botania.instance, 2048, 10, false);			EntityRegistry.registerModEntity(LibEntityNames.PIXIE_REGISTRY, EntityPixie.class, LibEntityNames.PIXIE, id++, Botania.instance, 16, 3, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.FLAME_RING_REGISTRY, EntityFlameRing.class, LibEntityNames.FLAME_RING, id++, Botania.instance, 32, 40, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.VINE_BALL_REGISTRY, EntityVineBall.class, LibEntityNames.VINE_BALL, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LOZ.MODID, "TwilightPrincess"), EntityTwilightPrincess.class, "Twilight Princess", id++, LOZ.instance, 128, 3, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.MAGIC_LANDMINE_REGISTRY, EntityMagicLandmine.class, LibEntityNames.MAGIC_LANDMINE, id++, Botania.instance, 128, 40, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.SPARK_REGISTRY, EntitySpark.class, LibEntityNames.SPARK, id++, Botania.instance, 64, 10, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.THROWN_ITEM_REGISTRY, EntityThrownItem.class, LibEntityNames.THROWN_ITEM, id++, Botania.instance, 64, 20, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.MAGIC_MISSILE_REGISTRY, EntityMagicMissile.class, LibEntityNames.MAGIC_MISSILE, id++, Botania.instance, 64, 2, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.THORN_CHAKRAM_REGISTRY, EntityThornChakram.class, LibEntityNames.THORN_CHAKRAM, id++, Botania.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.CORPOREA_SPARK_REGISTRY, EntityCorporeaSpark.class, LibEntityNames.CORPOREA_SPARK, id++, Botania.instance, 64, 10, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.ENDER_AIR_BOTTLE_REGISTRY, EntityEnderAirBottle.class, LibEntityNames.ENDER_AIR_BOTTLE, id++, Botania.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.POOL_MINECART_REGISTRY, EntityPoolMinecart.class, LibEntityNames.POOL_MINECART, id++, Botania.instance, 80, 3, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.PINK_WITHER_REGISTRY, EntityPinkWither.class, LibEntityNames.PINK_WITHER, id++, Botania.instance, 80, 3, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.PLAYER_MOVER_REGISTRY, EntityPlayerMover.class, LibEntityNames.PLAYER_MOVER, id++, Botania.instance, 40, 3, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.MANA_STORM_REGISTRY, EntityManaStorm.class, LibEntityNames.MANA_STORM, id++, Botania.instance, 64, 10, false);
//		EntityRegistry.registerModEntity(LOZ.MODID.BABYLON_WEAPON_REGISTRY, EntityBabylonWeapon.class, LibEntityNames.BABYLON_WEAPON, id++, Botania.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(LOZ.MODID.FALLING_STAR_REGISTRY, EntityFallingStar.class, LibEntityNames.FALLING_STAR, id++, Botania.instance, 64, 10, true);
	}
}
