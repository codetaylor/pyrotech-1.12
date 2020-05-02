package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMarshmallowStick
    extends Item {

  public static final String NAME = "marshmallow_stick";

  public ItemMarshmallowStick() {

    this.setMaxStackSize(1);
    this.setMaxDamage(8);
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    // If the player is holding a marshmallow stick in their main hand, we want
    // to check if they're holding a marshmallow in their offhand and apply that
    // marshmallow to the stick if they are.

    if (hand == EnumHand.MAIN_HAND) {
      ItemStack itemMainHand = player.getHeldItemMainhand();
      ItemStack itemOffhand = player.getHeldItemOffhand();

      if (itemOffhand.getItem() == ModuleTechBasic.Items.MARSHMALLOW) {

        if (!world.isRemote) {
          itemOffhand.shrink(1);

          ItemStack newItemStack = new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_STICK_EDIBLE);
          ItemMarshmallowStickEdible.setType(ItemMarshmallowStickEdible.EnumType.MARSHMALLOW, newItemStack);

          if (itemMainHand.getCount() > 1) {
            itemMainHand.shrink(1);
            StackHelper.addToInventoryOrSpawn(world, player, newItemStack, player.getPosition(), 0.5, false, true);
          }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);
      }
    }

    return super.onItemRightClick(world, player, hand);
  }
}
