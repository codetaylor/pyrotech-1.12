package com.codetaylor.mc.pyrotech.modules.bucket.item;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.util.FluidHelper;
import com.codetaylor.mc.athenaeum.util.FluidUtilFix;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.library.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public abstract class ItemBucketBase
    extends UniversalBucket {

  public ItemBucketBase() {

    this.setHasSubtypes(true);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @ParametersAreNonnullByDefault
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

    if (oldStack.getItem() != newStack.getItem()) {
      return true;
    }

    if (oldStack.getMetadata() != newStack.getMetadata()) {
      return true;
    }

    FluidStack oldFluidStack = this.getFluid(oldStack);
    FluidStack newFluidStack = this.getFluid(newStack);

    if (oldFluidStack == null
        && newFluidStack == null) {
      return slotChanged;
    }

    if (oldFluidStack == null) {
      return true;
    }

    if (newFluidStack == null) {
      return true;
    }

    return slotChanged || oldFluidStack.getFluid() != newFluidStack.getFluid();
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {

    return (this.hasFluid(stack) || stack.getMetadata() == EnumType.MILK.getMeta()) ? 1 : this.getBucketStackLimit();
  }

  protected abstract int getBucketStackLimit();

  @Override
  public int getItemBurnTime(ItemStack stack) {

    FluidStack fluid = this.getFluid(stack);

    if (fluid != null
        && fluid.getFluid() == FluidRegistry.LAVA) {
      return 20000;
    }

    return 0;
  }

  @Nonnull
  @Override
  public ItemStack getEmpty() {

    return new ItemStack(this, 1, EnumType.EMPTY.getMeta());
  }

  @Nullable
  @Override
  public Item getContainerItem() {

    return super.getContainerItem();
  }

  @Override
  public boolean hasContainerItem(@Nonnull ItemStack stack) {

    return super.hasContainerItem(stack);
  }

  @Nonnull
  @Override
  public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {

    if (!this.getEmpty().isEmpty()) {

      int durability = this.getDurability(itemStack);

      if (durability <= 0) {
        ModPyrotech.PROXY.playSound(SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
        return new ItemStack(Blocks.AIR);
      }

      // Create a copy such that the game can't mess with it
      ItemStack copy = this.getEmpty().copy();
      this.setDurability(copy, durability);
      return copy;
    }

    return super.getContainerItem(itemStack);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (this.getDurability(stack) == this.getMaxDurability()) {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.uses.full", this.getDurability(stack)));

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.uses", this.getDurability(stack), this.getMaxDurability()));
    }

    if (this.getHotContainerDamagePerSecond() <= 0) {
      tooltip.add(TextFormatting.GREEN + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids.true"));

    } else {
      tooltip.add(TextFormatting.RED + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.hot.fluids.false"));
    }
  }

  @Override
  public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {

    if (!this.isInCreativeTab(tab)) {
      return;
    }

    subItems.add(new ItemStack(this));

    if (!this.showAllBuckets()) {
      return;
    }

    subItems.add(new ItemStack(this, 1, EnumType.MILK.getMeta()));

    for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {

      if (!fluid.getName().equals("milk")) {

        // add all fluids that the bucket can be filled  with
        FluidStack fluidStack = new FluidStack(fluid, getCapacity());
        ItemStack stack = new ItemStack(this);
        IFluidHandlerItem fluidHandler = new BucketWrapper(stack);

        if (fluidHandler.fill(fluidStack, false) == fluidStack.amount) {
          subItems.add(this.createWithFluid(fluidStack));
        }
      }
    }
  }

  protected abstract boolean showAllBuckets();

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    if (stack.getMetadata() != EnumType.EMPTY.getMeta()) {
      String fluidName = EnumType.fromMeta(stack.getMetadata()).getName();
      return I18n.translateToLocal("item." + Util.prefix(this.getLangKey() + "." + fluidName) + ".name");
    }

    FluidStack fluidStack = this.getFluid(stack);

    if (fluidStack == null) {
      return I18n.translateToLocal("item." + Util.prefix(this.getLangKey() + ".empty") + ".name");
    }

    String unlocalizedName = this.getUnlocalizedNameInefficiently(stack);
    String name = fluidStack.getFluid().getName();

    if (I18n.canTranslate(unlocalizedName + "." + name)) {
      return I18n.translateToLocal(unlocalizedName + "." + name);
    }

    return I18n.translateToLocalFormatted(unlocalizedName + ".name", fluidStack.getLocalizedName());
  }

  protected abstract String getLangKey();

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);

    if (stack.getMetadata() == EnumType.MILK.getMeta()) {
      player.setActiveHand(hand);
      return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    RayTraceResult target = this.rayTrace(world, player, !this.hasFluid(stack));
    ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, target);

    if (ret != null
        && ret.getType() == EnumActionResult.SUCCESS) {
      return ret;
    }

    //noinspection ConstantConditions
    if (target == null
        || target.typeOfHit != RayTraceResult.Type.BLOCK) {
      return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    // Cauldron

    BlockPos targetBlockPos = target.getBlockPos();
    ItemStack cauldronResult = this.interactWithCauldron(world, player, targetBlockPos, world.getBlockState(targetBlockPos), stack);

    if (cauldronResult != null) {
      return ActionResult.newResult(EnumActionResult.SUCCESS, cauldronResult);
    }

    // Place Fluid

    ItemStack result = this.placeFluidFromContainer(world, player, targetBlockPos, target.sideHit, stack);

    if (result.isEmpty()) {
      return ActionResult.newResult(EnumActionResult.FAIL, stack);

    } else {
      return ActionResult.newResult(EnumActionResult.SUCCESS, result);
    }
  }

  // ---------------------------------------------------------------------------
  // - Cauldron
  // ---------------------------------------------------------------------------

  @Nullable
  private ItemStack interactWithCauldron(World world, EntityPlayer player, BlockPos pos, IBlockState blockState, ItemStack containerStack) {

    if (!(blockState.getBlock() instanceof BlockCauldron)) {
      return null;
    }

    int level = blockState.getValue(BlockCauldron.LEVEL);
    FluidStack fluidStack = this.getFluid(containerStack);
    int durability = this.getDurability(containerStack);

    if (fluidStack != null
        && fluidStack.getFluid() == FluidRegistry.WATER) {

      // Fill cauldron with water

      if (level < 3) {

        if (!world.isRemote) {
          player.addStat(StatList.CAULDRON_FILLED);
          Blocks.CAULDRON.setWaterLevel(world, pos, blockState, 3);
          FluidHelper.playFluidEmptySoundServer(FluidRegistry.WATER, world, pos);
        }

        if (!player.capabilities.isCreativeMode) {
          containerStack.shrink(1);
          ItemStack emptyStack = new ItemStack(this, 1, EnumType.EMPTY.getMeta());
          this.setDurability(emptyStack, durability - 1);

          if (this.isBroken(emptyStack)) {
            emptyStack = this.getBrokenItemStack();
            SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
          }

          if (containerStack.isEmpty()) {
            return emptyStack;

          } else {
            ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
            return containerStack;
          }
        }

        return containerStack;
      }

    } else if (fluidStack == null) {

      // Drain cauldron of water

      if (level == 3) {

        if (!world.isRemote) {
          player.addStat(StatList.CAULDRON_USED);
          Blocks.CAULDRON.setWaterLevel(world, pos, blockState, 0);
          FluidHelper.playFluidFillSoundServer(FluidRegistry.WATER, world, pos);
        }

        if (!player.capabilities.isCreativeMode) {
          containerStack.shrink(1);
          FluidStack water = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
          ItemStack filledBucket = this.createWithFluid(water);
          this.setDurability(filledBucket, durability);

          if (containerStack.isEmpty()) {
            return filledBucket;

          } else {
            ItemHandlerHelper.giveItemToPlayer(player, filledBucket);
            return containerStack;
          }
        }
      }

      return containerStack;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Fluid Placement
  // ---------------------------------------------------------------------------

  @Nullable
  private FluidActionResult tryPlaceFluid(World world, EntityPlayer player, BlockPos pos, ItemStack containerStack, FluidStack fluidStack) {

    if (!world.isRemote && world.isBlockModifiable(player, pos)) {
      return FluidUtil.tryPlaceFluid(player, world, pos, containerStack, fluidStack);
    }

    return null;
  }

  /**
   * Tries to place a fluid and returns either an empty item stack or the
   * remaining item(s) in the container stack.
   *
   * @param world
   * @param player
   * @param pos
   * @param sideHit
   * @param containerStack
   * @return an empty item stack or the remaining item(s)
   */
  private ItemStack placeFluidFromContainer(World world, EntityPlayer player, BlockPos pos, EnumFacing sideHit, ItemStack containerStack) {

    if (!world.isBlockModifiable(player, pos)) {
      return ItemStack.EMPTY;
    }

    // The block adjacent to the side we clicked on
    BlockPos targetPos = pos.offset(sideHit);

    // Can the player place there?
    if (player.canPlayerEdit(targetPos, sideHit, containerStack)) {

      // Try to place the liquid
      FluidStack fluidStack = this.getFluid(containerStack);

      // Use FluidUtilFix instead of Forge's FluidUtil
      // Fix for https://github.com/codetaylor/pyrotech-1.12/issues/375
      FluidActionResult result = FluidUtilFix.tryPlaceFluid(player, world, targetPos, containerStack, fluidStack);

      if (result.isSuccess()
          && !player.capabilities.isCreativeMode) {

        // success!
        //noinspection ConstantConditions
        player.addStat(StatList.getObjectUseStats(this));
        int durability = this.getDurability(containerStack);
        containerStack.shrink(1);
        ItemStack drained = result.getResult();
        ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);
        this.setDurability(emptyStack, durability - 1);

        if (this.isBroken(emptyStack)) {
          emptyStack = this.getBrokenItemStack();
          SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
        }

        // check whether we replace the item or add the empty one to the inventory
        if (containerStack.isEmpty()) {
          return emptyStack;

        } else {
          // add empty bucket to player inventory
          ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
          return containerStack;
        }
      }
    }

    return ItemStack.EMPTY;
  }

  // ---------------------------------------------------------------------------
  // - Milk
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    return stack.getMetadata() == EnumType.MILK.getMeta() ? EnumAction.DRINK : EnumAction.NONE;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return stack.getMetadata() == EnumType.MILK.getMeta() ? 32 : 0;
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving) {

    if (stack.getMetadata() != EnumType.MILK.getMeta()) {
      return stack;
    }

    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
      int durability = this.getDurability(stack);

      if (!entityPlayer.capabilities.isCreativeMode) {
        stack = new ItemStack(this);
        this.setDurability(stack, durability - 1);

        if (this.isBroken(stack)) {
          stack = this.getBrokenItemStack();
          SoundHelper.playSoundServer(world, entityLiving.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
        }
      }

      //noinspection ConstantConditions
      entityPlayer.addStat(StatList.getObjectUseStats(this));
    }

    if (!world.isRemote) {
      entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
    }

    return stack;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {

    if (this.isCowMilkDisabled()) {
      return false;
    }

    if (this.hasFluid(stack)
        || player.capabilities.isCreativeMode) {
      return false;
    }

    if (target instanceof EntityCow) {
      int durability = this.getDurability(stack);
      player.playSound(SoundEvents.ENTITY_COW_MILK, 1, 1);

      if (stack.getCount() == 1) {
        stack.setItemDamage(EnumType.MILK.getMeta());
        this.setDurability(stack, durability);

      } else {
        stack.shrink(1);
        ItemStack result = EnumType.MILK.asStack(this);
        this.setDurability(result, durability);

        ItemHandlerHelper.giveItemToPlayer(player, result, player.inventory.currentItem);
      }

      return true;
    }

    return false;
  }

  protected abstract boolean isCowMilkDisabled();

  // ---------------------------------------------------------------------------
  // - Durability
  // ---------------------------------------------------------------------------

  protected abstract int getMaxDurability();

  protected abstract int getHotTemperature();

  protected abstract int getHotContainerDamagePerSecond();

  protected abstract int getHotPlayerDamagePerSecond();

  protected abstract int getFullContainerDamagePerSecond();

  public boolean isBroken(ItemStack itemStack) {

    return this.getDurability(itemStack) <= 0;
  }

  protected ItemStack getBrokenItemStack() {

    return ItemStack.EMPTY;
  }

  public void setDurability(ItemStack itemStack, int durability) {

    NBTTagCompound tag = itemStack.getTagCompound();

    if (tag == null) {
      tag = new NBTTagCompound();
    }

    tag.setInteger("durability", durability);
    itemStack.setTagCompound(tag);
  }

  public int getDurability(ItemStack itemStack) {

    NBTTagCompound tag = itemStack.getTagCompound();

    if (tag == null) {
      return this.getMaxDurability();
    }

    if (tag.hasKey("durability")) {
      return tag.getInteger("durability");
    }

    return this.getMaxDurability();
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return this.getDurability(stack) < this.getMaxDurability();
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    return 1 - this.getDurability(stack) / (double) this.getMaxDurability();
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

    if (world.isRemote) {
      return;
    }

    if (world.getTotalWorldTime() % 20 != 0) {
      return;
    }

    FluidStack fluidStack = this.getFluid(stack);

    if (fluidStack == null) {
      return;
    }

    Fluid fluid = fluidStack.getFluid();

    int containerDamagePerSecond = this.getFullContainerDamagePerSecond();

    if (fluid.getTemperature(fluidStack) >= this.getHotTemperature()) {
      int playerDamagePerSecond = this.getHotPlayerDamagePerSecond();

      if (playerDamagePerSecond > 0) {
        entity.attackEntityFrom(DamageSource.IN_FIRE, playerDamagePerSecond);
        entity.setFire(1);
      }

      containerDamagePerSecond += this.getHotContainerDamagePerSecond();
    }

    if (containerDamagePerSecond == 0) {
      return;
    }

    int durability = this.getDurability(stack);
    durability -= containerDamagePerSecond;

    if (durability <= 0) {

      if (itemSlot == 0 && entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;

        if (player.getHeldItemOffhand() == stack) {
          player.setHeldItem(EnumHand.OFF_HAND, this.getBrokenItemStack());
        }

        if (player.getHeldItemMainhand() == stack) {
          entity.replaceItemInInventory(itemSlot, this.getBrokenItemStack());
        }

      } else {
        entity.replaceItemInInventory(itemSlot, this.getBrokenItemStack());
      }

      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) entity;
        BlockPos pos = player.getPosition();
        FluidActionResult fluidActionResult = this.tryPlaceFluid(player.getEntityWorld(), player, pos, stack, fluidStack);

        if (this.dropFluidSourceOnBreak()
            && fluidActionResult != null
            && fluidActionResult.isSuccess()) {
          IBlockState blockState = world.getBlockState(pos);
          Block block = blockState.getBlock();
          IBlockState defaultState = block.getDefaultState();

          if (block instanceof IFluidBlock
              || block instanceof BlockLiquid) {
            world.setBlockState(pos, defaultState.withProperty(BlockLiquid.LEVEL, 5), 1 | 2 | 8);
          }
        }
      }

      SoundHelper.playSoundServer(world, entity.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);

    } else {
      this.setDurability(stack, durability);
    }
  }

  protected abstract boolean dropFluidSourceOnBreak();

  // ---------------------------------------------------------------------------
  // - Helpers
  // ---------------------------------------------------------------------------

  private ItemStack createWithFluid(FluidStack fluidStack) {

    Fluid fluid = fluidStack.getFluid();

    if (fluid == null) {
      return new ItemStack(this, 0, EnumType.EMPTY.getMeta());
    }

    if ("milk".equals(fluid.getName())) {
      return new ItemStack(this, 1, EnumType.MILK.getMeta());
    }

    ItemStack stack = new ItemStack(this, 1, 0);
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag("fluids", new FluidStack(fluidStack, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
    stack.setTagCompound(tag);
    return stack;
  }

  @Nullable
  @Override
  public FluidStack getFluid(@Nonnull ItemStack container) {

    if (container.getMetadata() == EnumType.MILK.getMeta()) {
      return FluidRegistry.getFluidStack("milk", Fluid.BUCKET_VOLUME);
    }

    NBTTagCompound tags = container.getTagCompound();

    if (tags != null) {
      return FluidStack.loadFluidStackFromNBT(tags.getCompoundTag("fluids"));
    }

    return null;
  }

  public boolean hasFluid(ItemStack container) {

    return (this.getFluid(container) != null);
  }

  // ---------------------------------------------------------------------------
  // - Fill Bucket
  // ---------------------------------------------------------------------------

  @Override
  public void onFillBucket(FillBucketEvent event) {

    if (event.getResult() != Event.Result.DEFAULT) {
      // event was already handled
      return;
    }

    // not for us to handle
    ItemStack emptyBucket = event.getEmptyBucket();

    if (emptyBucket.isEmpty()
        || !emptyBucket.isItemEqual(this.getEmpty())
        || (this.isNbtSensitive() && ItemStack.areItemStackTagsEqual(emptyBucket, this.getEmpty()))) {
      return;
    }

    // needs to target a block
    RayTraceResult target = event.getTarget();

    if (target == null
        || target.typeOfHit != RayTraceResult.Type.BLOCK) {
      return;
    }

    World world = event.getWorld();
    BlockPos pos = target.getBlockPos();
    ItemStack singleBucket = emptyBucket.copy();
    singleBucket.setCount(1);

    // Grab a fluidStack for later in case the result bucket breaks.
    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, pos, target.sideHit);
    FluidStack fluidStack = null;

    if (fluidHandler != null) {
      IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();

      if (tankProperties != null && tankProperties.length > 0) {
        fluidStack = tankProperties[0].getContents();
      }
    }

    EntityPlayer entityPlayer = event.getEntityPlayer();
    FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, entityPlayer, world, pos, target.sideHit);

    if (filledResult.isSuccess()) {

      ItemStack result = filledResult.getResult();

      // If the result is empty, it means the container has broken.
      // We can just check durability here because the tryPickUpFluid method
      // uses the wrapper, which reduces the durability.
      if (result.isEmpty() || this.isBroken(result)) {
        // Because the result is empty
        if (fluidStack != null) {
          this.tryPlaceFluid(world, entityPlayer, entityPlayer.getPosition(), this.createWithFluid(fluidStack), fluidStack);
        }
        result = this.getBrokenItemStack();
        SoundHelper.playSoundServer(world, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
      }

      event.setResult(Event.Result.ALLOW);
      event.setFilledBucket(result);

    } else {
      // cancel event, otherwise the vanilla minecraft ItemBucket would
      // convert it into a water/lava bucket depending on the blocks material
      event.setCanceled(true);
    }
  }

  // ---------------------------------------------------------------------------
  // - Wrapper
  // ---------------------------------------------------------------------------

  @Override
  public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, NBTTagCompound nbt) {

    return new BucketWrapper(stack);
  }

  private class BucketWrapper
      extends FluidBucketWrapper {

    /* package */ BucketWrapper(@Nonnull ItemStack container) {

      super(container);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {

      return ItemBucketBase.this.getFluid(this.container);
    }

    @Override
    protected void setFluid(@Nullable FluidStack fluidStack) {

      int durability = ItemBucketBase.this.getDurability(this.container);

      if (fluidStack == null) {
        this.container = new ItemStack(ItemBucketBase.this, 1, 0);
        durability -= 1;

      } else {
        this.container = ItemBucketBase.this.createWithFluid(fluidStack);
      }

      ItemBucketBase.this.setDurability(this.container, durability);

      if (ItemBucketBase.this.isBroken(this.container)) {
        this.container = ItemStack.EMPTY;
        ModPyrotech.PROXY.playSound(SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS);
      }
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      if (this.container.getCount() != 1
          || resource == null
          || resource.amount < Fluid.BUCKET_VOLUME
          || this.getFluid() != null
          || !this.canFillFluidType(resource)) {
        return 0;
      }

      if (doFill) {
        this.setFluid(resource);
      }

      return Fluid.BUCKET_VOLUME;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {

      return super.canFillFluidType(fluid);
    }
  }

  // ---------------------------------------------------------------------------
  // - Variants
  // ---------------------------------------------------------------------------

  public enum EnumType
      implements IVariant {

    EMPTY(0, "empty"),
    MILK(1, "milk");

    private static final EnumType[] META_LOOKUP = Stream
        .of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final String name;
    private final int meta;

    EnumType(int meta, String name) {

      this.name = name;
      this.meta = meta;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public ItemStack asStack(ItemBucketBase item) {

      return this.asStack(item, 1);
    }

    public ItemStack asStack(ItemBucketBase item, int amount) {

      return new ItemStack(item, amount, this.meta);
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta > META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }
}
