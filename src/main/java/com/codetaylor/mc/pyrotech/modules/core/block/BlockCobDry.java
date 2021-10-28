package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockCobDry
    extends Block {

  public static final String NAME = "cob_dry";

  public BlockCobDry() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 1);
    this.setHardness(1.2f);
    this.setResistance(10);
    this.setTickRandomly(true);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (!world.isRemote) {

      if (!world.isAreaLoaded(pos, 3)) {
        return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
      }

      BlockPos up = pos.up();

      if (world.isRainingAt(up)) {
        world.setBlockState(pos, ModuleCore.Blocks.COB_WET.getDefaultState());
      }

      if (world.getBlockState(up).getBlock() == ModuleCore.Blocks.COB_WET) {
        world.setBlockState(pos, ModuleCore.Blocks.COB_WET.getDefaultState());
      }

    }
  }
}
