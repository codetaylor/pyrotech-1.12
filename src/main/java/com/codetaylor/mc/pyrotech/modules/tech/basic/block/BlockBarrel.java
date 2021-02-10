package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

public class BlockBarrel
    extends Block {

  public static final String NAME = "barrel";

  public static final IProperty<Boolean> SEALED = PropertyBool.create("sealed");

  public BlockBarrel() {

    super(Material.WOOD);
    this.setHardness(1);
    this.setDefaultState(this.blockState.getBaseState().withProperty(SEALED, false));
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, SEALED);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return (meta == 1)
        ? this.getDefaultState().withProperty(SEALED, true)
        : this.getDefaultState().withProperty(SEALED, false);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(SEALED) ? 1 : 0;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return state.getValue(SEALED) ? 1 : 0;
  }

}
