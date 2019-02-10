package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.FacingHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.library.spi.block.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompactingBin;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalCompactingBinWorker;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.stream.Stream;

public class BlockMechanicalCompactingBin
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "mechanical_compacting_bin";

  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

  public BlockMechanicalCompactingBin() {

    super(Material.ROCK);
    this.setHardness(4f);
    this.setResistance(0.5F);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.NORTH)
        .withProperty(TYPE, EnumType.BIN));
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
  @Nullable
  @Override
  public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {

    return this.interactionRayTrace(super.collisionRayTrace(blockState, world, pos, start, end), blockState, world, pos, start, end);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (state.getValue(TYPE) == EnumType.BIN) {

      if (!world.isRemote) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileCompactingBin) {
          ItemStackHandler handler = ((TileCompactingBin) tileEntity).getInputStackHandler();
          StackHelper.spawnStackHandlerSlotContentsOnTop(world, handler, 0, pos);
        }

        EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
        EnumFacing rotatedFacing = FacingHelper.rotateFacingCW(facing);
        BlockPos offset = pos.offset(rotatedFacing);
        IBlockState blockState = world.getBlockState(offset);

        if (blockState.getBlock() == this) {
          world.destroyBlock(offset, false);
        }
      }

    } else {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileMechanicalCompactingBinWorker) {
        TileMechanicalCompactingBinWorker tile = (TileMechanicalCompactingBinWorker) tileEntity;
        StackHelper.spawnStackHandlerSlotContentsOnTop(world, tile.getOutputStackHandler(), 0, pos);
        StackHelper.spawnStackHandlerSlotContentsOnTop(world, tile.getCogStackHandler(), 0, pos);
      }

      EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
      BlockPos offset = pos.offset(facing);
      IBlockState blockState = world.getBlockState(offset);

      if (blockState.getBlock() == this) {
        world.destroyBlock(offset, false);
      }
    }

    super.breakBlock(world, pos, state);
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
    return this.getDefaultState()
        .withProperty(TYPE, EnumType.BIN)
        .withProperty(Properties.FACING_HORIZONTAL, opposite);
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    if (state.getValue(TYPE) == EnumType.MACHINE) {
      return new TileMechanicalCompactingBinWorker();
    }

    return new TileCompactingBin();
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

    return (side == EnumFacing.DOWN);
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, TYPE);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    // 0-3 type 0
    // 4-7 type 1

    return this.getDefaultState()
        .withProperty(TYPE, EnumType.fromMeta((meta >> 2) & 1))
        .withProperty(Properties.FACING_HORIZONTAL, EnumFacing.HORIZONTALS[meta & 3]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(TYPE).getMeta() << 2) | state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex();
  }

  public enum EnumType
      implements IVariant {

    BIN(0, "bin"),
    MACHINE(1, "machine");

    private static final EnumType[] META_LOOKUP = Stream.of(values())
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

  // ---------------------------------------------------------------------------
  // - Item
  // ---------------------------------------------------------------------------

  public static class Item
      extends ItemBlock {

    public Item(BlockMechanicalCompactingBin block) {

      super(block);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {

      IBlockState blockState = world.getBlockState(pos);
      Block block = blockState.getBlock();

      if (!block.isReplaceable(world, pos)) {
        pos = pos.offset(facing);
      }

      ItemStack itemStack = player.getHeldItem(hand);

      if (!itemStack.isEmpty()
          && player.canPlayerEdit(pos, facing, itemStack)
          && world.mayPlace(this.block, pos, false, facing, null)) {

        int i = this.getMetadata(itemStack.getMetadata());
        IBlockState stateForPlacement = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);

        // Get facing of placement state
        EnumFacing placementFacing = stateForPlacement.getValue(Properties.FACING_HORIZONTAL);
        EnumFacing rotatedFacing = FacingHelper.rotateFacingCW(placementFacing);
        BlockPos secondPos = pos.offset(rotatedFacing);

        // Do checks on the second placement
        if (player.canPlayerEdit(secondPos, rotatedFacing, itemStack)
            && world.mayPlace(this.block, secondPos, false, rotatedFacing, null)) {

          IBlockState secondState = this.block.getDefaultState()
              .withProperty(BlockMechanicalCompactingBin.TYPE, EnumType.MACHINE)
              .withProperty(Properties.FACING_HORIZONTAL, rotatedFacing.getOpposite());

          if (this.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, stateForPlacement)
              && this.placeSecondBlockAt(itemStack, player, world, secondPos, secondState)) {

            stateForPlacement = world.getBlockState(pos);
            SoundType soundtype = stateForPlacement.getBlock().getSoundType(stateForPlacement, world, pos, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            itemStack.shrink(1);
          }

          return EnumActionResult.SUCCESS;
        }
      }

      return EnumActionResult.FAIL;
    }

    public boolean placeSecondBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, IBlockState newState) {

      if (!world.setBlockState(pos, newState, 1 | 2 | 8)) {
        return false;
      }

      IBlockState state = world.getBlockState(pos);

      if (state.getBlock() == this.block) {
        ItemBlock.setTileEntityNBT(world, player, pos, stack);
        this.block.onBlockPlacedBy(world, pos, state, player, stack);
      }

      return true;
    }
  }
}
