package com.codetaylor.mc.pyrotech.modules.tech.refractory.block;

import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.FluidUtilFix;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentFire;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableAdjacentIgniterBlock;
import com.codetaylor.mc.pyrotech.library.util.Tooltips;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.ModuleTechRefractoryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileBrickTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.TileStoneTarCollector;
import com.codetaylor.mc.pyrotech.modules.tech.refractory.tile.spi.TileTarCollectorBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class BlockTarCollector
    extends Block
    implements IBlockVariant<BlockTarCollector.EnumType>,
    IBlockIgnitableAdjacentFire,
    IBlockIgnitableAdjacentIgniterBlock {

  public static final String NAME = "tar_collector";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  public BlockTarCollector() {

    super(Material.ROCK);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(2);
    this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.STONE));
  }

  @Nonnull
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {

    return (face == EnumFacing.UP) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTarCollectorBase) {
      TileTarCollectorBase tile = (TileTarCollectorBase) tileEntity;
      FluidTank fluidTank = tile.getFluidTank();
      FluidStack fluid = fluidTank.getFluid();
      int fluidAmount = fluidTank.getFluidAmount();

      if (fluid != null && fluidAmount > 0) {
        int luminosity = fluid.getFluid().getLuminosity(fluid);
        return MathHelper.clamp(luminosity, 0, 15);
      }
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public void addInformation(ItemStack itemStack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {

    if (itemStack.getMetadata() == BlockTarCollector.EnumType.STONE.getMeta()) {
      Tooltips.addCapacity(tooltip, ModuleTechRefractoryConfig.STONE_TAR_COLLECTOR.CAPACITY, 1);
      Tooltips.addHotFluids(tooltip, ModuleTechRefractoryConfig.STONE_TAR_COLLECTOR.HOLDS_HOT_FLUIDS, 2);

    } else if (itemStack.getMetadata() == BlockTarCollector.EnumType.BRICK.getMeta()) {
      Tooltips.addCapacity(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.CAPACITY, 1);
      Tooltips.addHotFluids(tooltip, ModuleTechRefractoryConfig.BRICK_TAR_COLLECTOR.HOLDS_HOT_FLUIDS, 2);
    }
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
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

  @Nonnull
  @Override
  public String getModelName(ItemStack stack) {

    return EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.STONE) {
      return new TileStoneTarCollector();

    } else if (type == EnumType.BRICK) {
      return new TileBrickTarCollector();
    }

    return null;
  }

  @Override
  public void igniteWithAdjacentFire(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    if (facing == EnumFacing.UP) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileTarCollectorBase) {
        FluidTank fluidTank = ((TileTarCollectorBase) tileEntity).getFluidTank();

        if (fluidTank.getFluidAmount() > 0) {
          ((TileTarCollectorBase) tileEntity).setBurning(true);
        }
      }
    }
  }

  @Override
  public void igniteWithAdjacentIgniterBlock(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    if (Util.canSetFire(world, pos.up())) {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileTarCollectorBase) {

        if (((TileTarCollectorBase) tileEntity).isFlammable()) {
          ((TileTarCollectorBase) tileEntity).setBurning(true);
        }
      }
    }
  }

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

  @Override
  public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTarCollectorBase) {
      return side == EnumFacing.UP
          && ((TileTarCollectorBase) tileEntity).isBurning();
    }

    return super.isFireSource(world, pos, side);
  }

  public enum EnumType
      implements IVariant {

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
