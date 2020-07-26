package com.codetaylor.mc.pyrotech.modules.core.item;

import com.codetaylor.mc.athenaeum.util.Properties;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockStrawBed;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemStrawBed
    extends ItemBlock {

  public ItemStrawBed(Block block) {

    super(block);
    this.setMaxStackSize(1);
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return EnumActionResult.SUCCESS;

    } else if (facing != EnumFacing.UP) {
      return EnumActionResult.FAIL;

    } else {
      IBlockState blockState = world.getBlockState(pos);
      Block block = blockState.getBlock();
      boolean isReplaceable = block.isReplaceable(world, pos);

      if (!isReplaceable) {
        pos = pos.up();
      }

      EnumFacing playerFacing = player.getHorizontalFacing();
      BlockPos offsetPos = pos.offset(playerFacing);
      ItemStack heldItem = player.getHeldItem(hand);

      if (player.canPlayerEdit(pos, facing, heldItem)
          && player.canPlayerEdit(offsetPos, facing, heldItem)) {

        IBlockState offsetBlockState = world.getBlockState(offsetPos);

        if (this.canPlaceAt(world, pos)
            && this.canPlaceAt(world, offsetPos)) {

          IBlockState blockStateFoot = ModuleCore.Blocks.STRAW_BED.getDefaultState()
              .withProperty(BlockStrawBed.OCCUPIED, false)
              .withProperty(Properties.FACING_HORIZONTAL, playerFacing)
              .withProperty(BlockStrawBed.TYPE, BlockStrawBed.EnumType.FOOT);
          world.setBlockState(pos, blockStateFoot, 2 | 8);

          IBlockState blockStateHead = blockStateFoot
              .withProperty(BlockStrawBed.TYPE, BlockStrawBed.EnumType.HEAD);
          world.setBlockState(offsetPos, blockStateHead, 2 | 8);

          SoundType soundtype = blockStateFoot.getBlock().getSoundType(blockStateFoot, world, pos, player);
          world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
          world.notifyNeighborsRespectDebug(pos, block, false);
          world.notifyNeighborsRespectDebug(offsetPos, offsetBlockState.getBlock(), false);

          if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
          }

          heldItem.shrink(1);
          return EnumActionResult.SUCCESS;

        } else {
          return EnumActionResult.FAIL;
        }

      } else {
        return EnumActionResult.FAIL;
      }
    }
  }

  private boolean canPlaceAt(World world, BlockPos pos) {

    return (world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos))
        && world.getBlockState(pos.down()).isTopSolid();
  }
}
