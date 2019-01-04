package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.SCPacketParticleBoneMeal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockFarmlandMulched
    extends BlockPartialBase {

  public static final String NAME = "farmland_mulched";
  public static final PropertyInteger FERTILIZER = PropertyInteger.create("fertilizer", 0, 15);

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.9375, 1);
  private static final AxisAlignedBB AABB_NEGATIVE = new AxisAlignedBB(0, 0.9375, 0, 1, 1, 1);

  public BlockFarmlandMulched() {

    super(Material.GROUND);
    this.setTickRandomly(true);
    this.setLightOpacity(255);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {

    EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));

    if (plantType == EnumPlantType.Crop) {
      return true;
    }

    return super.canSustainPlant(state, world, pos, direction, plantable);
  }

  public int getMaxFertilizer() {

    return 15; // TODO: config
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    int fertilizer = state.getValue(FERTILIZER);

    BlockPos posUp = pos.up();
    IBlockState blockStateUp = world.getBlockState(posUp);

    if (blockStateUp.getBlock() instanceof IGrowable) {
      IGrowable growable = (IGrowable) blockStateUp.getBlock();

      if (growable.canGrow(world, posUp, blockStateUp, world.isRemote)) {

        if (growable.canUseBonemeal(world, world.rand, posUp, blockStateUp)) {

          if (!world.isRemote) {
            growable.grow(world, world.rand, posUp, blockStateUp);
            world.setBlockState(pos, state
                .withProperty(FERTILIZER, fertilizer - 1), 1 | 2 | 4);
            ModulePyrotech.PACKET_SERVICE.sendToAllAround(
                new SCPacketParticleBoneMeal(posUp, 4),
                world.provider.getDimension(),
                posUp
            );
          }
        }
      }
    }

    if (fertilizer == 0) {
      world.setBlockState(pos, Blocks.FARMLAND.getDefaultState()
          .withProperty(BlockFarmland.MOISTURE, 7), 1 | 2);
    }
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

    return this.getDefaultState()
        .withProperty(FERTILIZER, this.getMaxFertilizer());
  }

  private void turnToDirt(World world, BlockPos pos) {

    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
    AxisAlignedBB bounds = AABB_NEGATIVE.offset(pos);

    for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, bounds)) {
      double d0 = Math.min(bounds.maxY - bounds.minY, bounds.maxY - entity.getEntityBoundingBox().minY);
      entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001D, entity.posZ);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    super.neighborChanged(state, world, pos, block, fromPos);

    if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
      this.turnToDirt(world, pos);
    }
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

    super.onBlockAdded(world, pos, state);

    if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
      this.turnToDirt(world, pos);
    }
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

    switch (side) {
      case UP:
        return true;
      case NORTH:
      case SOUTH:
      case WEST:
      case EAST:
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return !iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH && block != ModuleBlocks.FARMLAND_MULCHED;
      default:
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
  }

  @Nonnull
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(FERTILIZER, meta & this.getMaxFertilizer());
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(FERTILIZER);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, FERTILIZER);
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {

    return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}
