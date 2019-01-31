package com.codetaylor.mc.pyrotech.modules.ignition.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentIgniterBlock;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.util.RefractoryIgnitionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public class BlockIgniter
    extends Block
    implements IBlockVariant<BlockIgniter.EnumType> {

  public static final String NAME = "igniter";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  public BlockIgniter() {

    super(Material.ROCK);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(2);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(VARIANT, EnumType.STONE)
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.SOUTH));
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT, Properties.FACING_HORIZONTAL);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(VARIANT, EnumType.fromMeta(meta & 0x3))
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.HORIZONTALS[(meta >> 2) & 0x3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta() | (state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex() << 2);
  }

  @Override
  public int damageDropped(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public void getSubBlocks(
      CreativeTabs tab,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : EnumType.values()) {
      list.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Override
  public void neighborChanged(
      IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos
  ) {

    if (world.isRemote) {
      return;
    }

    EnumFacing selfFacing = state.getValue(Properties.FACING_HORIZONTAL);
    EnumFacing selfFacingOpposite = selfFacing.getOpposite();
    boolean blockPowered = world.isSidePowered(pos.offset(selfFacing), selfFacingOpposite);

    if (blockPowered) {
      BlockPos offset = pos.offset(selfFacingOpposite);
      IBlockState facingBlockState = world.getBlockState(offset);
      Block facingBlock = facingBlockState.getBlock();

      if (Util.canSetFire(world, offset)) {
        world.setBlockState(offset, Blocks.FIRE.getDefaultState(), 3);

      } else if (facingBlock instanceof IBlockIgnitableAdjacentIgniterBlock) {
        ((IBlockIgnitableAdjacentIgniterBlock) facingBlock).igniteWithAdjacentIgniterBlock(world, offset, facingBlockState, selfFacing);

      } else {
        RefractoryIgnitionHelper.igniteBlocks(world, offset);
      }
    }
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(
      World world,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer,
      EnumHand hand
  ) {

    EnumFacing opposite = placer.getHorizontalFacing().getOpposite();
    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta)).withProperty(Properties.FACING_HORIZONTAL, opposite);
  }

  @Nonnull
  @Override
  public String getModelName(ItemStack itemStack) {

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  @Nonnull
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  public enum EnumType
      implements IVariant {

    // Total of 4 due to meta bit constraints from facing

    STONE(0, "stone"),
    BRICK(1, "brick");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final int meta;
    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}
