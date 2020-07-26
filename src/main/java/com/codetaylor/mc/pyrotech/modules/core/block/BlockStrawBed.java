package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.athenaeum.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider.WorldSleepResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockStrawBed
    extends BlockPartialBase {

  public static final String NAME = "straw_bed";

  public static final PropertyEnum<BlockStrawBed.EnumType> TYPE = PropertyEnum.create("type", BlockStrawBed.EnumType.class);
  public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

  private static final AxisAlignedBB AABB = AABBHelper.create(0, 0, 0, 16, 4, 16);

  public BlockStrawBed() {

    super(Material.CLOTH);
    this.setSoundType(SoundType.PLANT);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {

    return MapColor.SAND;
  }

  @Nullable
  private EntityPlayer getPlayerInBed(World world, BlockPos pos) {

    for (EntityPlayer entityplayer : world.playerEntities) {

      if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
        return entityplayer;
      }
    }

    return null;
  }

  private boolean isFoot(IBlockState state) {

    return (state.getBlock() == this) && (state.getValue(TYPE) == EnumType.FOOT);
  }

  private boolean isHead(IBlockState state) {

    return (state.getBlock() == this) && (state.getValue(TYPE) == EnumType.HEAD);
  }

  @SideOnly(Side.CLIENT)
  public boolean hasCustomBreakingProgress(@Nonnull IBlockState state) {

    return true;
  }

  @Nonnull
  @Override
  public EnumPushReaction getMobilityFlag(@Nonnull IBlockState state) {

    return EnumPushReaction.DESTROY;
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {

    return this.isFoot(state) || this.isHead(state) || super.isBed(state, world, pos, player);
  }

  @Override
  public boolean isBedFoot(IBlockAccess world, @Nonnull BlockPos pos) {

    return this.isFoot(world.getBlockState(pos));
  }

  // ---------------------------------------------------------------------------
  // - Drops
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public Item getItemDropped(IBlockState state, @Nonnull Random rand, int fortune) {

    return (state.getValue(TYPE) == EnumType.FOOT) ? Items.AIR : Item.getItemFromBlock(this);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {

    if (this.isHead(state)) {
      super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    if (player.capabilities.isCreativeMode
        && this.isFoot(state)) {
      BlockPos blockpos = pos.offset(state.getValue(Properties.FACING_HORIZONTAL));

      if (world.getBlockState(blockpos).getBlock() == this) {
        world.setBlockToAir(blockpos);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    if (this.isFoot(state)) {
      pos = pos.offset(state.getValue(Properties.FACING_HORIZONTAL));
      state = world.getBlockState(pos);

      if (state.getBlock() != this) {
        return true;
      }
    }

    WorldSleepResult sleepResult = world.provider.canSleepAt(player, pos);

    if (sleepResult == WorldSleepResult.DENY) {
      return true;

    } else if (sleepResult == WorldSleepResult.BED_EXPLODES) {
      this.explode(world, pos, state);
      return true;
    }

    if (state.getValue(OCCUPIED)) {
      EntityPlayer entityplayer = this.getPlayerInBed(world, pos);

      if (entityplayer != null) {
        TextComponentTranslation message = new TextComponentTranslation("tile.bed.occupied");
        player.sendStatusMessage(message, true);
        return true;
      }

      state = state.withProperty(OCCUPIED, false);
      world.setBlockState(pos, state, 4);
    }

    switch (player.trySleep(pos)) {

      case OK:
        state = state.withProperty(OCCUPIED, true);
        world.setBlockState(pos, state, 4);
        break;

      case NOT_POSSIBLE_NOW: {
        TextComponentTranslation message = new TextComponentTranslation("tile.bed.noSleep");
        player.sendStatusMessage(message, true);
        break;
      }

      case NOT_SAFE: {
        TextComponentTranslation message = new TextComponentTranslation("tile.bed.notSafe");
        player.sendStatusMessage(message, true);
        break;
      }

      case TOO_FAR_AWAY: {
        TextComponentTranslation message = new TextComponentTranslation("tile.bed.tooFarAway");
        player.sendStatusMessage(message, true);
        break;
      }
    }

    return true;
  }

  private void explode(World world, BlockPos pos, IBlockState state) {

    world.setBlockToAir(pos);
    BlockPos blockpos = pos.offset(state.getValue(Properties.FACING_HORIZONTAL).getOpposite());

    if (world.getBlockState(blockpos).getBlock() == this) {
      world.setBlockToAir(blockpos);
    }

    world.newExplosion(null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 5.0f, true, true);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    EnumFacing enumfacing = state.getValue(Properties.FACING_HORIZONTAL);

    if (this.isFoot(state)) {
      BlockPos posOffset = pos.offset(enumfacing);

      if (world.getBlockState(posOffset).getBlock() != this) {
        world.setBlockToAir(pos);
      }

    } else {
      BlockPos posOffset = pos.offset(enumfacing.getOpposite());

      if (world.getBlockState(posOffset).getBlock() != this) {

        if (!world.isRemote) {
          this.dropBlockAsItem(world, pos, state, 0);
        }

        world.setBlockToAir(pos);
      }
    }
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, Properties.FACING_HORIZONTAL, TYPE, OCCUPIED);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    EnumFacing enumfacing = EnumFacing.getHorizontal(meta);

    if ((meta & 8) > 0) {
      return this.getDefaultState()
          .withProperty(TYPE, EnumType.HEAD)
          .withProperty(Properties.FACING_HORIZONTAL, enumfacing)
          .withProperty(OCCUPIED, (meta & 4) > 0);

    } else {
      return this.getDefaultState()
          .withProperty(TYPE, EnumType.FOOT)
          .withProperty(Properties.FACING_HORIZONTAL, enumfacing);
    }
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (this.isFoot(state)) {
      IBlockState iblockstate = world.getBlockState(pos.offset(state.getValue(Properties.FACING_HORIZONTAL)));

      if (iblockstate.getBlock() == this) {
        state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
      }
    }

    return state;
  }

  @Nonnull
  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
    EnumFacing rotatedFacing = rot.rotate(facing);
    return state.withProperty(Properties.FACING_HORIZONTAL, rotatedFacing);
  }

  @Nonnull
  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror) {

    EnumFacing facing = state.getValue(Properties.FACING_HORIZONTAL);
    Rotation rotation = mirror.toRotation(facing);
    return state.withRotation(rotation);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    int i = 0;
    i = i | state.getValue(Properties.FACING_HORIZONTAL).getHorizontalIndex();

    if (this.isHead(state)) {
      i |= 8;

      if (state.getValue(OCCUPIED)) {
        i |= 4;
      }
    }

    return i;
  }

  public enum EnumType
      implements IStringSerializable {

    HEAD("head"),
    FOOT("foot");

    private final String name;

    EnumType(String name) {

      this.name = name;
    }

    public String toString() {

      return this.name;
    }

    @Nonnull
    public String getName() {

      return this.name;
    }
  }
}
