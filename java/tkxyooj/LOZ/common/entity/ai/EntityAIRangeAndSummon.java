package tkxyooj.LOZ.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincess;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincessMinion;
import tkxyooj.LOZ.items.magic.ItemFlameTwilight;

public class EntityAIRangeAndSummon extends EntityAIBase {

	private final EntityTwilightPrincess midna;

	public EntityAIRangeAndSummon(EntityTwilightPrincess boss) {
		midna = boss;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return midna.getPhase() == 2;
	}

	@Override
	public void startExecuting() {
		midna.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemFlameTwilight.block));
	}

	@Override
	public void updateTask() {
		EntityLivingBase targetedEntity = midna.getAttackTarget();
		if (targetedEntity == null)
			return;
		float dist = midna.getDistance(targetedEntity);
		// spawn minions every so often
		if (midna.getAttackCooldown() % 15 == 0) {
			checkAndSpawnMinions();
		}

		if (midna.getAttackCooldown() == 0) {
			if (dist < 2.0F) {
				// melee attack
				midna.attackEntityAsMob(targetedEntity);
				midna.setAttackCooldown(20);
			} else if (dist < 20F && midna.getEntitySenses().canSee(targetedEntity)) {
				if (midna.getNextAttackType() == 0) {
					midna.launchBombFairy();
				} else {
					midna.launchStaticFairy();
				}

				if (midna.getRNG().nextInt(2) > 0) {
					midna.setNextAttackType(0);
				} else {
					midna.setNextAttackType(1);
				}
				midna.setAttackCooldown(60);
			} else {
				// if not, teleport around
				midna.teleportToSightOfEntity(targetedEntity);
				midna.setAttackCooldown(20);

			}
		}
	}

	private void checkAndSpawnMinions() {
		if (!midna.world.isRemote && midna.getMinionsToSummon() > 0) {
			int minions = midna.countMyMinions();

			// if not, spawn one!
			if (minions < EntityTwilightPrincess.MAX_ACTIVE_MINIONS) {
				spawnMinionAt();
				midna.setMinionsToSummon(midna.getMinionsToSummon() - 1);
			}
		}
		// if there's no minions left to summon, we should move into phase 3 naturally
	}

	private void spawnMinionAt() {
		// find a good spot
		EntityLivingBase targetedEntity = midna.getAttackTarget();
		Vec3d minionSpot = midna.findVecInLOSOf(targetedEntity);

		if (minionSpot != null) {
			// put a clone there
			EntityTwilightPrincessMinion minion = new EntityTwilightPrincessMinion(midna.world, midna);
			minion.setPosition(minionSpot.x, minionSpot.y, minionSpot.z);
			minion.onInitialSpawn(midna.world.getDifficultyForLocation(new BlockPos(minionSpot)), null);
			midna.world.spawnEntity(minion);

			minion.setAttackTarget(targetedEntity);

			minion.spawnExplosionParticle();
			minion.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, ((midna.getRNG().nextFloat() - midna.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
	}

}