package com.codetaylor.mc.pyrotech.modules.tech.machine.tile;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.CrucibleFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.StoneCrucibleRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi.TileCombustionWorkerStoneItemInFluidOutBase;
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

public class TileStoneCrucible
    extends TileCombustionWorkerStoneItemInFluidOutBase<StoneCrucibleRecipe> {

  public TileStoneCrucible() {

    this.addInteractions(new IInteraction[]{
        new InteractionBucket()
    });
  }

  @Override
  public StoneCrucibleRecipe getRecipe(ItemStack itemStack) {

    return StoneCrucibleRecipe.getRecipe(itemStack);
  }

  @Override
  protected EnumFacing[] getInputInteractionSides() {

    return new EnumFacing[]{EnumFacing.UP};
  }

  @Override
  protected FluidStack getRecipeOutput(StoneCrucibleRecipe recipe, ItemStack input) {

    FluidStack fluidStack = recipe.getOutput();

    if (this.processAsynchronous()) {
      fluidStack.amount = fluidStack.amount * input.getCount();
    }

    return fluidStack;
  }

  @Override
  protected boolean allowAutomation() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.ALLOW_AUTOMATION;
  }

  @Override
  public boolean processAsynchronous() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.ASYNCHRONOUS_OPERATION;
  }

  @Override
  protected int getOutputFluidTankSize() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.OUTPUT_TANK_SIZE;
  }

  @Override
  protected boolean shouldKeepHeat() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.KEEP_HEAT;
  }

  @Override
  protected int getInputSlotSize() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.INPUT_SLOT_SIZE;
  }

  @Override
  protected int getFuelSlotSize() {

    return ModuleTechMachineConfig.STONE_CRUCIBLE.FUEL_SLOT_SIZE;
  }

  @Override
  public boolean allowInsertInput(ItemStack stack, StoneCrucibleRecipe recipe) {

    return true;
  }

  @Override
  protected void reduceRecipeTime() {

    ItemStack input = this.getInputStackHandler().getStackInSlot(0);
    StoneCrucibleRecipe recipe = this.getRecipe(input);

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
      extends InteractionBucketBase<TileStoneCrucible> {

    /* package */ InteractionBucket() {

      super(
          TileStoneCrucible.this.getOutputFluidTank(),
          new EnumFacing[]{EnumFacing.UP},
          TileStoneCrucible.this.getInputInteractionBoundsTop()
      );
    }

    public FluidTank getFluidTank() {

      return TileStoneCrucible.this.getOutputFluidTank();
    }

    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CrucibleFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }

}
