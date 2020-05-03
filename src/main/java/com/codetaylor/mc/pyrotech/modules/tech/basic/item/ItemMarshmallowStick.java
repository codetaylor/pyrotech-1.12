package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ItemMarshmallowStick
    extends Item {

  public static final String NAME = "marshmallow_stick";

  public enum EnumType {
    EMPTY(0), MARSHMALLOW(1), MARSHMALLOW_ROASTED(2), MARSHMALLOW_BURNED(3);

    private static final Int2ObjectMap<EnumType> TYPES = new Int2ObjectOpenHashMap<>(EnumType.values().length);

    static {
      Arrays.stream(EnumType.values()).forEach(enumType -> TYPES.put(enumType.id, enumType));
    }

    public static EnumType from(int id) {

      return TYPES.get(id);
    }

    private final int id;

    EnumType(int id) {

      this.id = id;
    }
  }

  public ItemMarshmallowStick() {

    this.setMaxStackSize(1);
    this.setMaxDamage(8);

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "marshmallow_type"),
        (itemStack, world, entity) -> ItemMarshmallowStick.getType(itemStack).id
    );
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    EnumType type = ItemMarshmallowStick.getType(stack);

    if (type == EnumType.MARSHMALLOW) {
      return "item.pyrotech.marshmallow.on.stick";

    } else if (type == EnumType.MARSHMALLOW_BURNED) {
      return "item.pyrotech.marshmallow.on.stick.burned";

    } else if (type == EnumType.MARSHMALLOW_ROASTED) {
      return "item.pyrotech.marshmallow.on.stick.roasted";
    }

    return super.getUnlocalizedName(stack);
  }

  public static void setType(EnumType type, ItemStack itemStack) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    tag.setInteger("MarshmallowType", type.id);
    itemStack.setTagCompound(tag);
  }

  public static EnumType getType(ItemStack itemStack) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    return EnumType.from(tag.getInteger("MarshmallowType"));
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    if (hand == EnumHand.MAIN_HAND) {
      ItemStack itemMainHand = player.getHeldItemMainhand();
      ItemStack itemOffhand = player.getHeldItemOffhand();

      if (itemOffhand.getItem() == ModuleTechBasic.Items.MARSHMALLOW) {

        switch (ItemMarshmallowStick.getType(itemMainHand)) {
          case MARSHMALLOW_BURNED:
          case MARSHMALLOW_ROASTED:
            break;

          case MARSHMALLOW:
            if (itemOffhand.getCount() < itemOffhand.getMaxStackSize()) {
              player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW, itemOffhand.getCount() + 1));
              ItemMarshmallowStick.setType(EnumType.EMPTY, itemMainHand);
              this.setCooldownOnMarshmallows(player);
              return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);
            }

          case EMPTY:
            itemOffhand.shrink(1);
            ItemMarshmallowStick.setType(ItemMarshmallowStick.EnumType.MARSHMALLOW, itemMainHand);
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          default:
        }

      } else if (itemOffhand.isEmpty()) {

        switch (ItemMarshmallowStick.getType(itemMainHand)) {
          case MARSHMALLOW_BURNED:
            // TODO: remove burned marshmallow

          case MARSHMALLOW_ROASTED:
            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_ROASTED));
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          case MARSHMALLOW:
            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW));
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          case EMPTY:
          default:
        }
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  private void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
  }
}
