package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileActivePile;
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

  @Override
  public void neighborChanged(
      IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos
  ) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileActivePile) {
      ((TileActivePile) tileEntity).setNeedStructureValidation();
    }
  }

  @Override
  public void randomDisplayTick(
      IBlockState stateIn, World worldIn, BlockPos pos, Random rand
  ) {

    double centerX = pos.getX() + 0.5F;
    double centerY = pos.getY() + 2F;
    double centerZ = pos.getZ() + 0.5F;
    worldIn.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.1D,
        0.0D
    );
    worldIn.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.15D,
        0.0D
    );
    worldIn.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY - 1,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.1D,
        0.0D
    );
    worldIn.spawnParticle(
        EnumParticleTypes.SMOKE_NORMAL,
        centerX + (rand.nextDouble() - 0.5),
        centerY - 1,
        centerZ + (rand.nextDouble() - 0.5),
        0.0D,
        0.15D,
        0.0D
    );
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TileActivePile) {
      ItemStackHandler stackHandler = ((TileActivePile) tileEntity).getOutput();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        StackHelper.spawnStackOnTop(worldIn, stackHandler.getStackInSlot(i), pos, 0);
      }
    }

    super.breakBlock(worldIn, pos, state);
  }

  @Override
  public boolean canSilkHarvest(
      World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player
  ) {

    return false;
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    return 0;
  }

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    return true;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileActivePile();
  }
}
