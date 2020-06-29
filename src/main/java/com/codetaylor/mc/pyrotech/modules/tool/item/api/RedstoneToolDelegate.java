package com.codetaylor.mc.pyrotech.modules.tool.item.api;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.core.block.BlockOreDenseRedstoneBase;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IRedstoneTool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public final class RedstoneToolDelegate {

  public static long ticksToMillis(long ticks) {

    return ticks * 50;
  }

  public static long getExpirationTimestamp(long ticks) {

    return System.currentTimeMillis() + RedstoneToolDelegate.ticksToMillis(ticks);
  }

  public static boolean isActive(ItemStack itemStack) {

    Item item = itemStack.getItem();

    if (!(item instanceof IRedstoneTool)) {
      return false;
    }

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);

    if (!compound.hasKey("Pyrotech")) {
      return false;
    }

    NBTTagCompound pyrotech = compound.getCompoundTag("Pyrotech");

    if (!pyrotech.hasKey("RedstoneToolActiveExpiresTimestamp")) {
      return false;
    }

    return pyrotech.getLong("RedstoneToolActiveExpiresTimestamp") > System.currentTimeMillis();
  }

  public static void setActive(ItemStack itemStack, int durationTicks) {

    Item item = itemStack.getItem();

    if (!(item instanceof IRedstoneTool)) {
      return;
    }

    NBTTagCompound compound = StackHelper.getTagSafe(itemStack);
    NBTTagCompound pyrotech = compound.getCompoundTag("Pyrotech");
    pyrotech.setLong("RedstoneToolActiveExpiresTimestamp", RedstoneToolDelegate.getExpirationTimestamp(durationTicks));
    compound.setTag("Pyrotech", pyrotech);
  }

  public static void setDamage(IRedstoneTool item, @Nonnull ItemStack itemStack, int damage) {

    boolean damaged = (item.getRedstoneToolDamage(itemStack) < damage);
    boolean active = item.isRedstoneToolActive(itemStack);

    if (active && damaged) {

      if (RandomHelper.random().nextFloat() < ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_DAMAGE_CHANCE) {
        item.setRedstoneToolDamage(itemStack, damage);
      }

    } else {
      item.setRedstoneToolDamage(itemStack, damage);
    }

    if (active || damaged) {
      double activationChance = (active) ? ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_ACTIVATION_CHANCE : ModuleToolConfig.REDSTONE_TOOLS.INACTIVE_ACTIVATION_CHANCE;

      if (RandomHelper.random().nextFloat() < activationChance) {
        item.activateRedstoneTool(itemStack);
      }
    }
  }

  public static void activateNearbyDenseRedstoneOre(ItemStack itemStack, World world, Entity entity) {

    if (world.isRemote) {
      return;
    }

    if (!(entity instanceof EntityPlayer)) {
      return;
    }

    EntityPlayer player = (EntityPlayer) entity;

    if (world.getTotalWorldTime() % 20 != 0) {
      return;
    }

    if (player.getHeldItemMainhand() != itemStack) {
      return;
    }

    Item item = itemStack.getItem();

    if (item instanceof IRedstoneTool) {
      BlockPos pos = player.getPosition();

      BlockHelper.forBlocksInCubeShuffled(world, pos, 4, 4, 4, (w, p, bs) -> {

        if (bs.getBlock() instanceof BlockOreDenseRedstoneBase) {
          BlockOreDenseRedstoneBase block = (BlockOreDenseRedstoneBase) bs.getBlock();

          if (!block.isActivated()) {
            block.activate(w, p);
            RedstoneToolDelegate.activateAndHealTool(itemStack, world, (IRedstoneTool) item);
          }

          return world.rand.nextFloat() < 0.32;

        } else if (bs.getBlock() == Blocks.REDSTONE_ORE) {
          world.setBlockState(p, Blocks.LIT_REDSTONE_ORE.getDefaultState());
          RedstoneToolDelegate.activateAndHealTool(itemStack, world, (IRedstoneTool) item);
          return world.rand.nextFloat() < 0.16;
        }
        return true; // keep processing
      });
    }
  }

  private static void activateAndHealTool(ItemStack itemStack, World world, IRedstoneTool item) {

    if (!item.isRedstoneToolActive(itemStack)) {
      item.activateRedstoneTool(itemStack);
    }

    if (world.rand.nextFloat() < ModuleToolConfig.REDSTONE_TOOLS.PROXIMITY_REPAIR_CHANCE) {
      itemStack.setItemDamage(itemStack.getItemDamage() - 1);
    }
  }

  private RedstoneToolDelegate() {
    //
  }
}
