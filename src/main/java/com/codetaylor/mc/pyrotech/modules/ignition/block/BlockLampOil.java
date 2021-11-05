package com.codetaylor.mc.pyrotech.modules.ignition.block;

import com.codetaylor.mc.athenaeum.interaction.spi.IBlockInteractable;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.item.ItemIgniterBase;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileLampOil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockLampOil
    extends BlockPartialBase
    implements IBlockInteractable,
    IBlockIgnitableWithIgniterItem {

  public static final PropertyBool LIT = PropertyBool.create("lit");

  public static final String NAME = "lamp_oil";

  private static final AxisAlignedBB AABB = AABBHelper.create(3, 0, 3, 13, 16, 13);

  public BlockLampOil() {

    super(Material.ROCK);
    this.setHardness(1);
    this.setHarvestLevel("pickaxe", 0);

    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(LIT, false));
  }

  // ---------------------------------------------------------------------------
  // - Ignition
  // ---------------------------------------------------------------------------

  @Override
  public void igniteWithIgniterItem(World world, BlockPos pos, IBlockState blockState, EnumFacing facing) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileLampOil) {
      ((TileLampOil) tileEntity).setActive(true);
    }
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getValue(LIT)) {
      return this.getLightValue();
    }

    return 0;
  }

  protected int getLightValue() {

    return ModuleIgnitionConfig.OIL_LAMP.LIGHT_VALUE;
  }

  // ---------------------------------------------------------------------------
  // - Render
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  @ParametersAreNonnullByDefault
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (state.getValue(LIT)) {
      double d0 = (double) pos.getX() + 0.5;
      double d1 = (double) pos.getY() + 0.6;
      double d2 = (double) pos.getZ() + 0.5;

      world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Collision
  // ---------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
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
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    ItemStack heldItem = player.getHeldItemMainhand();

    if (heldItem.getItem() instanceof ItemIgniterBase) {
      return false;
    }

    return this.interact(IInteraction.EnumType.MouseClick, world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

    // Delay the destruction of the TE until after #getDrops is called. We need
    // access to the TE while creating the dropped item in order to serialize it.
    return willHarvest || super.removedByPlayer(state, world, pos, player, false);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {

    super.harvestBlock(world, player, pos, state, te, stack);

    if (!world.isRemote) {
      world.setBlockToAir(pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    // Serialize the TE into the item dropped.
    // Called before #breakBlock

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileLampOil) {

      drops.add(StackHelper.createItemStackFromTileEntity(
          this,
          1,
          0,
          tileEntity
      ));
    }
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(@Nonnull IBlockState state) {

    return true;
  }

  @ParametersAreNonnullByDefault
  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileLampOil();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, LIT);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(LIT) ? 1 : 0);
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(LIT, (meta == 1));
  }
}
