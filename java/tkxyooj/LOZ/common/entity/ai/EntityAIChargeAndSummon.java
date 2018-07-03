package tkxyooj.LOZ.common.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import tkxyooj.LOZ.common.entity.EntityGanon;
import tkxyooj.LOZ.common.entity.EntityGanonMinion;

public class EntityAIChargeAndSummon extends EntityAIBase {

	public interface ICharger 
	{
		public boolean isCharging();
		public void setCharging(boolean flag);
	}

	private static final double MIN_RANGE_SQ = 16.0D;
	private static final double MAX_RANGE_SQ = 64.0D;
	private static final int FREQ = 1;

	private final EntityGanon ganon;
	private EntityLivingBase chargeTarget;
	private double chargeX;
	private double chargeY;
	private double chargeZ;

	protected float speed;

	private final boolean canBreak;

	private int windup;

	private boolean hasAttacked;

	public EntityAIChargeAndSummon(EntityCreature entityLiving, float f, boolean canBreak) {
		this.ganon = (EntityGanon) entityLiving;
		this.speed = f;
		this.canBreak = canBreak;
		this.windup = 0;
		this.hasAttacked = false;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		this.chargeTarget = this.ganon.getAttackTarget();

		if (this.chargeTarget == null) {
			return false;
		} else {
			double distance = this.ganon.getDistanceSq(this.chargeTarget);
			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
				return false;
			} else if (!this.ganon.onGround) {
				return false;
			} else {
				Vec3d chargePos = findChargePoint(ganon, chargeTarget, 2.1);
				boolean canSeeTargetFromDest = ganon.getEntitySenses().canSee(chargeTarget);
				if (!canSeeTargetFromDest) {
					return false;
				} else {
					chargeX = chargePos.x;
					chargeY = chargePos.y;
					chargeZ = chargePos.z;

					return this.ganon.getRNG().nextInt(FREQ) == 0;
				}
			}

		}
	}

	@Override
	public void startExecuting() {
		this.windup = 8 + this.ganon.getRNG().nextInt(30);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return windup > 0 || !this.ganon.getNavigator().noPath();
	}

	@Override
	public void updateTask() {
		// look where we're going
		this.ganon.getLookHelper().setLookPosition(chargeX, chargeY - 1, chargeZ, 10.0F, this.ganon.getVerticalFaceSpeed());

		if (windup > 0) {
			if (--windup == 0) {
				// actually charge!

				this.ganon.getNavigator().tryMoveToXYZ(chargeX, chargeY, chargeZ, this.speed);
			} else {
				this.ganon.limbSwingAmount += 0.8;

				if (this.ganon instanceof ICharger) {
					((ICharger) ganon).setCharging(true);
				}
			}
		} else if (canBreak) {
			if (!ganon.world.isRemote && ganon.world.getGameRules().getBoolean("mobGriefing")) {
				AxisAlignedBB bb = ganon.getEntityBoundingBox();
				int minx = MathHelper.floor(bb.minX - 0.75D);
				int miny = MathHelper.floor(bb.minY + 0.0D);
				int minz = MathHelper.floor(bb.minZ - 0.75D);
				int maxx = MathHelper.floor(bb.maxX + 0.75D);
				int maxy = MathHelper.floor(bb.maxY + 0.15D);
				int maxz = MathHelper.floor(bb.maxZ + 0.75D);
				if (ganon.world.isAreaLoaded(new BlockPos(minx, miny, minz), new BlockPos(maxx, maxy, maxz))) {
					for (int dx = minx; dx <= maxx; dx++) {
						for (int dy = miny; dy <= maxy; dy++) {
							for (int dz = minz; dz <= maxz; dz++) {
								BlockPos pos = new BlockPos(dx, dy, dz);
								IBlockState state = ganon.world.getBlockState(pos);
								float hardness = state.getBlockHardness(ganon.world, pos);
								if (hardness >= 0 && hardness < 50 && ganon.world.getTileEntity(pos) == null)
									ganon.world.destroyBlock(pos, true);
							}
						}
					}
				}
			}
		}

		// attack the target when we get in range
		double var1 = this.ganon.width * 2.1F * this.ganon.width * 2.1F;

		if (this.ganon.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.getEntityBoundingBox().minY, this.chargeTarget.posZ) <= var1) {
			if (!this.hasAttacked) {
				this.hasAttacked = true;
				this.ganon.attackEntityAsMob(this.chargeTarget);
			}
		}
		if (ganon.getAttackCooldown() % 15 == 0) {
			checkAndSpawnMinions();
		}
	}
	
	private void checkAndSpawnMinions() {
		if (!ganon.world.isRemote && ganon.getMinionsToSummon() > 0) {
			int minions = ganon.countMyMinions();

			// if not, spawn one!
			if (minions < EntityGanon.MAX_ACTIVE_MINIONS) {
				spawnMinionAt();
				ganon.setMinionsToSummon(ganon.getMinionsToSummon() - 1);
			}
		}
		// if there's no minions left to summon, we should move into phase 3 naturally
	}

	private void spawnMinionAt() {
		// find a good spot
		EntityLivingBase targetedEntity = ganon.getAttackTarget();
		Vec3d minionSpot = ganon.findVecInLOSOf(targetedEntity);

		if (minionSpot != null) {
			// put a clone there
			EntityGanonMinion minion = new EntityGanonMinion(ganon.world, ganon);
			minion.setPosition(minionSpot.x, minionSpot.y, minionSpot.z);
			minion.onInitialSpawn(ganon.world.getDifficultyForLocation(new BlockPos(minionSpot)), null);
			ganon.world.spawnEntity(minion);

			minion.setAttackTarget(targetedEntity);

			minion.spawnExplosionParticle();
			minion.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, ((ganon.getRNG().nextFloat() - ganon.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
	}
	@Override
	public void resetTask() {
		this.windup = 0;
		this.chargeTarget = null;
		this.hasAttacked = false;

		if (this.ganon instanceof ICharger) {
			((ICharger) ganon).setCharging(false);
		}
	}


	/**
	 * Finds a point that is overshoot blocks "beyond" the target from our position.
	 */
	protected Vec3d findChargePoint(Entity attacker, Entity target, double overshoot) {

		// compute angle
		double vecx = target.posX - attacker.posX;
		double vecz = target.posZ - attacker.posZ;
		float rangle = (float) (Math.atan2(vecz, vecx));

		double distance = MathHelper.sqrt(vecx * vecx + vecz * vecz);

		// figure out where we're headed from the target angle
		double dx = MathHelper.cos(rangle) * (distance + overshoot);
		double dz = MathHelper.sin(rangle) * (distance + overshoot);

		// add that to the target entity's position, and we have our destination
		return new Vec3d(attacker.posX + dx, target.posY, attacker.posZ + dz);
	}


}