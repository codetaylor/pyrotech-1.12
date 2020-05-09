package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * This item exists simply to uncouple the empty stick from the ItemFood class
 * and prevent the campfire's comfort effect (always edible)
 */
public class ItemMarshmallowStickEmpty
    extends Item {

  public static final String NAME = "marshmallow_stick_empty";

  public ItemMarshmallowStickEmpty() {

    this.setMaxStackSize(1);
    this.setMaxDamage(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_STICK_DURABILITY);
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_STICK_DURABILITY;
  }

  @Nonnull
  @Override
  public String getUnlocalizedName() {

    return "item.pyrotech.marshmallow.stick";
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    if (hand == EnumHand.MAIN_HAND && player.isSneaking()) {
      ItemStack itemOffhand = player.getHeldItemOffhand();

      // Try placing a marshmallow on the stick.
      if (itemOffhand.getItem() == ModuleTechBasic.Items.MARSHMALLOW) {
        itemOffhand.shrink(1);
        ItemStack newItemStack = new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_STICK);
        ItemMarshmallowStick.setType(ItemMarshmallowStick.EnumType.MARSHMALLOW, newItemStack);
        this.setCooldownOnMarshmallows(player);
        return new ActionResult<>(EnumActionResult.SUCCESS, newItemStack);
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  private void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_BURNED, 10);
  }
}
