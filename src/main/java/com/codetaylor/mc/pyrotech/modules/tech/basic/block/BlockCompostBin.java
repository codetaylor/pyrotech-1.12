package com.codetaylor.mc.pyrotech.modules.tech.basic.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompostBin;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockCompostBin
    extends BlockPartialBase
    implements IBlockInteractable {

  public static final String NAME = "compost_bin";

  public static final AxisAlignedBB AABB = AABBHelper.create(1, 0, 1, 15, 16, 15);

  public static final int PROPERTY_STATE_DRY = 0;
  public static final int PROPERTY_STATE_WET = 1;
  public static final int PROPERTY_STATE_READY = 2;
  public static final PropertyInteger PROPERTY_STATE = PropertyInteger.create("state", 0, 2);
  public static final PropertyInteger PROPERTY_COMPOST_VALUE = PropertyInteger.create("compost_value", 0, 5);

  // TODO: remove this and the debug messages after proven stable
  public static final boolean DEBUG = false;

  public BlockCompostBin() {

    super(Material.WOOD);
    this.setHardness(2f);
    this.setResistance(0.5F);
    this.setDefaultState(this
        .getBlockState()
        .getBaseState()
        .withProperty(PROPERTY_STATE, PROPERTY_STATE_DRY)
        .withProperty(PROPERTY_COMPOST_VALUE, 0)
    );
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
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

    if (BlockCompostBin.DEBUG
        && !world.isRemote && hand == EnumHand.MAIN_HAND
        && playerIn.getHeldItemMainhand().isEmpty()) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileCompostBin) {
        TileCompostBin compostBin = (TileCompostBin) tileEntity;

        System.out.println("Moisture: " + compostBin.getMoistureLevel());
        System.out.println("Input Compost Value: " + compostBin.getInputCompostValue());
        System.out.println("Stored Compost Value: " + compostBin.getStoredCompostValue());
        System.out.println("Output Compost Value: " + compostBin.getOutputCompostValue());
        System.out.println("Total Compost Value: " + compostBin.getTotalCompostValue());

        System.out.println("Output Items:");
        for (int i = 0; i < compostBin.getOutputStackHandler().getSlots(); i++) {
          System.out.println("  " + i);
          System.out.println("    Progress: " + compostBin.getLayerProgress(i));
          System.out.println("    Output: " + compostBin.getLayerOutputItemStack(i));
        }
      }
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof TileCompostBin) {
        TileCompostBin tileCompostBin = (TileCompostBin) tileEntity;
        ItemStackHandler inputStackHandler = tileCompostBin.getInputStackHandler();
        ItemStackHandler outputStackHandler = tileCompostBin.getOutputStackHandler();
        StackHelper.spawnStackHandlerSlotContentsOnTop(world, inputStackHandler, 0, pos);
        StackHelper.spawnStackHandlerSlotContentsOnTop(world, outputStackHandler, 0, pos);

        for (int i = 0; i < tileCompostBin.getStoredCompostValue(); i++) {
          StackHelper.spawnStackOnTop(world, BlockRock.EnumType.DIRT.asStack(), pos);
        }
      }
    }

    super.breakBlock(world, pos, state);
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

    return new TileCompostBin();
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

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, PROPERTY_STATE, PROPERTY_COMPOST_VALUE);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return 0;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState();
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileCompostBin) {
      TileCompostBin tileCompostBin = (TileCompostBin) tileEntity;
      float moistureLevel = tileCompostBin.getMoistureLevel();

      if (moistureLevel == 0) {
        state = state.withProperty(PROPERTY_STATE, PROPERTY_STATE_DRY);

      } else if (!tileCompostBin.getOutputStackHandler().getFirstNonEmptyItemStack().isEmpty()) {
        state = state.withProperty(PROPERTY_STATE, PROPERTY_STATE_READY);

      } else {
        state = state.withProperty(PROPERTY_STATE, PROPERTY_STATE_WET);
      }

      int totalCompostValue = tileCompostBin.getTotalCompostValue();
      int maxCompostValueCapacity = tileCompostBin.getMaxCompostValueCapacity();
      float percentFull = (totalCompostValue / (float) maxCompostValueCapacity);
      float usedCapacity = Math.max(0, Math.min(1, percentFull)) * 100;

      if (usedCapacity == 0) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 0);

      } else if (usedCapacity > 0 && usedCapacity < 25) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 1);

      } else if (usedCapacity >= 25 && usedCapacity < 50) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 2);

      } else if (usedCapacity >= 50 && usedCapacity < 75) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 3);

      } else if (usedCapacity >= 75 && usedCapacity < 100) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 4);

      } else if (usedCapacity == 100) {
        state = state.withProperty(PROPERTY_COMPOST_VALUE, 5);
      }
    }

    return state;
  }
}
