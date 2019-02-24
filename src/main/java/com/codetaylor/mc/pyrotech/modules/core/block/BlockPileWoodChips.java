package com.codetaylor.mc.pyrotech.modules.core.block;

import com.codetaylor.mc.pyrotech.library.spi.block.BlockPileBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockPileWoodChips
    extends BlockPileBase {

  public static final String NAME = "pile_wood_chips";

  public BlockPileWoodChips() {

    super(Material.WOOD);
    this.setHardness(0.25f);
    this.setResistance(0);
    this.setHarvestLevel("shovel", 0);
    this.setSoundType(SoundType.GROUND);
  }

  @Override
  protected ItemStack getDrop(World world, BlockPos pos, IBlockState state) {

    return new ItemStack(ModuleCore.Blocks.ROCK, 1, BlockRock.EnumType.WOOD_CHIPS.getMeta());
  }

  @Override
  public boolean canHarvestBlock(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {

    return (player.getHeldItemMainhand().getItem() instanceof ItemSpade);
  }
}
