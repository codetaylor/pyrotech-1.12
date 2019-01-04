package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockFarmlandMulched;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleBlocks;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMulch
    extends Item {

  public static final String NAME = "mulch";

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack heldItem = player.getHeldItem(hand);

    if (!heldItem.isEmpty()
        && heldItem.getItem() == ModuleItems.MULCH) {

      RayTraceResult rayTraceResult = this.rayTrace(world, player, true);

      //noinspection ConstantConditions
      if (rayTraceResult != null
          && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {

        BlockPos blockPos = rayTraceResult.getBlockPos();
        IBlockState blockState = world.getBlockState(blockPos);

        if (blockState.getBlock() != Blocks.FARMLAND) {
          // Allow clicking on plants directly on top of farmland.
          blockPos = blockPos.down();
          blockState = world.getBlockState(blockPos);
        }

        if (!world.isBlockModifiable(player, blockPos)
            || !player.canPlayerEdit(blockPos.offset(rayTraceResult.sideHit), rayTraceResult.sideHit, heldItem)) {
          return new ActionResult<>(EnumActionResult.PASS, heldItem);
        }

        if (blockState.getBlock() == Blocks.FARMLAND) {

          if (blockState.getValue(BlockFarmland.MOISTURE) > 0) {

            if (!world.isRemote) {
              world.setBlockState(blockPos, ModuleBlocks.FARMLAND_MULCHED.getDefaultState()
                  .withProperty(BlockFarmlandMulched.FERTILIZER, 15), 1 | 2);

            } else {
              this.playSound(world, player);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, StackHelper.decrease(heldItem, 1, false));
          }
        }
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  private void playSound(World world, EntityPlayer player) {

    world.playSound(
        player,
        player.posX,
        player.posY,
        player.posZ,
        SoundEvents.BLOCK_GRASS_PLACE,
        SoundCategory.PLAYERS,
        1,
        (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
    );
  }
}
