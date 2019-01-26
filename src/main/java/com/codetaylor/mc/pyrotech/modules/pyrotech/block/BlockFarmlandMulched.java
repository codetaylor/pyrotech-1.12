package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.network.SCPacketParticleBoneMeal;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileFarmlandMulched;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockFarmlandMulched
    extends BlockPartialBase {

  public static final String NAME = "farmland_mulched";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.9375, 1);
  private static final AxisAlignedBB AABB_NEGATIVE = new AxisAlignedBB(0, 0.9375, 0, 1, 1, 1);

  public BlockFarmlandMulched() {

    super(Material.GROUND);
    this.setHardness(0.7f);
    this.setTickRandomly(true);
    this.setLightOpacity(255);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {

    if (!world.isRemote
        && ModulePyrotechConfig.MULCHED_FARMLAND.ALLOW_TRAMPLE
        && entity.canTrample(world, this, pos, fallDistance)) {
      this.turnToDirt(world, pos);
    }

    super.onFallenUpon(world, pos, entity, fallDistance);
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

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileFarmlandMulched();
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (rand.nextFloat() > 0.5) {
      return;
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (!(tileEntity instanceof TileFarmlandMulched)) {
      return;
    }

    TileFarmlandMulched tile = (TileFarmlandMulched) tileEntity;

    BlockPos posUp = pos.up();
    IBlockState blockStateUp = world.getBlockState(posUp);

    if (blockStateUp.getBlock() instanceof IGrowable) {
      IGrowable growable = (IGrowable) blockStateUp.getBlock();

      if (!world.isRemote
          && growable.canGrow(world, posUp, blockStateUp, false)
          && growable.canUseBonemeal(world, world.rand, posUp, blockStateUp)) {

        growable.grow(world, world.rand, posUp, blockStateUp);

        if (!ModulePyrotechConfig.MULCHED_FARMLAND.UNLIMITED_CHARGES) {
          tile.decrementRemainingCharges();
        }

        ModulePyrotech.PACKET_SERVICE.sendToAllAround(
            new SCPacketParticleBoneMeal(posUp, 4),
            world.provider.getDimension(),
            posUp
        );
      }
    }

    if (tile.getRemainingCharges() == 0) {
      world.setBlockState(pos, Blocks.FARMLAND.getDefaultState()
          .withProperty(BlockFarmland.MOISTURE, 7), 1 | 2);
    }
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

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {

    return (face == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}
