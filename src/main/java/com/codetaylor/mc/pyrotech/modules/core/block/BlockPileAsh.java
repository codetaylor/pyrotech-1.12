package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.library.spi.block.BlockPileBase;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPileAsh
    extends BlockPileBase {

  public static final String NAME = "pile_ash";

  public BlockPileAsh() {

    super(Material.SAND);
    this.setHardness(0.1f);
    this.setResistance(0);
    this.setHarvestLevel("shovel", 0);
    this.setSoundType(SoundType.SAND);
  }

  @Override
  protected ItemStack getDrop(World world, BlockPos pos, IBlockState state) {

    return ItemMaterial.EnumType.PIT_ASH.asStack();
  }
}
