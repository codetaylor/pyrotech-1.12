package com.codetaylor.mc.pyrotech.modules.tech.bloomery.block;

import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileWitherForge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockWitherForge
    extends BlockBloomery {

  public static final String NAME = "wither_forge";

  protected double getEntityWalkBurnDamage() {

    return ModuleTechBloomeryConfig.WITHER_FORGE.ENTITY_WALK_BURN_DAMAGE;
  }

  // ---------------------------------------------------------------------------
  // - Light
  // ---------------------------------------------------------------------------

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (this.getActualState(state, world, pos).getValue(TYPE) == EnumType.BottomLit) {
      return 15;
    }

    return super.getLightValue(state, world, pos);
  }

  // ---------------------------------------------------------------------------
  // - Tile Entity
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    if (this.isTop(state)) {
      return new TileWitherForge.Top();

    } else {
      return new TileWitherForge();
    }
  }
}
