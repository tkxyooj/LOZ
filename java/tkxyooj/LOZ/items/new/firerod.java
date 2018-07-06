
interface IRepairable
{
    /**
    * Try repairing the item with the given itemstacks.
    * ALL non-null itemstacks in repairItems have to be usable.
    *
    * Remove the used items from repairItems. Decrease their stacksize or set their entries to null.
    * Returns the repaired item.
    *
    * @param repairable  The item to repair
    * @param repairItems The items to repair with
    * @return The returned item or null if repairItems retairns a non-null entry that can't be used for repairing or if the tool already is fully repaired.
    */
    @Nonnull
    ItemStack repair(ItemStack repairable, NonNullList<ItemStack> repairItems);
}

public class ItemMagicRod extends BaseModItem implements IFairyUpgrade, ISacredFlame, ISpawnParticles, IUnenchantable
{
	/** The type of magic this rod uses (FIRE or ICE) */
	private final MagicType magicType;

    private enum MagicType
    {
        FIRE("fire", true, "textures/blocks/lava_still.png", Sounds.MAGIC_FIRE, EnumParticleTypes.FLAME);
        
        private final String unlocalizedName;
        private final boolean affectsBlocks;
        private final ResourceLocation texture;
        private final String moveSound;
        private final EnumParticleTypes trailingParticle;
    
        private MagicType(String name, boolean affectsBlocks, String texture, String moveSound, EnumParticleTypes trailingParticle) 
        {
            this.unlocalizedName = name;
            this.affectsBlocks = affectsBlocks;
            this.texture = new ResourceLocation(texture);
            this.moveSound = moveSound;
            this.trailingParticle = trailingParticle;
        }
    }
    
	/** The amount of damage inflicted by the rod's projectile spell */
	private final float damage;

	/** Amount of durability lost when used */
	private final float magic_cost;

	/**
	 * @param magicType	The type of magic this rod uses (FIRE or ICE)
	 * @param damage	The amount of damage inflicted by the rod's projectile spell
	 * @param magic_cost	Amount of durability lost when used
	 */
	public ItemMagicRod(MagicType magicType, float damage, float magic_cost) 
    {
		super();
		this.magicType = magicType;
		this.damage = damage;
		this.magic_cost = magic_cost;
		setFull3D();
		setMaxDamage(getMaxItemUseDuration(this));
		setMaxStackSize(1);
		setCreativeTab(ZSSCreativeTabs.tabTools);
	}
    
	/**
	 * Returns the next time this stack may be used
	 */
	private long getNextUseTime(ItemStack stack) 
    {
		return (stack.hasTagCompound() ? stack.getTagCompound().getLong("next_use") : 0);
	}

	/**
	 * Sets the next time this stack may be used to the current world time plus a number of ticks
	 */
	private void setNextUseTime(ItemStack stack, World world, int ticks) 
    {
		if (!stack.hasTagCompound()) { stack.setTagCompound(new NBTTagCompound()); }
		stack.getTagCompound().setLong("next_use", (world.getTotalWorldTime() + ticks));
	}

   	/**
	 * Returns true if the rod is broken
	 */
	private boolean isBroken(ItemStack stack) 
    {
		return (stack.hasTagCompound() && stack.getTagCompound().getBoolean("isBroken"));
	} 
    
	/**
	 * Returns true if the rod is upgraded to the MK2 version
	 */
	private boolean isUpgraded(ItemStack stack) 
    {
		return (stack.hasTagCompound() && stack.getTagCompound().getBoolean("isUpgraded"));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) 
    {
		String s = (isUpgraded(stack) ? StatCollector.translateToLocal("item.firerod.mk2") + " " : "");
		return s + StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}

	@Override
	public int getItemEnchantability() 
    {
		return 0;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) 
    {
        if (isUpgraded)
        {
            return 72000;
        }
        else
        {
            return 36000;
        }
	}

    @Override
    public void setDamage(ItemStack stack, int damage) 
    {
        int max = getMaxItemUseDuration(stack);
        super.setDamage(stack, Math.min(max, damage));

        if(getDamage(stack) == max) 
        {
        breakTool(stack, null);
        }
    }
    
    //Setting the item with the broken tag
    public static void breakTool(ItemStack stack, EntityLivingBase entity) 
    {
        item.getEntityItem().getTagCompound().setBoolean("isBroken", true);
    }
    
    /** Repairing the fire rod */
    public static void unbreakTool(ItemStack stack) 
    {
        if(isBroken(stack)) 
        {
            // ensure correct damage value
            stack.setItemDamage(stack.getMaxDamage());
    
            // setItemDamage might break the tool again, so we do this afterwards
            item.getEntityItem().getTagCompound().setBoolean("isBroken", false);
        }
    }
    
    public static class OnRepair
    {
        public final int amount;

        public OnRepair(ItemStack itemStack, int amount) 
        {
            super(itemStack);
            this.amount = amount;
        }

        public static boolean fireEvent(ItemStack itemStack, int amount) 
        {
            OnRepair event = new OnRepair(itemStack, amount);
            return !MinecraftForge.EVENT_BUS.post(event);
        }
    }
    
    public static void healTool(ItemStack stack, int amount, EntityLivingBase entity) 
    {
        damageTool(stack, -amount, entity);
    }

    public static void damageTool(ItemStack stack, int amount, EntityLivingBase entity) 
    {
        if(amount == 0 || isBroken(stack)) 
        {
            return;
        }

        int actualAmount = amount;

        // ensure we never deal more damage than durability
        actualAmount = Math.min(actualAmount, getCurrentDurability(stack));
        stack.setItemDamage(stack.getItemDamage() + actualAmount);

        if(getCurrentDurability(stack) == 0) 
        {
            breakTool(stack, entity);
        }
    }
    
    // entity is optional, only needed for rendering break effect, never needed when repairing    
    public static void repairTool(ItemStack stack, int amount) 
    {
        repairTool(stack, amount, null);
    }
    
    public static void repairTool(ItemStack stack, int amount, EntityLivingBase entity) 
    {
        unbreakTool(stack);

        OnRepair.fireEvent(stack, amount);

        healTool(stack, amount, entity);
    }
    /** Repairing the fire rod */
    //Getting the XP
    public static int getPlayerXP(EntityPlayer player) 
    {
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static void addPlayerXP(EntityPlayer player, int amount) 
    {
		int experience = getPlayerXP(player) + amount;
		if (experience < 0) 
        {
			return;
		}
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (experience - expForLevel) / (float) player.xpBarCap();
	}

	public static int getLevelForExperience(int experience) 
    {
		int i = 0;
		while (getExperienceForLevel(i) <= experience) 
        {
			i++;
		}
		return i - 1;
	}

	public static int getExperienceForLevel(int level) 
    {
		if (level == 0) 
        {
			return 0;
		}
		if (level > 0 && level < 16) 
        {
			return (int) (Math.pow(level, 2)+ 6 * level);
		} 
        else if (level > 15 && level < 32) 
        {
			return (int) (2.5* Math.pow(level, 2) - 40.5 * level + 360);
		} 
        else 
        {
			return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
		}
    }
    //Event to repair
    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) 
    {
		if (event.phase == TickEvent.Phase.END && (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().player != null)) 
        {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
            if ((Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) && (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())) 
            {
            	if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().isItemEnchanted())
                {
            		ItemStack held = player.getHeldItemMainhand();
            		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("mending"), held) > 0)
                    {
            			if(player.experienceTotal >= 2 && held.isItemDamaged())
                        {
        					BetterThanMending.network.sendToServer(new RepairItemPacket());
        					
            			}
            		}
            	}
            }
		}
	}
    
    public void repairtherod()
    {
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
		mainThread.addScheduledTask(new Runnable() 
        {
			@Override
			public void run() 
            {
				EntityPlayerMP player = ctx.getServerHandler().player;
				if (player.getHeldItemMainhand() != null) 
                {
					ItemStack held = player.getHeldItemMainhand();
					held.setItemDamage(held.getItemDamage() - 4);
					Utilities.addPlayerXP(player, -2);
				}
			}
		});
			return null;
	}
	
	//Cancel the armor-equip-on-rightclick functionality if conditions for repairing are met instead
	@SubscribeEvent
	public void onItemUse(PlayerInteractEvent.RightClickItem event)
    {
		if(event.getItemStack().getItem() instanceof ItemArmor || event.getItemStack().getItem() instanceof ItemElytra)
        {
			if(event.getItemStack().isItemDamaged() && EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("mending"), event.getItemStack()) > 0)
            {
				if ((event.getEntityPlayer().isSneaking()))
                {
					event.setCanceled(true);
				}
			}
		}
    }
    
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
    {
		float mp = (player.isSneaking() ? magic_cost : magic_cost / 20.0F);
		boolean isUpgraded = isUpgraded(stack);
        if (isUpgraded) 
        {
			mp *= 1.5F;
		}
		if (player.capabilities.isCreativeMode || (world.getTotalWorldTime() > getNextUseTime(stack) && !isBroken(stack))) 
        {
			if (player.isSneaking()) 
            {
				EntityMobThrowable magic;

				magic = new EntityMagicSpell(world, player).setType(magicType).setArea(isUpgraded ? 3.0F : 2.0F);
				magic.setDamage(isUpgraded ? damage * 1.5F : damage);
				if (!world.isRemote) 
                {
					WorldUtils.playSoundAtEntity(player, Sounds.WHOOSH, 0.4F, 0.5F);
					world.spawnEntityInWorld(magic);
				}
				setNextUseTime(stack, world, 10); // prevents use during swing animation
				player.swingItem();
			} 
            else 
            {
				player.setItemInUse(stack, getMaxItemUseDuration(stack));
			}
		} 
        else 
        {
			// TODO need better magic fail sound...
			player.playSound(Sounds.MAGIC_FAIL, 1.0F, 1.0F);
		}
		return stack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if (!player.worldObj.isRemote) 
        {
            setMaxDamage--;
			int ticksInUse = getMaxItemUseDuration(stack) - count;
			float mp = (magic_cost / 50.0F) * (isUpgraded(stack) ? 1.5F : 1.0F);
			if (ticksInUse % 4 == 0 && !isBroken(stack)) 
            {
				player.clearItemInUse();//EntityLivingBase#stopActiveHand
			} 
            else 
            {
				handleUpdateTick(stack, player.worldObj, player, ticksInUse);
			}
		}
	}
    
    	/**
	 * Handles fire and ice rod update tick
	 */
	private void handleUpdateTick(ItemStack stack, World world, EntityPlayer player, int ticksInUse) 
    {
		float r = 0.5F + Math.min(5.5F, (ticksInUse / 10F));
		if (isUpgraded(stack)) 
        {
			r *= 1.5F;
		}
		PacketDispatcher.sendToAllAround(new PacketISpawnParticles(player, r), player, 64.0D);
		if (ticksInUse % 4 == 3) {
			if (Config.getRodFireGriefing()) 
            {
				affectBlocks(world, player, r);
			}
			List<EntityLivingBase> targets = TargetUtils.acquireAllLookTargets(player, Math.round(r), 1.0F);
			for (EntityLivingBase target : targets) 
            {
				target.attackEntityFrom(getDamageSource(player), r);
				if (magicType == MagicType.FIRE && !target.isImmuneToFire()) 
                {
					target.setFire(5);
				}
			}
		}
		if (ticksInUse % magicType.getSoundFrequency() == 0) 
        {
			world.playSoundAtEntity(player, magicType.getMovingSound(), magicType.getSoundVolume(world.rand), magicType.getSoundPitch(world.rand));
		}
	}

	/**
	 * Affects blocks within the area of effect provided there is line of sight to the player
	 */
	private void affectBlocks(World world, EntityPlayer player, float radius) 
    {
		Set<BlockPos> affectedBlocks = new HashSet<BlockPos>();
		Vec3 vec3 = player.getLookVec();
		double x = player.posX + vec3.xCoord;
		double y = player.posY + player.getEyeHeight() + vec3.yCoord;
		double z = player.posZ + vec3.zCoord;
		int r = MathHelper.ceiling_float_int(radius);
		for (int n = 0; n < r; ++n) 
        {
			int i = MathHelper.floor_double(x);
			int j = MathHelper.floor_double(y);
			int k = MathHelper.floor_double(z);
			if (canAddBlockPosition(world, player, i, j, k)) 
            {
				affectedBlocks.add(new BlockPos(i, j, k));
			}
			x += vec3.xCoord;
			y += vec3.yCoord;
			z += vec3.zCoord;
		}

		affectAllBlocks(world, affectedBlocks, magicType);
	}

	private boolean canAddBlockPosition(World world, EntityPlayer player, int i, int j, int k) 
    {
		Vec3 vec31 = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 vec32 = new Vec3(i, j, k);
		MovingObjectPosition mop = world.rayTraceBlocks(vec31, vec32);
		return (mop == null || (mop.typeOfHit == MovingObjectType.BLOCK && (mop.getBlockPos().getX() == i && mop.getBlockPos().getY() == j && mop.getBlockPos().getZ() == k) || !world.getBlockState(mop.getBlockPos()).getBlock().getMaterial().blocksLight()));
	}

	/**
	 * Affects all blocks in the set of chunk positions with the magic type's effect (freeze, thaw, etc.)
	 */
	public static void affectAllBlocks(World world, Set<BlockPos> blocks, MagicType type) {
		for (BlockPos pos : blocks) 
        {
			Block block = world.getBlockState(pos).getBlock();
			if (WorldUtils.canMeltBlock(world, block, pos) && world.rand.nextInt(4) == 0) 
            {
				world.setBlockToAir(pos);
				world.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, Sounds.FIRE_FIZZ, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			} 
            else if (block.getMaterial() == Material.air && world.getBlockState(pos.down()).getBlock().isFullBlock() && world.rand.nextInt(8) == 0) 
            {
				world.setBlockState(pos, Blocks.fire.getDefaultState());
				world.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, Sounds.FIRE_IGNITE, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			} 
            else if (block == ZSSBlocks.bombFlower) 
            {
				block.onBlockExploded(world, pos, null);
			}

		}
	}

	/** Only used for Fire and Ice Rods */
	private DamageSource getDamageSource(EntityPlayer player) 
    {
		return new DamageSourceFire("blast.fire", player, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void spawnParticles(World world, EntityPlayer player, ItemStack stack, double x, double y, double z, float r) 
    {
		y += player.getEyeHeight();
		Vec3 look = player.getLookVec();
		for (float f = 0; f < r; f += 0.5F) 
        {
			x += look.xCoord;
			y += look.yCoord;
			z += look.zCoord;
			for (int i = 0; i < 4; ++i) 
            {
				world.spawnParticle(magicType.getTrailingParticle(),
				x + 0.5F - world.rand.nextFloat(),
				y + 0.5F - world.rand.nextFloat(),
				z + 0.5F - world.rand.nextFloat(),
				look.xCoord * (0.5F * world.rand.nextFloat()),
				look.yCoord * 0.15D,
				look.zCoord * (0.5F * world.rand.nextFloat()));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) 
    {
		return true;
	}
    
    @Override
	public void handleFairyUpgrade(EntityItem item, EntityPlayer player, TileEntityDungeonCore core) 
    {
		int cost = Math.round(Config.getRodUpgradeCost() * (magicType == MagicType.WIND ? 0.75F : 1.0F));
		if (core.consumeRupees(cost)) 
        {
			if (!item.getEntityItem().hasTagCompound()) 
            {
				item.getEntityItem().setTagCompound(new NBTTagCompound());
			}
			item.getEntityItem().getTagCompound().setBoolean("isUpgraded", true);
			core.getWorld().playSoundEffect(core.getPos().getX() + 0.5D, core.getPos().getY() + 1, core.getPos().getZ() + 0.5D, Sounds.SECRET_MEDLEY, 1.0F, 1.0F);
		} 
        else 
        {
			core.getWorld().playSoundEffect(core.getPos().getX() + 0.5D, core.getPos().getY() + 1, core.getPos().getZ() + 0.5D, Sounds.FAIRY_LAUGH, 1.0F, 1.0F);
			PlayerUtils.sendTranslatedChat(player, "chat.zss.fairy.laugh.unworthy");
		}
    }
}
