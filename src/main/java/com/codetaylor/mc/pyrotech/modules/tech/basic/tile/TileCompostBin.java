package com.codetaylor.mc.pyrotech.modules.tech.basic.tile;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.*;
import com.codetaylor.mc.athenaeum.inventory.DynamicStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.data.*;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.tile.spi.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.ParticleHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.library.CompostBinRecipeBase;
import com.codetaylor.mc.pyrotech.library.util.Util;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockCompostBin;
import com.codetaylor.mc.pyrotech.modules.tech.basic.client.render.CompostBinInteractionInputRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CompostBinRecipe;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileCompostBin
    extends TileEntityDataBase
    implements ITileInteractable,
    ITickable {

  private static final int UPDATE_INTERVAL_TICKS = 20;
  private int updateIntervalCounter = 0;

  private final List<TileDataFloat> layerProgressList;
  private final InputStackHandler inputStackHandler;
  private final OutputStackHandler outputStackHandler;
  private final TileDataItemStackHandler<?> tileDataInputStackHandler;
  private final TileDataItemStackHandler<?> tileDataOutputStackHandler;
  private final TileDataInteger storedCompostValue;
  private final InputFluidTank inputFluidTank;
  private final TileDataFluidTank<InputFluidTank> tileDataFluidTank;
  private final IInteraction<?>[] interactions;
  private final TileDataItemStack currentRecipeOutput;
  private final IntArrayList layerIndexToOutputIndex;

  private double accumulatedEvaporation;

  public TileCompostBin() {

    super(ModuleTechBasic.TILE_DATA_SERVICE);

    this.layerProgressList = new ArrayList<>(this.getMaximumOutputItemCapacity());
    this.layerIndexToOutputIndex = new IntArrayList(this.getMaximumOutputItemCapacity());

    for (int i = 0; i < this.getLayerCount(); i++) {
      this.layerIndexToOutputIndex.add(i);
    }

    this.inputStackHandler = new InputStackHandler(this);
    this.inputStackHandler.addObserver((handler, slot) -> {
      this.updateRecipeOutput();
      this.checkCompostValueAndDrainTank();
      this.markDirty();
    });

    this.outputStackHandler = new OutputStackHandler(this.getMaximumOutputItemCapacity());
    this.outputStackHandler.addObserver((stackHandler, slotIndex) -> {
      this.updateRecipeOutput();
      this.checkCompostValueAndDrainTank();
      this.markDirty();
    });

    for (int i = 0; i < this.getMaximumOutputItemCapacity(); i++) {
      this.layerProgressList.add(new TileDataFloat(0));
    }

    this.inputFluidTank = new InputFluidTank(1000, this);

    this.currentRecipeOutput = new TileDataItemStack(ItemStack.EMPTY);

    // --- Network ---

    this.tileDataInputStackHandler = new TileDataItemStackHandler<>(this.inputStackHandler);
    this.tileDataOutputStackHandler = new TileDataItemStackHandler<>(this.outputStackHandler);
    this.storedCompostValue = new TileDataInteger(0);
    this.tileDataFluidTank = new TileDataFluidTank<>(this.inputFluidTank);

    List<ITileData> tileDataList = new ArrayList<>(
        Arrays.asList(
            this.tileDataInputStackHandler,
            this.storedCompostValue,
            this.tileDataOutputStackHandler,
            this.tileDataFluidTank,
            this.currentRecipeOutput
        )
    );

    tileDataList.addAll(this.layerProgressList);

    this.registerTileDataForNetwork(tileDataList.toArray(new ITileData[0]));

    // --- Interactions ---

    this.interactions = new IInteraction[]{
        new InteractionInput(this, this.inputStackHandler),
        new InteractionShovel(),
        new InteractionInputFluid(this.inputFluidTank)
    };
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return this.allowAutomation()
        && (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (this.allowAutomation()) {

      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {

        //noinspection unchecked
        return (T) this.inputStackHandler;

      } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

        //noinspection unchecked
        return (T) this.inputFluidTank;
      }
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Network
  // ---------------------------------------------------------------------------

  @Override
  public void onTileDataUpdate() {

    if (this.tileDataOutputStackHandler.isDirty()
        || this.tileDataInputStackHandler.isDirty()
        || this.tileDataFluidTank.isDirty()) {
      BlockHelper.notifyBlockUpdate(this.world, this.pos);
    }
  }

  // ---------------------------------------------------------------------------
  // - Moisture
  // ---------------------------------------------------------------------------

  private void checkCompostValueAndDrainTank() {

    if (!this.world.isRemote && this.getTotalCompostValue() == 0) {
      this.inputFluidTank.drainInternal(Integer.MAX_VALUE, true);
    }
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {

      if (ModuleCoreConfig.CLIENT.SHOW_RECIPE_PROGRESSION_PARTICLES
          && this.getMoistureLevel() > 0
          && this.getLayerCountActive() > 0
          && this.world.getTotalWorldTime() % 20 == 0) {
        ParticleHelper.spawnProgressParticlesClient(1, this.pos.getX() + 0.5, this.pos.getY() + 1.15, this.pos.getZ() + 0.5, 0.5, 0.15, 0.5);
      }

      return;
    }

    this.updateIntervalCounter -= 1;

    if (this.updateIntervalCounter > 0) {
      return;
    }

    this.updateIntervalCounter = UPDATE_INTERVAL_TICKS;

    int totalCompostValue = this.getTotalCompostValue();

    if (totalCompostValue == 0) {
      this.inputFluidTank.drainInternal(Integer.MAX_VALUE, true);

    } else {

      // rain moisture
      if (this.world.isRainingAt(this.pos)) {
        this.inputFluidTank.fill(new FluidStack(FluidRegistry.WATER, UPDATE_INTERVAL_TICKS), true);
      }

      // moisture evaporation
      if (this.getMoistureLevel() > 0) {
        int mB = ModuleTechBasicConfig.COMPOST_BIN.MOISTURE_EVAPORATION_RATE_MILLIBUCKETS_PER_TICK[0];
        int tick = ModuleTechBasicConfig.COMPOST_BIN.MOISTURE_EVAPORATION_RATE_MILLIBUCKETS_PER_TICK[1];
        double moistureEvaporationRate = (mB / (double) tick) * UPDATE_INTERVAL_TICKS;
        this.accumulatedEvaporation += moistureEvaporationRate;

        while (this.accumulatedEvaporation >= 1) {
          this.accumulatedEvaporation -= 1;
          this.inputFluidTank.drainInternal(1, true);
        }

      } else {
        this.accumulatedEvaporation = 0;
      }
    }

    int outputCompostValue = this.getOutputCompostValue();
    int activeLayerCount = this.getLayerCountActive();

    // calculated progress increment value
    float increment = Math.min(1, (1f / ModuleTechBasicConfig.COMPOST_BIN.COMPOST_DURATION_TICKS) * UPDATE_INTERVAL_TICKS);

    // reverse iterate the layers, move any complete layers without an output
    // to the top and reset their progress
    for (int layerIndex = this.getLayerCount() - 1; layerIndex >= 0; layerIndex--) {

      if (this.isLayerProgressComplete(layerIndex)
          && this.isLayerOutputEmpty(layerIndex)) {

        TileDataFloat dataFloat = this.layerProgressList.remove(layerIndex);
        this.layerProgressList.add(dataFloat);
        dataFloat.set(0);

        int outputIndex = this.layerIndexToOutputIndex.removeInt(layerIndex);
        this.layerIndexToOutputIndex.add(outputIndex);

        System.out.println("Reset layer progress for layer at index " + layerIndex);
      }
    }

    for (int layerIndex = 0; layerIndex < this.getLayerCount(); layerIndex++) {
      TileDataFloat layerProgress = this.layerProgressList.get(layerIndex);

      // skip complete layers
      if (this.isLayerProgressComplete(layerIndex)) {
        continue;
      }

      // how many active layers are there above this one, influences progress speed
      int activeLayersAbove = Math.max(0, activeLayerCount - layerIndex - 1);

      // the minimum total compost value to activate this layer
      int requiredCompostValue = this.getLayerRequiredCompostValue(layerIndex);

      if (requiredCompostValue <= totalCompostValue) { // layer is active

        if (this.getMoistureLevel() > 0) {
          float modifier = 1 + (float) (activeLayersAbove * ModuleTechBasicConfig.COMPOST_BIN.ADDITIVE_PERCENTILE_SPEED_MODIFIER_PER_LAYER);
          layerProgress.add(increment * modifier);
        }

        // clamp max progress to 1
        if (layerProgress.get() > 1) {
          layerProgress.set(1);
        }

        // turn items into compost value
        if (this.getStoredCompostValue() + outputCompostValue < requiredCompostValue
            && layerProgress.get() > 0.3) {

          int sanity = 1000;

          while (this.getStoredCompostValue() + outputCompostValue < requiredCompostValue) {
            ItemStack itemStack = this.inputStackHandler.extractItem(false);

            if (itemStack.isEmpty()) {
              break;
            }

            int compostValue = this.getCompostValue(itemStack);
            this.storedCompostValue.add(compostValue);
            System.out.println("Removed item: " + itemStack);
            System.out.println("Added compost value: " + compostValue);
            System.out.println("Stored compost value: " + this.storedCompostValue.get());

            if (--sanity <= 0) {
              // This shouldn't happen, obv.
              throw new RuntimeException("Exceeded safety threshold while converting input to stored compost value");
            }
          }
        }

        // consume stored compost value to generate output items
        if (this.getStoredCompostValue() + outputCompostValue >= requiredCompostValue
            && this.isLayerProgressComplete(layerIndex)) {

          this.layerProgressList.get(layerIndex).set(1);

          if (layerIndex <= this.outputStackHandler.getSlots() && this.isLayerOutputEmpty(layerIndex)) {
            this.storedCompostValue.add(-this.getCompostValueRequiredPerOutputItem());
            System.out.println("Decrementing stored compost value by " + this.getCompostValueRequiredPerOutputItem());
            System.out.println("Stored compost value: " + this.storedCompostValue.get());
            this.outputStackHandler.insertItem(this.getLayerOutputIndex(layerIndex), this.currentRecipeOutput.get().copy(), false);
            System.out.println("Inserting output item in slot " + layerIndex);
          }
        }

      } else { // layer should be inactive
        layerProgress.set(0);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Recipe
  // ---------------------------------------------------------------------------

  /**
   * Sets the current recipe output to empty if the device is completely empty,
   * checking the input, output, and stored compost value.
   */
  private void updateRecipeOutput() {

    if (this.world.isRemote) {
      return;
    }

    ItemStack inputItemStack = this.inputStackHandler.getFirstNonEmptyItemStack();

    if (this.isEmpty()) {
      this.currentRecipeOutput.set(ItemStack.EMPTY);

    } else if (!inputItemStack.isEmpty() && this.currentRecipeOutput.get().isEmpty()) {
      CompostBinRecipeBase<?> recipe = this.getRecipe(inputItemStack);

      if (recipe != null) {
        this.currentRecipeOutput.set(recipe.getOutput().copy());
      }
    }

    System.out.println("Updated recipe output: " + this.currentRecipeOutput);
  }

  /**
   * @return true if any part of the given stack can be inserted
   */
  private boolean isItemValidForInsertion(ItemStack itemStack) {

    CompostBinRecipeBase<?> recipe;

    if (this.currentRecipeOutput.get().isEmpty()) {
      recipe = this.getRecipe(itemStack);

    } else {
      recipe = this.getRecipe(itemStack, this.currentRecipeOutput.get());
    }

    if (recipe == null) {
      return false;
    }

    return (this.getTotalCompostValue() + this.getCompostValue(itemStack) <= this.getMaxCompostValueCapacity());
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public int getLayerCountActive() {

    int totalCompostValue = this.getTotalCompostValue();

    int result = 0;

    for (int layerIndex = 0; layerIndex < this.getLayerCount(); layerIndex++) {
      int requiredCompostValue = this.getLayerRequiredCompostValue(layerIndex);

      if (requiredCompostValue <= totalCompostValue) {
        result += 1;
      }
    }

    return result;
  }

  public int getLayerCount() {

    return this.getMaximumOutputItemCapacity();
  }

  public boolean isLayerProgressComplete(int layerIndex) {

    Preconditions.checkElementIndex(layerIndex, this.getLayerCount());
    return this.layerProgressList.get(layerIndex).get() > 0.9999;
  }

  public int getLayerOutputIndex(int layerIndex) {

    Preconditions.checkElementIndex(layerIndex, this.getLayerCount());
    return this.layerIndexToOutputIndex.getInt(layerIndex);
  }

  public ItemStack getLayerOutputItemStack(int layerIndex) {

    Preconditions.checkElementIndex(layerIndex, this.getLayerCount());
    return this.getOutputStackHandler().getStackInSlot(this.getLayerOutputIndex(layerIndex));
  }

  public boolean isLayerOutputEmpty(int layerIndex) {

    Preconditions.checkElementIndex(layerIndex, this.getLayerCount());
    return this.getLayerOutputItemStack(layerIndex).isEmpty();
  }

  /**
   * @return the minimum required available cv for a layer to be active
   */
  public int getLayerRequiredCompostValue(int layerIndex) {

    Preconditions.checkElementIndex(layerIndex, this.getLayerCount());
    return (layerIndex + 1) * this.getCompostValueRequiredPerOutputItem();
  }

  public boolean isEmpty() {

    return this.inputStackHandler.getTotalItemCount() == 0
        && this.outputStackHandler.getTotalItemCount() == 0
        && this.storedCompostValue.get() == 0;
  }

  public int getStoredCompostValue() {

    return this.storedCompostValue.get();
  }

  public float getMoistureLevel() {

    return this.inputFluidTank.getFluidAmount() / 1000f;
  }

  /**
   * @return input cv + stored cv + output cv
   */
  public int getTotalCompostValue() {

    return this.getInputCompostValue() + this.getStoredCompostValue() + this.getOutputCompostValue();
  }

  public int getOutputCompostValue() {

    if (!this.currentRecipeOutput.get().isEmpty()) {
      // If there is output, we should have a current recipe output
      return (this.outputStackHandler.getTotalItemCount() / this.currentRecipeOutput.get().getCount()) * this.getCompostValueRequiredPerOutputItem();
    }

    return 0;
  }

  public int getInputCompostValue() {

    int result = 0;

    for (int i = 0; i < this.inputStackHandler.getSlots(); i++) {
      ItemStack itemStack = this.inputStackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        CompostBinRecipe recipe;

        if (this.currentRecipeOutput.get().isEmpty()) {
          recipe = CompostBinRecipe.getRecipe(itemStack);

        } else {
          ItemStack output = this.currentRecipeOutput.get();
          recipe = CompostBinRecipe.getRecipe(itemStack, output);
        }

        if (recipe != null) {
          result += recipe.getCompostValue() * itemStack.getCount();
        }
      }
    }

    return result;
  }

  public int getMaxCompostValueCapacity() {

    return this.getCompostValueRequiredPerOutputItem() * this.getMaximumOutputItemCapacity();
  }

  /**
   * @return the single-item compost value for the given stack
   */
  public int getCompostValue(ItemStack itemStack) {

    CompostBinRecipeBase<?> recipe;

    if (this.currentRecipeOutput.get().isEmpty()) {
      recipe = this.getRecipe(itemStack);

    } else {
      recipe = this.getRecipe(itemStack, this.currentRecipeOutput.get());
    }

    if (recipe == null) {
      return 0;
    }

    return recipe.getCompostValue();
  }

  public CompostBinRecipeBase<?> getRecipe(ItemStack itemStack) {

    return CompostBinRecipe.getRecipe(itemStack);
  }

  public CompostBinRecipeBase<?> getRecipe(ItemStack itemStack, ItemStack output) {

    return CompostBinRecipe.getRecipe(itemStack, output);
  }

  public float getMostCompleteActiveLayerProgress() {

    float result = 0;

    List<TileDataFloat> progressList = this.layerProgressList;

    for (int layerIndex = 0; layerIndex < progressList.size(); layerIndex++) {
      TileDataFloat tileDataFloat = progressList.get(layerIndex);

      if (tileDataFloat.get() > result && !this.isLayerProgressComplete(layerIndex)) {
        result = tileDataFloat.get();
      }
    }

    return result;
  }

  public float[] getMostCompleteActiveLayerProgress(float[] result) {

    List<TileDataFloat> progressList = this.layerProgressList;

    for (int layerIndex = 0; layerIndex < progressList.size(); layerIndex++) {

      if (this.isLayerProgressComplete(layerIndex)) {
        continue;
      }

      TileDataFloat progressData = progressList.get(layerIndex);
      float progress = progressData.get();

      for (int i = 0; i < result.length; i++) {

        if (progress > result[i]) {

          if (i != result.length - 1) {

            for (int j = result.length - 1; j > i; j--) {
              result[j] = result[j - 1];
            }
          }
          result[i] = progress;
          break;
        }
      }
    }

    return result;
  }

  public float getLayerProgress(int layer) {

    return this.layerProgressList.get(layer).get();
  }

  public InputStackHandler getInputStackHandler() {

    return this.inputStackHandler;
  }

  public OutputStackHandler getOutputStackHandler() {

    return this.outputStackHandler;
  }

  public ItemStack getCurrentRecipeOutput() {

    return this.currentRecipeOutput.get();
  }

  protected boolean allowAutomation() {

    return ModuleTechBasicConfig.COMPOST_BIN.ALLOW_AUTOMATION;
  }

  protected String[] getShovelBlacklist() {

    return ModuleTechBasicConfig.COMPOST_BIN.SHOVEL_BLACKLIST;
  }

  protected String[] getShovelWhitelist() {

    return ModuleTechBasicConfig.COMPOST_BIN.SHOVEL_WHITELIST;
  }

  protected int getCompostValueRequiredPerOutputItem() {

    return ModuleTechBasicConfig.COMPOST_BIN.COMPOST_VALUE_REQUIRED_PER_OUTPUT_ITEM;
  }

  protected int getMaximumOutputItemCapacity() {

    return ModuleTechBasicConfig.COMPOST_BIN.MAXIMUM_OUTPUT_ITEM_CAPACITY;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("inputStackHandler", this.inputStackHandler.serializeNBT());
    compound.setTag("inputFluidTank", this.inputFluidTank.writeToNBT(new NBTTagCompound()));
    compound.setTag("outputStackHandler", this.outputStackHandler.serializeNBT());
    compound.setInteger("storedCompostValue", this.storedCompostValue.get());
    compound.setTag("currentRecipeOutput", this.currentRecipeOutput.get().serializeNBT());
    compound.setDouble("accumulatedEvaporation", this.accumulatedEvaporation);

    int[] recipeProgress = new int[this.getMaximumOutputItemCapacity()];
    for (int i = 0; i < this.getMaximumOutputItemCapacity(); i++) {
      TileDataFloat data = this.layerProgressList.get(i);
      recipeProgress[i] = Float.floatToIntBits(data.get());
    }
    compound.setIntArray("recipeProgress", recipeProgress);

    compound.setIntArray("layerIndexToOutputIndex", this.layerIndexToOutputIndex.elements());

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.inputStackHandler.deserializeNBT(compound.getCompoundTag("inputStackHandler"));
    this.inputFluidTank.readFromNBT(compound.getCompoundTag("inputFluidTank"));
    this.outputStackHandler.deserializeNBT(compound.getCompoundTag("outputStackHandler"));
    this.storedCompostValue.set(compound.getInteger("storedCompostValue"));
    this.currentRecipeOutput.set(new ItemStack(compound.getCompoundTag("currentRecipeOutput")));
    this.accumulatedEvaporation = compound.getDouble("accumulatedEvaporation");

    int[] intBitArray = compound.getIntArray("recipeProgress");
    int capacity = Math.min(intBitArray.length, this.getMaximumOutputItemCapacity());
    for (int i = 0; i < capacity; i++) {
      TileDataFloat data = this.layerProgressList.get(i);
      data.set(Float.intBitsToFloat(intBitArray[i]));
    }

    this.readIntArray(compound.getIntArray("layerIndexToOutputIndex"), this.layerIndexToOutputIndex);

    this.updateRecipeOutput();
    //BlockHelper.notifyBlockUpdate(this.world, this.pos);
  }

  @Override
  protected void setWorldCreate(World world) {

    this.setWorld(world);
  }

  private IntArrayList readIntArray(int[] intArray, IntArrayList result) {

    for (int i = 0; i < intArray.length; i++) {

      if (i == result.size()) {
        result.add(intArray[i]);

      } else {
        result.set(i, intArray[i]);
      }
    }

    return result;
  }

  private NBTTagIntArray writeIntArrayList(IntArrayList list) {

    return new NBTTagIntArray(list.toIntArray());
  }

  // ---------------------------------------------------------------------------
  // - Rendering
  // ---------------------------------------------------------------------------

  @Override
  public boolean shouldRenderInPass(int pass) {

    return (pass == 0) || (pass == 1);
  }

  // ---------------------------------------------------------------------------
  // - Interactions
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public Stages getStages() {

    return ModuleTechBasicConfig.STAGES_COMPOST_BIN;
  }

  @Override
  public IInteraction[] getInteractions() {

    return this.interactions;
  }

  public static class InteractionInput
      extends InteractionItemStack<TileCompostBin> {

    private final TileCompostBin tile;

    /* package */ InteractionInput(TileCompostBin tile, ItemStackHandler stackHandler) {

      super(new ItemStackHandler[]{stackHandler}, 0, new EnumFacing[]{EnumFacing.UP}, BlockCompostBin.AABB, new Transform(
          Transform.translate(0.5, 1.0, 0.5),
          Transform.rotate(),
          Transform.scale(0.75, 0.75, 0.75)
      ));
      this.tile = tile;
    }

    public TileCompostBin getTile() {

      return this.tile;
    }

    @Override
    protected boolean doItemStackValidation(ItemStack itemStack) {

      return this.tile.isItemValidForInsertion(itemStack);
    }

    @Override
    protected void onInsert(EnumType type, ItemStack itemStack, World world, EntityPlayer player, BlockPos pos) {

      super.onInsert(type, itemStack, world, player, pos);

      if (!world.isRemote
          && type == EnumType.MouseClick) {
        world.playSound(
            null,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundCategory.BLOCKS,
            0.5f,
            (float) (1 + Util.RANDOM.nextGaussian() * 0.4f)
        );
      }
    }

    @Override
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      //
    }

    @Override
    public void renderSolidPassText(World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

      // TODO: maybe custom percentage full?
    }

    @Override
    public boolean renderAdditivePass(World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

      return CompostBinInteractionInputRenderer.INSTANCE.renderAdditivePass(this, world, renderItem, hitSide, hitVec, hitPos, blockState, heldItemMainHand, partialTicks);
    }
  }

  private class InteractionShovel
      extends InteractionUseItemBase<TileCompostBin> {

    /* package */ InteractionShovel() {

      super(new EnumFacing[]{EnumFacing.UP}, BlockCompostBin.AABB);
    }

    @Override
    protected boolean allowInteraction(TileCompostBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (tile.getOutputStackHandler().getTotalItemCount() == 0) {
        return false;
      }

      ItemStack heldItemStack = player.getHeldItem(hand);
      Item heldItem = heldItemStack.getItem();
      ResourceLocation resourceLocation = heldItem.getRegistryName();

      if (resourceLocation == null) {
        return false;
      }

      if (tile.outputStackHandler.extractItemStack(true).isEmpty()) {
        return false;
      }

      String registryName = resourceLocation.toString();

      if (heldItem.getToolClasses(heldItemStack).contains("shovel")) {
        return !ArrayHelper.contains(tile.getShovelBlacklist(), registryName);

      } else {
        return ArrayHelper.contains(tile.getShovelWhitelist(), registryName);
      }
    }

    @Override
    protected int getItemDamage(ItemStack itemStack) {

      return 1;
    }

    @Override
    protected boolean doInteraction(TileCompostBin tile, World world, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing hitSide, float hitX, float hitY, float hitZ) {

      if (!world.isRemote) {
        ItemStack itemStack = tile.outputStackHandler.extractItemStack(false);
        StackHelper.spawnStackOnTop(world, itemStack, hitPos, 1.0);
      }

      return true;
    }
  }

  public static class InteractionInputFluid
      extends InteractionBucketBase<TileSoakingPot> {

    private final FluidTank fluidTank;

    /* package */ InteractionInputFluid(FluidTank fluidTank) {

      super(fluidTank, new EnumFacing[]{EnumFacing.UP}, BlockCompostBin.AABB);
      this.fluidTank = fluidTank;
    }

    public FluidTank getFluidTank() {

      return this.fluidTank;
    }
  }

  // ---------------------------------------------------------------------------
  // - Stack Handlers
  // ---------------------------------------------------------------------------

  public class InputStackHandler
      extends DynamicStackHandler {

    private final TileCompostBin tile;

    /* package */ InputStackHandler(TileCompostBin tile) {

      super(1);
      this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (!this.tile.isItemValidForInsertion(stack)) {
        return stack; // item is not valid for insertion, fail
      }

      CompostBinRecipeBase<?> recipe = this.tile.getRecipe(stack);

      if (recipe == null) {
        // This should never happen because the item's recipe is checked above.
        return stack; // item has no recipe, fail
      }

      int compostValueTotal = this.tile.getTotalCompostValue();
      int compostValueMax = this.tile.getMaxCompostValueCapacity();
      int compostValueItem = this.tile.getCompostValue(stack);
      int compostValueItemTotal = compostValueItem * stack.getCount();

      if (compostValueTotal >= compostValueMax) {
        return stack; // There's no room for insert, fail

      } else if (compostValueTotal + compostValueItemTotal <= compostValueMax) {
        // There's enough room for all items in the stack
        this.insertItem(stack, simulate);
        return ItemStack.EMPTY;

      } else {
        // Trim the input stack down to size and insert
        ItemStack toInsert = stack.copy();
        int emptyCompostValue = (compostValueMax - compostValueTotal);
        int insertCount = emptyCompostValue / compostValueItem;

        if (insertCount > 0) {
          toInsert.setCount(insertCount);
          this.insertItem(toInsert, simulate);
          ItemStack toReturn = stack.copy();
          toReturn.setCount(toReturn.getCount() - insertCount);
          return toReturn;
        }

        return stack; // something went wrong
      }
    }

    public int removeItems(int amount) {

      int remaining = amount;

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          remaining -= super.extractItem(i, remaining, false).getCount();

          if (remaining == 0) {
            return amount;
          }
        }
      }

      return amount - remaining;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          return super.extractItem(i, amount, simulate);
        }
      }

      return ItemStack.EMPTY;
    }

    public ItemStack extractItem(boolean simulate) {

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          return super.extractItem(i, 1, simulate);
        }
      }

      return ItemStack.EMPTY;
    }
  }

  public static class OutputStackHandler
      extends DynamicStackHandler {

    public OutputStackHandler(int initialSize) {

      super(initialSize);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {

      while (this.getSlots() - 1 < slot) {
        this.stacks.add(ItemStack.EMPTY);
      }

      return super.getStackInSlot(slot);
    }

    public ItemStack extractItemStack(boolean simulate) {

      for (int i = this.getSlots() - 1; i >= 0; i--) {

        if (!this.getStackInSlot(i).isEmpty()) {
          return this.extractItem(i, Integer.MAX_VALUE, simulate);
        }
      }

      return ItemStack.EMPTY;
    }
  }

  public static class InputFluidTank
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private final TileCompostBin tile;

    public InputFluidTank(int capacity, TileCompostBin tile) {

      super(capacity);
      this.tile = tile;
    }

    @Override
    public boolean canDrain() {

      return false;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      if (this.tile.getTotalCompostValue() > 0) {
        return super.fill(resource, doFill);
      }

      return 0;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {

      if (super.fillInternal(resource, doFill) > 0) {
        return Fluid.BUCKET_VOLUME;
      }

      return 0;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {

      return (fluid.getFluid() == FluidRegistry.WATER);
    }
  }
}
