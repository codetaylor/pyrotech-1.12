package com.codetaylor.mc.pyrotech.modules.tool.item.api;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IQuartzTool;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IRedstoneTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public final class QuartzToolDelegate {

  public static boolean isActive(ItemStack itemStack) {

    Item item = itemStack.getItem();

    if (!(item instanceof IQuartzTool)) {
      return false;
    }

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);

    if (!compound.hasKey("Pyrotech")) {
      return false;
    }

    NBTTagCompound pyrotech = compound.getCompoundTag("Pyrotech");

    if (!pyrotech.hasKey("QuartzToolActive")) {
      return false;
    }

    return pyrotech.getBoolean("QuartzToolActive");
  }

  public static void setActive(ItemStack itemStack, boolean active) {

    Item item = itemStack.getItem();

    if (!(item instanceof IQuartzTool)) {
      return;
    }

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);
    NBTTagCompound pyrotech = compound.getCompoundTag("Pyrotech");
    pyrotech.setBoolean("QuartzToolActive", active);
    compound.setTag("Pyrotech", pyrotech);
  }

  public static void onUpdate(World world, ItemStack itemStack) {

    boolean active = QuartzToolDelegate.isActive(itemStack);
    boolean isNether = world.provider.isNether();

    if (active && !isNether) {
      QuartzToolDelegate.setActive(itemStack, false);

    } else if (!active && isNether) {
      QuartzToolDelegate.setActive(itemStack, true);
    }
  }

  private QuartzToolDelegate() {
    //
  }
}
