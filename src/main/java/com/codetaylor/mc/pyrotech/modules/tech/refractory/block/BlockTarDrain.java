package com.codetaylor.mc.pyrotech.modules.tech.refractory.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.FluidUtilFix;
import com.codetaylor.mc.pyrotech.library.util.Tooltips;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileBrickTarDrain;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileStoneTarDrain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class BlockTarDrain
    extends Block
    implements IBlockVariant<BlockTarDrain.EnumType> {

  public static final String NAME = "tar_drain";

  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  public BlockTarDrain() {

    super(Material.ROCK);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(2);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(VARIANT, EnumType.STONE)
        .withProperty(FACING, EnumFacing.SOUTH));
  }

  @ParametersAreNonnullByDefault
  @Override
  public void addInformation(ItemStack itemStack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {

    if (itemStack.getMetadata() == BlockTarCollector.EnumType.STONE.getMeta()) {
      Tooltips.addCapacity(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.CAPACITY, 1);
      Tooltips.addHotFluids(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.HOLDS_HOT_FLUIDS, 2);
      Tooltips.addRange(tooltip, ModuleTechRefractoryConfig.STONE_TAR_DRAIN.RANGE, 3);

    } else if (itemStack.getMetadata() == BlockTarCollector.EnumType.BRICK.getMeta()) {
      Tooltips.addCapacity(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.CAPACITY, 1);
      Tooltips.addHotFluids(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.HOLDS_HOT_FLUIDS, 2);
      Tooltips.addRange(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_DRAIN.RANGE, 3);
    }
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT, FACING);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(VARIANT, EnumType.fromMeta(meta & 0x3))
        .withProperty(FACING, EnumFacing.HORIZONTALS[(meta >> 2) & 0x3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta() | (state.getValue(FACING).getHorizontalIndex() << 2);
  }

  @Override
  public int damageDropped(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getSubBlocks(
      CreativeTabs tab,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : EnumType.values()) {
      list.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @ParametersAreNonnullByDefault
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
    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta)).withProperty(FACING, opposite);
  }

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.STONE) {
      return new TileStoneTarDrain();

    } else if (type == EnumType.BRICK) {
      return new TileBrickTarDrain();
    }

    return null;
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

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(
      World world,
      BlockPos pos,
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity == null) {
      return false;
    }

    IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);

    if (fluidHandler == null) {
      return false;
    }

    ItemStack heldItem = player.getHeldItem(hand);

    return FluidUtilFix.interactWithFluidHandler(player, hand, fluidHandler)
        || FluidUtil.getFluidHandler(heldItem) != null;
  }

  @Nonnull
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
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

    @Nonnull
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
