package tkxyooj.LOZ.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import tkxyooj.LOZ.common.entity.EntityTwilightPrincess;
import tkxyooj.LOZ.items.magic.ItemFlameTwilight;

public class EntityAIRangeAttack extends EntityAIBase {

	private final EntityTwilightPrincess midna;

	public EntityAIRangeAttack(EntityTwilightPrincess boss) {
		midna = boss;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return midna.getPhase() == 1;
	}

	@Override
	public void startExecuting() {
		midna.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack (ItemFlameTwilight.block));
	}

	@Override
	public void updateTask() {
		EntityLivingBase targetedEntity = midna.getAttackTarget();
		if (targetedEntity == null)
			return;
		float dist = midna.getDistance(targetedEntity);

		if (midna.getAttackCooldown() == 60) {
			midna.teleportToSightOfEntity(targetedEntity);
		}

		if (midna.getEntitySenses().canSee(targetedEntity) && midna.getAttackCooldown() == 0 && dist < 20F) {
			if (midna.getNextAttackType() == 0) {
				midna.launchBombFairy();
			} else {
				midna.launchStaticFairy();
			}

			if (midna.getRNG().nextInt(3) > 0) {
				midna.setNextAttackType(0);
			} else {
				midna.setNextAttackType(1);
			}
			midna.setAttackCooldown(60);
		}
	}
}