package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.network.SCPacketMarshmallowStickTimestamp;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    this.setMaxDamage(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_STICK_DURABILITY);

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "marshmallow_type"),
        (itemStack, world, entity) -> ItemMarshmallowStick.getType(itemStack).id
    );
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.MARSHMALLOW_STICK_DURABILITY;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    /*if (ItemMarshmallowStick.getType(stack) == EnumType.MARSHMALLOW) {
      return 5 * 20;
    }

    return 0;*/
    return 10 * 60 * 20;
  }

  @Nonnull
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    switch (ItemMarshmallowStick.getType(stack)) {

      case MARSHMALLOW:
      case MARSHMALLOW_ROASTED:
        return EnumAction.BOW;

      default:
        return EnumAction.NONE;
    }
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

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

    return ItemMarshmallowStick.getType(oldStack) != ItemMarshmallowStick.getType(newStack);
  }

  @Nullable
  @Override
  public NBTTagCompound getNBTShareTag(ItemStack stack) {

    // We remove the timestamp here because when it is sent to the client,
    // it replaces the item on the client, and it will interrupt the item
    // being used. We send a custom packet to the client and update this
    // value manually to bypass the interrupt.

    NBTTagCompound tag = super.getNBTShareTag(stack);

    if (tag != null) {

      // Allow the timestamp tag to be sent to the client if we're clearing
      // the value, causing an item change.
      //if (ItemMarshmallowStick.getRoastTimestamp(stack) != Long.MAX_VALUE) {
      tag = tag.copy();
      tag.removeTag("RoastByTimestamp");
      //}
    }
    return tag;
  }

  // ---------------------------------------------------------------------------
  // - NBT Accessors
  // ---------------------------------------------------------------------------

  public static void setType(EnumType type, ItemStack itemStack) {

    // TODO: remove loggers and stdout
    LogManager.getLogger(ItemMarshmallowStick.class).info(type);

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    tag.setInteger("MarshmallowType", type.id);
    itemStack.setTagCompound(tag);
  }

  public static EnumType getType(ItemStack itemStack) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    return EnumType.from(tag.getInteger("MarshmallowType"));
  }

  public static void setRoastByTimestamp(World world, EntityPlayer player, ItemStack itemStack, long timestamp) {

    // TODO: remove logger, unused params
    LogManager.getLogger(ItemMarshmallowStick.class).info(timestamp);

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
    tag.setLong("RoastByTimestamp", timestamp);
    itemStack.setTagCompound(tag);
  }

  public static long getRoastByTimestamp(ItemStack itemStack) {

    NBTTagCompound tag = StackHelper.getTagSafe(itemStack);

    if (!tag.hasKey("RoastByTimestamp")) {
      return Long.MAX_VALUE;
    }

    return tag.getLong("RoastByTimestamp");
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

//    System.out.println("CLICK");

    if (hand == EnumHand.MAIN_HAND) {
      ItemStack itemMainHand = player.getHeldItemMainhand();
      ItemStack itemOffhand = player.getHeldItemOffhand();

      // Try starting the roast.

      if (ItemMarshmallowStick.getType(itemMainHand) != EnumType.EMPTY
          /*|| itemOffhand.getItem() != ModuleTechBasic.Items.MARSHMALLOW*/) {

        RayTraceResult rayTraceResult = this.rayTrace(world, player, false);

        // ray trace result can be null
        //noinspection ConstantConditions
        if (rayTraceResult != null
            && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
            && this.isRoastingBlock(world, rayTraceResult.getBlockPos())) {

          // TODO: Setting the NBT data here is causing an item reset and interrupting the use cycle.
          // When we set the NBT later, set it to roasted, it will interrupt the roasting process and
          // never let the marshmallow burn.
          if (!world.isRemote && ItemMarshmallowStick.getRoastByTimestamp(itemMainHand) == Long.MAX_VALUE) {

            // TODO: timestamp, config these magic numbers

            int roastDuration = 5 * 20;
            float roastVariance = 0.2f;
            roastDuration = Math.max(0, (int) (roastDuration - roastDuration * roastVariance));

            long timestamp = world.getTotalWorldTime() + roastDuration;
            ItemMarshmallowStick.setRoastByTimestamp(world, player, itemMainHand, timestamp);
            ModuleTechBasic.PACKET_SERVICE.sendToAll(new SCPacketMarshmallowStickTimestamp(player, timestamp));
          }

          player.setActiveHand(hand);
          return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);
        }
      }

      // Try placing a marshmallow on the stick or removing one from the stick.

      if (itemOffhand.getItem() == ModuleTechBasic.Items.MARSHMALLOW) {

        // Place a marshmallow on the stick.

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

        // Remove a marshmallow from the stick.

        switch (ItemMarshmallowStick.getType(itemMainHand)) {
          case MARSHMALLOW_BURNED:
            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_BURNED));
            ItemMarshmallowStick.setType(EnumType.EMPTY, itemMainHand);
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          case MARSHMALLOW_ROASTED:
            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW_ROASTED));
            ItemMarshmallowStick.setType(EnumType.EMPTY, itemMainHand);
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          case MARSHMALLOW:
            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModuleTechBasic.Items.MARSHMALLOW));
            ItemMarshmallowStick.setType(EnumType.EMPTY, itemMainHand);
            this.setCooldownOnMarshmallows(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemMainHand);

          case EMPTY:
          default:
        }
      }
    }

    return super.onItemRightClick(world, player, hand);
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

//    System.out.println("USING");

    if (!(player instanceof EntityPlayer)) {
      return;
    }

    World world = player.world;
    RayTraceResult rayTraceResult = this.rayTrace(world, (EntityPlayer) player, false);

    // The ray trace result can be null
    //noinspection ConstantConditions
    if (rayTraceResult == null
        || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK
        || !this.isRoastingBlock(world, rayTraceResult.getBlockPos())) {

      // TODO: Check if within roasting range of the fire

      player.stopActiveHand();
      this.setCooldownOnMarshmallows((EntityPlayer) player);
    }

    // TODO: turn into a roasted stick when it passes the threshold
    if (world.isRemote) {
      long totalWorldTime = world.getTotalWorldTime();
      long roastTimestamp = ItemMarshmallowStick.getRoastByTimestamp(stack);

      if (roastTimestamp < Long.MAX_VALUE) {
        EnumType type = ItemMarshmallowStick.getType(stack);

        if (type == EnumType.MARSHMALLOW_ROASTED && totalWorldTime >= roastTimestamp + 20) { // TODO: magic numbers
          ItemMarshmallowStick.setType(EnumType.MARSHMALLOW_BURNED, stack);

        } else if (type == EnumType.MARSHMALLOW && totalWorldTime >= roastTimestamp) {
          ItemMarshmallowStick.setType(EnumType.MARSHMALLOW_ROASTED, stack);
        }
      }
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeLeft) {

    System.out.println("STOPPED");

    if (!(player instanceof EntityPlayer)) {
      return;
    }

    long totalWorldTime = world.getTotalWorldTime();
    long roastTimestamp = ItemMarshmallowStick.getRoastByTimestamp(stack);

    if (totalWorldTime >= roastTimestamp + 20) { // TODO: magic numbers
      ItemMarshmallowStick.setType(EnumType.MARSHMALLOW_BURNED, stack);

    } else if (totalWorldTime >= roastTimestamp) {
      ItemMarshmallowStick.setType(EnumType.MARSHMALLOW_ROASTED, stack);
    }

    ItemMarshmallowStick.setRoastByTimestamp(world, (EntityPlayer) player, stack, Long.MAX_VALUE);
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase player) {

    System.out.println("FINISH");

    if (!(player instanceof EntityPlayer)) {
      return stack;
    }

    ItemMarshmallowStick.setRoastByTimestamp(world, (EntityPlayer) player, stack, Long.MAX_VALUE);
    ItemMarshmallowStick.setType(EnumType.MARSHMALLOW_BURNED, stack);

    RayTraceResult rayTraceResult = this.rayTrace(world, (EntityPlayer) player, false);

    // The ray trace result can be null
    //noinspection ConstantConditions
    if (rayTraceResult == null
        || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK
        || !this.isRoastingBlock(world, rayTraceResult.getBlockPos())) {

      // TODO: Check if within roasting range of the fire

      player.stopActiveHand();
      return stack;
    }

    // TODO: turn into a burned stick
    return stack;
  }

  public boolean isRoastingBlock(World world, BlockPos blockPos) {

    return world.getTileEntity(blockPos) instanceof TileCampfire;
  }

  private void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(this, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_ROASTED, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_BURNED, 10);
  }
}
