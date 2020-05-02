package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ItemMarshmallowStickEdible
    extends ItemFood {

  public static final String NAME = "marshmallow_stick_edible";

  public enum EnumType {
    MARSHMALLOW(0), MARSHMALLOW_ROASTED(1), MARSHMALLOW_BURNED(2);

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

  public ItemMarshmallowStickEdible() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_SATURATION, false);

    this.setMaxStackSize(1);
    this.setMaxDamage(8);

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "marshmallow_type"),
        (itemStack, world, entity) -> ItemMarshmallowStickEdible.getType(itemStack).id
    );
  }

  @Nullable
  @Override
  public CreativeTabs getCreativeTab() {

    return null;
  }

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    EnumType type = ItemMarshmallowStickEdible.getType(stack);

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

    // TODO: If not looking at a campfire.

    return super.onItemRightClick(world, player, hand);
  }
}
