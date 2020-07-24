package com.codetaylor.mc.pyrotech.modules.storage.block.spi;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
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

    this.updateConnectionState(world, pos);
    this.updateTankGroups(world, pos);
    this.settleFluids(world, pos);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

    this.updateConnectionState(world, pos);
  }

  private void updateConnectionState(World world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {
      TileTankBase tileTankBase = (TileTankBase) tileEntity;
      tileTankBase.updateConnectionState();
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
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

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

  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    RayTraceResult result = super.collisionRayTrace(blockState, world, pos, start, end);
    return this.interactionRayTrace(result, blockState, world, pos, start, end);
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

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @Override
  public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote) {
      world.setBlockToAir(pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {

    // Serialize the TE into the item dropped.
    // Called before #breakBlock

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileTankBase) {

      if (this.canHoldContentsWhenBroken()) {
        drops.add(StackHelper.createItemStackFromTileEntity(
            this,
            1,
            0,
            tileEntity
        ));

      } else {
        super.getDrops(drops, world, pos, state, fortune);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  // ---------------------------------------------------------------------------
  // - Placement
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

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

    NBTTagCompound stackTag = stack.getTagCompound();

    if (stackTag == null) {
      this.addInformationCapacity(tooltip);

    } else {

      if (stackTag.hasKey(StackHelper.BLOCK_ENTITY_TAG)) {
        NBTTagCompound tileTag = stackTag.getCompoundTag(StackHelper.BLOCK_ENTITY_TAG);

        if (tileTag.hasKey("tank")) {
          NBTTagCompound tankTag = tileTag.getCompoundTag("tank");

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
      }
    }

    boolean hotFluids = this.canHoldHotFluids();
    tooltip.add((hotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids." + hotFluids));

    boolean holdsContents = this.canHoldContentsWhenBroken();
    tooltip.add((holdsContents ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.contents.retain." + holdsContents));
  }

  protected abstract int getCapacity();

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
  public int getMetaFromState(IBlockState state) {

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
