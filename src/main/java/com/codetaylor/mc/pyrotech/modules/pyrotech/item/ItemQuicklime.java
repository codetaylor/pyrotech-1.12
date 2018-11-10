package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import com.codetaylor.mc.pyrotech.library.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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

public class ItemQuicklime
    extends Item {

  public static final String NAME = "quicklime";

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    ItemStack heldItem = player.getHeldItem(hand);

    if (!heldItem.isEmpty()
        && heldItem.getItem() == ModuleItems.QUICKLIME) {

      RayTraceResult rayTraceResult = this.rayTrace(world, player, true);

      if (rayTraceResult != null
          && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {

        BlockPos blockPos = rayTraceResult.getBlockPos();

        if (!world.isBlockModifiable(player, blockPos)
            || !player.canPlayerEdit(blockPos.offset(rayTraceResult.sideHit), rayTraceResult.sideHit, heldItem)) {
          return new ActionResult<>(EnumActionResult.PASS, heldItem);
        }

        if (world.getBlockState(blockPos).getMaterial() == Material.WATER) {
          this.playSplashSound(world, player);

          ItemStack itemStack = new ItemStack(ModuleItems.QUICKLIME, heldItem.getCount() - 1);
          player.addItemStackToInventory(ItemMaterial.EnumType.SLAKED_LIME.asStack());

          // TODO: config option
          world.setBlockToAir(blockPos);

          return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  private void playSplashSound(World world, EntityPlayer player) {

    world.playSound(
        player,
        player.posX,
        player.posY,
        player.posZ,
        SoundEvents.ENTITY_BOBBER_SPLASH,
        SoundCategory.PLAYERS,
        1,
        (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
    );
  }
}
