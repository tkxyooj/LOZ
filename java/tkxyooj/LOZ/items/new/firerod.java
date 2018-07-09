public class ItemMagicRod
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
	public int setMaxDamage() 
    {
        if (isUpgraded)
        {
            return 7200;
        }
        else
        {
            return 3600;
        }
	}

    /** Damaging the fire rod */
    @Override
    public void setDamage(ItemStack stack, int damage) 
    {
        //damage must never be more than max damage
        int max = setMaxDamage(stack);
        int actualDamage = damage;
        actualDamage = Math.min(actualDamage, getCurrentDurability(stack));
        
        super.setDamage(stack, Math.min(max, actualDamage));

        if(isBroken(stack)) 
        {
            return;
        }
        if(getCurrentDurability(stack) == 0) 
        {
            this.getEntityItem().getTagCompound().setBoolean("isBroken", true);
        }
    }
    
    /** Repairing the fire rod */
    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) 
    {
		if (event.phase == TickEvent.Phase.END && (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().player != null)) 
        {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
            while ((Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) && (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())) 
            {
            	if(player.getHeldItemOffhand() == ItemMagicPowderBag)
                {
            		repairRod(this, repairItem);
            	}
            }
		}
    }    
    
    public static void repairRod (ItemStack stack, ItemStack repairItem)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        repairItem = player.getHeldItemOffhand();
        
        if (repairItem.getCurrentDurability() == 0)
        {
            return;
        }
        
        if (stack.getCurrentDurability() == stack.getMaxDamage(stack))
        {
            return;
        }
        
        if(repairItem.getCurrentDurability() > 0 && isBroken(stack)) 
        {
            //update broken tag
            if(isBroken(stack)) 
            {
                item.getEntityItem().getTagCompound().setBoolean("isBroken", false);
            }
            setDamage(this, -1);
            setDamage(repairItem, 1);
        }
        
        setDamage(this, -1);
        setDamage(repairItem, 1);
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
				player.setItemInUse(stack, setMaxDamage(stack));
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
			int ticksInUse = setMaxDamage(stack) - count;
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
        //spawn particles
        stack.getItem()).spawnParticles(world, player, stack, player.posX, player.posY, player.posZ, 64);
		PacketDispatcher.sendToAllAround(new PacketISpawnParticles(player, r), player, 64.0D);
		if (ticksInUse % 4 == 3) {
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
