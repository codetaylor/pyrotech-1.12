package com.codetaylor.mc.pyrotech.modules.tech.refractory.block;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TilePitAsh;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPitAsh
    extends Block {

  public static final String NAME = "pit_ash_block";

  public BlockPitAsh() {

    super(Material.GROUND);
    this.setHardness(0.6F);
    this.setHarvestLevel("shovel", 0);
    this.setSoundType(SoundType.SAND);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TilePitAsh();
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {

    return 0;
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    return false;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof TilePitAsh) {
      ItemStackHandler stackHandler = ((TilePitAsh) tileEntity).getStackHandler();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        StackHelper.spawnStackOnTop(worldIn, stackHandler.getStackInSlot(i), pos, 0);
      }
    }

    super.breakBlock(worldIn, pos, state);
  }
}
