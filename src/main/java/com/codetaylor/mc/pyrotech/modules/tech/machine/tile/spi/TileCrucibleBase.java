package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.CrucibleFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.StoneMachineRecipeItemInFluidOutBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileCrucibleBase<E extends StoneMachineRecipeItemInFluidOutBase<E>>
    extends TileCombustionWorkerStoneItemInFluidOutBase<E> {

  public TileCrucibleBase() {

    this.addInteractions(new IInteraction[]{
        new TileCrucibleBase.InteractionBucket()
    });
  }

  public class InteractionBucket
      extends InteractionBucketBase<TileCrucibleBase> {

    /* package */ InteractionBucket() {

      super(
          TileCrucibleBase.this.getOutputFluidTank(),
          new EnumFacing[]{EnumFacing.UP},
          TileCrucibleBase.this.getInputInteractionBoundsTop()
      );
    }

    public FluidTank getFluidTank() {

      return TileCrucibleBase.this.getOutputFluidTank();
    }

    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CrucibleFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }
}
