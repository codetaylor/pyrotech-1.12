package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.spi.BlockPileBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TilePileSlag;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockPileSlag
    extends BlockPileBase {

  public static final String NAME = "pile_slag";

  public static final PropertyBool MOLTEN = PropertyBool.create("molten");

  public BlockPileSlag() {

    super(Material.ROCK);
    this.setHardness(1.75f);
    this.setResistance(40);
    this.setHarvestLevel("pickaxe", 1);
    this.setSoundType(SoundType.STONE);
    this.setTickRandomly(true);

    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(BlockPileBase.LEVEL, 8)
        .withProperty(BlockPileSlag.MOLTEN, false));
  }

  @Override
  protected ItemStack getDrop(World world, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TilePileSlag) {
      return ((TilePileSlag) tileEntity).getStackHandler().extractItem(0, 1, false);
    }

    return ItemStack.EMPTY;
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (state.getBlock() == this
        && state.getValue(BlockPileSlag.MOLTEN)
        && rand.nextDouble() < 1.0) {

      world.setBlockState(pos, state.withProperty(BlockPileSlag.MOLTEN, false));
    }
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    IBlockState blockState = world.getBlockState(pos);

    if (blockState.getBlock() == this
        && blockState.getValue(BlockPileSlag.MOLTEN)
        && ModulePyrotechConfig.BLOOMERY.MOLTEN_SLAG_ENTITY_WALK_DAMAGE > 0
        && !entity.isImmuneToFire()
        && entity instanceof EntityLivingBase
        && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
      entity.attackEntityFrom(DamageSource.HOT_FLOOR, (float) ModulePyrotechConfig.BLOOMERY.MOLTEN_SLAG_ENTITY_WALK_DAMAGE);
    }

    super.onEntityWalk(world, pos, entity);
  }

  // ---------------------------------------------------------------------------
  // - Tile
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TilePileSlag();
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, BlockPileBase.LEVEL, BlockPileSlag.MOLTEN);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState()
        .withProperty(BlockPileBase.LEVEL, (meta & 7) + 1)
        .withProperty(BlockPileSlag.MOLTEN, ((meta >> 3) & 1) == 1);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return (state.getValue(BlockPileBase.LEVEL) - 1)
        | ((state.getValue(BlockPileSlag.MOLTEN) ? 1 : 0) << 3);
  }
}
