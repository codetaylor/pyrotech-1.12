package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public final class BlockStoneBricksSlab {

  public static class Double
      extends BlockSlab.Double {

    public static final String NAME = "stone_bricks_slab_double";

    public Double(IBlockState blockState) {

      super(blockState);
      this.setSoundType(SoundType.STONE);
      this.setHardness(1.5f);
      this.setResistance(10);
      this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    protected Item getItem() {

      return Item.getItemFromBlock(ModuleCore.Blocks.STONE_BRICKS_SLAB);
    }
  }

  public static class Half
      extends BlockSlab.Half {

    public static final String NAME = "stone_bricks_slab";

    public Half(IBlockState blockState) {

      super(blockState);
      this.setSoundType(SoundType.STONE);
      this.setHardness(1.5f);
      this.setResistance(5);
      this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    protected Item getItem() {

      return Item.getItemFromBlock(ModuleCore.Blocks.STONE_BRICKS_SLAB);
    }
  }

  private BlockStoneBricksSlab() {
    //
  }
}
