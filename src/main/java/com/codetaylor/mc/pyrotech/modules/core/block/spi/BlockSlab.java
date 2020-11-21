package com.codetaylor.mc.pyrotech.modules.core.block.spi;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class BlockSlab
    extends net.minecraft.block.BlockSlab {

  public static final PropertyEnum<BlockSlab.Variant> VARIANT = PropertyEnum.create("variant", BlockSlab.Variant.class);

  public BlockSlab(IBlockState blockState) {

    super(blockState.getMaterial());

    IBlockState iblockstate = this.blockState.getBaseState();

    if (!this.isDouble()) {
      iblockstate = iblockstate.withProperty(HALF, net.minecraft.block.BlockSlab.EnumBlockHalf.BOTTOM);
    }

    this.setDefaultState(iblockstate.withProperty(VARIANT, BlockSlab.Variant.DEFAULT));
  }

  @Nonnull
  @Override
  public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {

    return this.getItem();
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public ItemStack getItem(World world, BlockPos pos, IBlockState state) {

    return new ItemStack(this.getItem());
  }

  protected abstract Item getItem();

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockSlab.Variant.DEFAULT);

    if (!this.isDouble()) {
      iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? net.minecraft.block.BlockSlab.EnumBlockHalf.BOTTOM : net.minecraft.block.BlockSlab.EnumBlockHalf.TOP);
    }

    return iblockstate;
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {

    int i = 0;

    if (!this.isDouble() && state.getValue(HALF) == net.minecraft.block.BlockSlab.EnumBlockHalf.TOP) {
      i |= 8;
    }

    return i;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(int meta) {

    return super.getUnlocalizedName();
  }

  @Nonnull
  @Override
  public IProperty<?> getVariantProperty() {

    return VARIANT;
  }

  @Nonnull
  @Override
  public Comparable<?> getTypeForItem(ItemStack stack) {

    return Variant.DEFAULT;
  }

  public static abstract class Double
      extends BlockSlab {

    public Double(IBlockState blockState) {

      super(blockState);
    }

    @Override
    public boolean isDouble() {

      return true;
    }
  }

  public static abstract class Half
      extends BlockSlab {

    public Half(IBlockState blockState) {

      super(blockState);
    }

    @Override
    public boolean isDouble() {

      return false;
    }
  }

  public enum Variant
      implements IStringSerializable {
    DEFAULT;

    public String getName() {

      return "default";
    }
  }
}
