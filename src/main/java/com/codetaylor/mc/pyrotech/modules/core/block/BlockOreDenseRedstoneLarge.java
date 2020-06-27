package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockOreDenseRedstoneLarge
    extends BlockPartialBase {

  public static final String NAME = "dense_redstone_ore_large_inactive";
  public static final String NAME_ACTIVATED = "dense_redstone_ore_large";

  private static final AxisAlignedBB AABB = AABBHelper.create(2, 0, 2, 14, 16, 14);

  private static final int TICK_RATE = 30;

  protected final boolean isActivated;

  public BlockOreDenseRedstoneLarge(boolean isActivated) {

    super(Material.ROCK);
    this.isActivated = isActivated;
    this.setResistance(5.0F);
    this.setHardness(3);
    this.setHarvestLevel("pickaxe", 2);
    this.setSoundType(SoundType.STONE);
    this.setTickRandomly(this.isActivated);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  @Override
  public CreativeTabs getCreativeTabToDisplayOn() {

    return this.isActivated ? super.getCreativeTabToDisplayOn() : null;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile.pyrotech.dense_redstone_ore";
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    return (this.isActivated) ? 11 : super.getLightValue(state, world, pos);
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

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  protected ItemStack getSilkTouchDrop(IBlockState state) {

    return new ItemStack(ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE_ACTIVATED);
  }

  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {

    return 5 + RANDOM.nextInt(10);
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {

    return this.quantityDropped(random) + random.nextInt(fortune + 1);
  }

  @Override
  public int quantityDropped(Random random) {

    return 4 + random.nextInt(8);
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
        drops.add(ItemMaterial.EnumType.REDSTONE_SHARD.asStack());
      }
    }
  }

  @ParametersAreNonnullByDefault
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    if (this.isActivated) {
      this.spawnParticles(world, pos);
    }
  }

  @ParametersAreNonnullByDefault
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (this.isActivated) {
      world.setBlockState(pos, this.getInactiveState());
    }
  }

  protected void activate(World worldIn, BlockPos pos) {

    this.spawnParticles(worldIn, pos);

    if (!this.isActivated) {
      worldIn.setBlockState(pos, this.getActiveState());
    }
  }

  protected IBlockState getInactiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE.getDefaultState();
  }

  protected IBlockState getActiveState() {

    return ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE_ACTIVATED.getDefaultState();
  }

  protected void spawnParticles(World world, BlockPos pos) {

    Random random = world.rand;

    for (int i = 0; i < 6; ++i) {
      double d1 = (float) pos.getX() + random.nextFloat();
      double d2 = (float) pos.getY() + random.nextFloat();
      double d3 = (float) pos.getZ() + random.nextFloat();

      if (i == 0 && !world.getBlockState(pos.up()).isOpaqueCube()) {
        d2 = (double) pos.getY() + 0.0625D + 1.0D;
      }

      if (i == 1 && !world.getBlockState(pos.down()).isOpaqueCube()) {
        d2 = (double) pos.getY() - 0.0625D;
      }

      if (i == 2 && !world.getBlockState(pos.south()).isOpaqueCube()) {
        d3 = (double) pos.getZ() + 0.0625D + 1.0D;
      }

      if (i == 3 && !world.getBlockState(pos.north()).isOpaqueCube()) {
        d3 = (double) pos.getZ() - 0.0625D;
      }

      if (i == 4 && !world.getBlockState(pos.east()).isOpaqueCube()) {
        d1 = (double) pos.getX() + 0.0625D + 1.0D;
      }

      if (i == 5 && !world.getBlockState(pos.west()).isOpaqueCube()) {
        d1 = (double) pos.getX() - 0.0625D;
      }

      if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
        world.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
      }
    }
  }
}
