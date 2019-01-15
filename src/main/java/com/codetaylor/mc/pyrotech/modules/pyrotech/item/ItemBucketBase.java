package com.codetaylor.mc.pyrotech.modules.pyrotech.item;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.pyrotech.init.ModuleItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.stream.Stream;

public abstract class ItemBucketBase
    extends UniversalBucket {

  public ItemBucketBase() {

    this.setHasSubtypes(true);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {

    return this.hasFluid(stack) ? 1 : 16;
  }

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

    // TODO: break check
    return super.getContainerItem();
  }

  @Override
  public boolean hasContainerItem(@Nonnull ItemStack stack) {

    // TODO: break check
    return super.hasContainerItem(stack);
  }

  @Override
  public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {

    if (!this.isInCreativeTab(tab)) {
      return;
    }

    subItems.add(new ItemStack(this));

    for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {

      if (!fluid.getName().equals("milk")) {

        // add all fluids that the bucket can be filled  with
        FluidStack fluidStack = new FluidStack(fluid, getCapacity());
        ItemStack stack = new ItemStack(this);
        IFluidHandlerItem fluidHandler = new BucketWrapper(stack);

        if (fluidHandler.fill(fluidStack, true) == fluidStack.amount) {
          ItemStack filled = fluidHandler.getContainer();
          subItems.add(filled);
        }
      }
    }
  }

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
    FluidStack fluidStack = this.getFluid(stack);

    if (stack.getMetadata() == EnumType.MILK.getMeta()) {
      player.setActiveHand(hand);
      return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    RayTraceResult target = this.rayTrace(world, player, !hasFluid(stack));
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

    BlockPos clickPos = target.getBlockPos();

    // can we place liquid there?
    if (world.isBlockModifiable(player, clickPos)) {
      // the block adjacent to the side we clicked on
      BlockPos targetPos = clickPos.offset(target.sideHit);

      // can the player place there?
      if (player.canPlayerEdit(targetPos, target.sideHit, stack)) {
        // try placing liquid
        FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, stack, fluidStack);

        if (result.isSuccess() && !player.capabilities.isCreativeMode) {
          // success!
          //noinspection ConstantConditions
          player.addStat(StatList.getObjectUseStats(this));

          stack.shrink(1);
          ItemStack drained = result.getResult();
          ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);

          // check whether we replace the item or add the empty one to the inventory
          if (stack.isEmpty()) {
            return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);

          } else {
            // add empty bucket to player inventory
            ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
        }
      }
    }

    // couldn't place liquid there
    return ActionResult.newResult(EnumActionResult.FAIL, stack);
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
  public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {

    if (stack.getMetadata() != EnumType.MILK.getMeta()) {
      return stack;
    }

    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer entityPlayer = (EntityPlayer) entityLiving;

      if (!entityPlayer.capabilities.isCreativeMode) {
        stack = new ItemStack(this);
      }

      //noinspection ConstantConditions
      entityPlayer.addStat(StatList.getObjectUseStats(this));
    }

    if (!worldIn.isRemote) {
      entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
    }

    return stack;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {

    if (this.hasFluid(stack)
        || player.capabilities.isCreativeMode) {
      return false;
    }

    if (target instanceof EntityCow) {
      player.playSound(SoundEvents.ENTITY_COW_MILK, 1, 1);

      if (stack.getCount() == 1) {
        stack.setItemDamage(EnumType.MILK.getMeta());

      } else {
        stack.shrink(1);
        ItemHandlerHelper.giveItemToPlayer(player, EnumType.MILK.asStack());
      }

      return true;
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // - Helpers
  // ---------------------------------------------------------------------------

  private ItemStack createWithFluid(Fluid fluid) {

    if ("milk".equals(fluid.getName())) {
      return new ItemStack(this, 1, EnumType.MILK.getMeta());
    }

    ItemStack stack = new ItemStack(this, 1, 0);
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag("fluids", new FluidStack(fluid, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
    stack.setTagCompound(tag);
    return stack;
  }

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

      if (fluidStack == null) {
        // TODO: can check for break here and return empty
        this.container = new ItemStack(ItemBucketBase.this, 1, 0);

      } else {
        this.container = ItemBucketBase.this.createWithFluid(fluidStack.getFluid());
      }
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      if (this.container.getCount() != 1
          || resource == null
          || resource.amount < Fluid.BUCKET_VOLUME
          // || this.container.getItem() instanceof ItemBucketMilk // TODO: check for milk bucket?
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

      // TODO: can bucket hold hot fluids?
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

    private static final ItemMaterial.EnumType[] META_LOOKUP = Stream
        .of(ItemMaterial.EnumType.values())
        .sorted(Comparator.comparing(ItemMaterial.EnumType::getMeta))
        .toArray(ItemMaterial.EnumType[]::new);

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

    public ItemStack asStack() {

      return this.asStack(1);
    }

    public ItemStack asStack(int amount) {

      return new ItemStack(ModuleItems.MATERIAL, amount, this.meta);
    }

    public static ItemMaterial.EnumType fromMeta(int meta) {

      if (meta < 0 || meta > META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }
}
