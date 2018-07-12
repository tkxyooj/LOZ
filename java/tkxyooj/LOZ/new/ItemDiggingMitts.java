public class ItemDiggingMitts extends ItemTool
{
  public static toolMaterial = new toolMaterial;
	public ItemDiggingMitts()
	{
		super(4, -2.4F, toolMaterial, new HashSet<>());
        setCreativeTab(CREATIVETABLOCATION);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return state.getBlock() != Blocks.BEDROCK;
	}

	@Override
	public float getStrVsBlock(ItemStack itemstack, IBlockState state)
	{
		return state.getBlock() != Blocks.BEDROCK ? efficiencyOnProperMaterial : 1.0F;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		Block block = world.getBlockState(pos).getBlock();
		
		if(!player.isSneaking() && (block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.GRASS_PATH))
		{
			if(useHoe(stack, player, world, pos, side) == EnumActionResult.FAIL)
			{
				return EnumActionResult.FAIL;
			}

			for(int x1 = -1; x1 <= +1; x1++)
			{
				for(int z1 = -1; z1 <= +1; z1++)
				{
					useHoe(stack, player, world, pos.add(x1, 0, z1), side);
				}
			}
			return EnumActionResult.SUCCESS;
		}
        
        if(player.isSneaking())
		{
			if(block == Blocks.DIRT || block == Blocks.GRASS)
			{
				for(int x1 = -1; x1 <= +1; x1++)
                {
                    for(int z1 = -1; z1 <= +1; z1++)
                    {
                        setBlock(stack, playerIn, worldIn, pos, Blocks.GRASS_PATH.getDefaultState());
                    }
                }
                return EnumActionResult.SUCCESS;
			}
            else
            {
                return EnumActionResult.FAIL;
            }
		}

		return EnumActionResult.PASS;
	}

	private EnumActionResult useHoe(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing facing)
	{
		if(!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
        {
            return EnumActionResult.FAIL;
        }
        else 
        {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos);
            if(hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if(facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
            {
                if(block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    setBlock(stack, playerIn, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if(block == Blocks.DIRT)
                {
                    switch((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            setBlock(stack, playerIn, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            setBlock(stack, playerIn, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
	}
	
	protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        switch(player.isSneaking())
        {
            case true:
                return worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            case false:
                return worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        if(!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }
}
