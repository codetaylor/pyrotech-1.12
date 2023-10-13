package com.codetaylor.mc.pyrotech.modules.storage.block.spi;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.storage.block.item.ItemBlockTank;
import com.codetaylor.mc.pyrotech.modules.storage.tile.spi.TileTankBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public abstract class BlockTankBase
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final IProperty<EnumConnection> CONNECTION = PropertyEnum.create("connection", EnumConnection.class);

  public BlockTankBase() {

    super(Material.ROCK);
    this.setHarvestLevel("pickaxe", 0);
    this.setHardness(2);
  }

  // ---------------------------------------------------------------------------
  // - Multiblock
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    this.updateConnectionStateForNeighborChanged(world, pos);
    this.updateTankGroups(world, pos);
    this.settleFluids(world, pos);
  }

  @ParametersAreNonnullByDefault
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, EnumFacing side) {

    // Null check the block entity tag to retain backwards compatibility.
    // If the item stack has the block entity tag, then it will have been
    // deserialized properly before this method is called and reading the
    // tank in the code below will result in the tank data being reset.

    if (stack.hasTagCompound()
        && stack.getSubCompound(StackHelper.BLOCK_ENTITY_TAG) == null) {

      NBTTagCompound tankTag = ItemBlockTank.getTankTag(stack);

      if (tankTag != null) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileTankBase) {
          ((TileTankBase) tileEntity).readFromItem(stack);
        }
      }
    }

    this.updateConnectionStateForPlacement(world, pos, side);
  }

  private void updateConnectionStateForPlacement(World world, BlockPos pos, EnumFacing side) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tileTankBase = (TileTankBase) tileEntity;
      tileTankBase.updateConnectionsForPlacement(side);
      BlockHelper.notifyBlockUpdate(world, pos);
    }
  }

  private void updateConnectionStateForNeighborChanged(World world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tileTankBase = (TileTankBase) tileEntity;
      tileTankBase.updateConnectionsForNeighborChanged();
      BlockHelper.notifyBlockUpdate(world, pos);
    }
  }

  private void updateTankGroups(World world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tileTankBase = (TileTankBase) tileEntity;
      tileTankBase.updateTankGroups();
    }
  }

  private void settleFluids(World world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tileTankBase = (TileTankBase) tileEntity;
      tileTankBase.settleFluids();
    }
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tile = (TileTankBase) tileEntity;
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

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {

    RayTraceResult result = super.collisionRayTrace(blockState, world, pos, start, end);
    return this.interactionRayTrace(result, blockState, world, pos, start, end);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(
      World world,
      BlockPos pos,
      IBlockState blockState,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, blockState, player, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean removedByPlayer(IBlockState blockState, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(blockState, world, pos, player, false);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState blockState, @Nullable TileEntity tileEntity, ItemStack itemStack) {

    super.harvestBlock(world, player, pos, blockState, tileEntity, itemStack);

    if (!world.isRemote) {
      world.setBlockToAir(pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState blockState, int fortune) {

    // Serialize the TE into the item dropped.
    // Called before #breakBlock

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {

      if (this.canHoldContentsWhenBroken()) {
        ItemStack itemStack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
        ((TileTankBase) tileEntity).writeToItem(itemStack);
        drops.add(itemStack);

      } else {
        super.getDrops(drops, world, pos, blockState, fortune);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState blockState) {

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.SOLID;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

    return super.canRenderInLayer(state, layer);
  }

  // ---------------------------------------------------------------------------
  // - Tooltip
  // ---------------------------------------------------------------------------

  @Override
  public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag) {

    NBTTagCompound tankTag = ItemBlockTank.getTankTag(stack);

    if (tankTag == null) {
      this.addInformationCapacity(tooltip);

    } else {

      if (tankTag.hasKey("Empty")
          && (!tankTag.hasKey("Amount")
          || tankTag.getInteger("Amount") <= 0)) {

        this.addInformationCapacity(tooltip);

      } else {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tankTag);

        if (fluidStack != null) {
          String localizedName = fluidStack.getLocalizedName();
          int amount = fluidStack.amount;
          int capacity = this.getCapacity();
          tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid", localizedName, amount, capacity));
        }
      }
    }

    boolean hotFluids = this.canHoldHotFluids();
    tooltip.add((hotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids." + hotFluids));

    boolean holdsContents = this.canHoldContentsWhenBroken();
    tooltip.add((holdsContents ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.contents.retain." + holdsContents));
  }

  public abstract int getCapacity();

  protected abstract boolean canHoldHotFluids();

  protected abstract boolean canHoldContentsWhenBroken();

  private void addInformationCapacity(@Nonnull List<String> tooltip) {

    int capacity = this.getCapacity();
    tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.fluid.capacity", capacity));
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      state = state.withProperty(CONNECTION, ((TileTankBase) tileEntity).getConnectionState());
    }

    return super.getActualState(state, world, pos);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, CONNECTION);
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState blockState) {

    return 0;
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState();
  }

  public enum EnumConnection
      implements IVariant {

    NONE(0, "none"),
    UP(1, "up"),
    DOWN(2, "down"),
    BOTH(3, "both");

    private static final EnumConnection[] META_LOOKUP = Stream.of(EnumConnection.values())
        .sorted(Comparator.comparing(EnumConnection::getMeta))
        .toArray(EnumConnection[]::new);

    private final int meta;
    private final String name;

    EnumConnection(int meta, String name) {

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

    public static EnumConnection fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }
}
