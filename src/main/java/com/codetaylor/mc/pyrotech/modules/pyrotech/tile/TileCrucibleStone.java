package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.CrucibleFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CrucibleStoneRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileCrucibleStone
    extends TileCombustionWorkerStoneItemInFluidOutBase<CrucibleStoneRecipe> {

  public TileCrucibleStone() {

    this.addInteractions(new IInteraction[]{
        new InteractionBucket()
    });
  }

  @Override
  public CrucibleStoneRecipe getRecipe(ItemStack itemStack) {

    return CrucibleStoneRecipe.getRecipe(itemStack);
  }

  @Override
  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.UP};
  }

  @Override
  protected FluidStack getRecipeOutput(CrucibleStoneRecipe recipe, ItemStack input) {

    FluidStack fluidStack = recipe.getOutput();

    if (this.processAsynchronous()) {
      fluidStack.amount = fluidStack.amount * input.getCount();
    }

    return fluidStack;
  }

  @Override
  protected boolean processAsynchronous() {

    return ModulePyrotechConfig.STONE_CRUCIBLE.ASYNCHRONOUS_OPERATION;
  }

  @Override
  protected int getOutputFluidTankSize() {

    return ModulePyrotechConfig.STONE_CRUCIBLE.OUTPUT_TANK_SIZE;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModulePyrotechConfig.STONE_CRUCIBLE.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModulePyrotechConfig.STONE_CRUCIBLE.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModulePyrotechConfig.STONE_CRUCIBLE.FUEL_SLOT_SIZE;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, CrucibleStoneRecipe recipe) {

    return true;
  }

  @Override
  protected void reduceRecipeTime() {

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    CrucibleStoneRecipe recipe = this.getRecipe(input);

    if (recipe == null) {
      return;
    }

    FluidStack output = recipe.getOutput();
    output.amount *= input.getCount();

    if (this.getOutputFluidTank().fill(output, false) == output.amount) {
      super.reduceRecipeTime();
    }
  }

  public class InteractionBucket
      extends InteractionBucketBase<TileCrucibleStone> {

    /* package */ InteractionBucket() {

      super(
          TileCrucibleStone.this.getOutputFluidTank(),
          new EnumFacing[]{EnumFacing.UP},
          TileCrucibleStone.this.getInputInteractionBoundsTop()
      );
    }

    public FluidTank getFluidTank() {

      return TileCrucibleStone.this.getOutputFluidTank();
    }

    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CrucibleFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }
}
