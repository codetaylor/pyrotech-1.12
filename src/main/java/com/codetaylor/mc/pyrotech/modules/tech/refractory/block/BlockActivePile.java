package com.codetaylor.mc.pyrotech.modules.tech.refractory.block;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileActivePile;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockActivePile
    extends Block {

  public static final String NAME = "active_pile";

  public BlockActivePile() {

    super(Material.ROCK);
    this.setHardness(2);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
    this.lightValue = 7;
  }

  @SuppressWarnings("deprecation")
  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(
      IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos
  ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileActivePile) {
      ((TileActivePile) tileEntity).setNeedStructureValidation();
    }
  }

  @Override
  public void randomDisplayTick(
      @Nonnull IBlockState state, World world, BlockPos pos, Random rand
  ) {

    double centerX = pos.getX() + 0.5F;
    double centerY = pos.getY() + 2F;
    double centerZ = pos.getZ() + 0.5F;
    world.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.1D,
        0.0D
    );
    world.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.15D,
        0.0D
    );
    world.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY - 1,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.1D,
        0.0D
    );
    world.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY - 1,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.15D,
        0.0D
    );
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileActivePile) {
      ItemStackHandler stackHandler = ((TileActivePile) tileEntity).getOutput();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        StackHelper.spawnStackOnTop(world, stackHandler.getStackInSlot(i), pos, 0);
      }
    }

    super.breakBlock(world, pos, state);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean canSilkHarvest(
      World world, BlockPos pos, IBlockState state, EntityPlayer player
  ) {

    return false;
  }

  @ParametersAreNonnullByDefault
  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    return 0;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return true;
  }

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileActivePile();
  }
}
