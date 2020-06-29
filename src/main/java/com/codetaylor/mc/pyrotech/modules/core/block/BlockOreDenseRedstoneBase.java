package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class BlockOreDenseRedstoneBase
    extends BlockPartialBase {

  private static final int TICK_RATE = 30;

  protected final boolean activated;

  public BlockOreDenseRedstoneBase(boolean activated) {

    super(Material.ROCK);
    this.activated = activated;
    this.setResistance(5.0F);
    this.setHardness(3);
    this.setHarvestLevel("pickaxe", 2);
    this.setSoundType(SoundType.STONE);
    this.setTickRandomly(this.activated);
  }

  public void activate(World world, BlockPos pos) {

    this.spawnParticles(world, pos, this.getParticleCount());

    if (!this.activated) {
      world.setBlockState(pos, this.getActiveState());
      this.playSound(world, pos);

    } else {

      if (world.rand.nextFloat() < 0.25) {
        this.playSound(world, pos);
      }
    }
  }

  public void playSound(World world, BlockPos pos) {

    if (!world.isRemote) {
      SoundEvent[] soundEvents = new SoundEvent[]{
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_00,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_01,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_02,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_03,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_04,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_05,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_06,
          ModuleCore.Sounds.DENSE_REDSTONE_ORE_ACTIVATE_07
      };

      world.playSound(
          null,
          pos,
          soundEvents[world.rand.nextInt(soundEvents.length)],
          SoundCategory.BLOCKS,
          1.0f,
          1.0f
      );
    }
  }

  public boolean isActivated() {

    return this.activated;
  }

  @ParametersAreNonnullByDefault
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (this.activated) {
      this.spawnParticles(world, pos, this.getParticleCount());
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (this.activated) {
      world.setBlockState(pos, this.getInactiveState());
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    Random rand = world instanceof World ? ((World) world).rand : RANDOM;
    int count = this.quantityDroppedWithBonus(fortune, rand);

    for (int i = 0; i < count; i++) {

      if (rand.nextFloat() < 0.25) {
        drops.add(new ItemStack(Items.REDSTONE));

      } else {
        drops.add(ItemMaterial.EnumType.DENSE_REDSTONE.asStack());
      }
    }
  }

  @Override
  public CreativeTabs getCreativeTabToDisplayOn() {

    return this.activated ? super.getCreativeTabToDisplayOn() : null;
  }

  @Override
  public int tickRate(@Nonnull World world) {

    return TICK_RATE;
  }

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {

    return super.canPlaceBlockOnSide(world, pos, side)
        && world.isSideSolid(pos.down(), EnumFacing.UP);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

    this.activate(world, pos);
    super.onBlockClicked(world, pos, player);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    this.activate(world, pos);
    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    return false;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, @Nonnull Random random) {

    return this.quantityDropped(random) + random.nextInt(fortune + 1);
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    return (this.activated) ? this.getActiveLightValue() : super.getLightValue(state, world, pos);
  }

  protected abstract IBlockState getInactiveState();

  protected abstract IBlockState getActiveState();

  protected abstract int getActiveLightValue();

  protected abstract int getParticleCount();

  public abstract int getProximityRepairAmount();

  protected void spawnParticles(World world, BlockPos pos, int particleCount) {

    Random random = world.rand;

    for (int i = 0; i < particleCount; ++i) {
      double d1 = (float) pos.getX() + random.nextFloat();
      double d2 = (float) pos.getY() + random.nextFloat();
      double d3 = (float) pos.getZ() + random.nextFloat();

      if (i == 0 && !world.getBlockState(pos.up()).isOpaqueCube()) {
        d2 = (double) pos.getY() + 0.0625 + 1.0;
      }

      if (i == 1 && !world.getBlockState(pos.down()).isOpaqueCube()) {
        d2 = (double) pos.getY() - 0.0625;
      }

      if (i == 2 && !world.getBlockState(pos.south()).isOpaqueCube()) {
        d3 = (double) pos.getZ() + 0.0625 + 1.0;
      }

      if (i == 3 && !world.getBlockState(pos.north()).isOpaqueCube()) {
        d3 = (double) pos.getZ() - 0.0625;
      }

      if (i == 4 && !world.getBlockState(pos.east()).isOpaqueCube()) {
        d1 = (double) pos.getX() + 0.0625 + 1.0;
      }

      if (i == 5 && !world.getBlockState(pos.west()).isOpaqueCube()) {
        d1 = (double) pos.getX() - 0.0625;
      }

      if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
        world.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
      }
    }
  }
}
