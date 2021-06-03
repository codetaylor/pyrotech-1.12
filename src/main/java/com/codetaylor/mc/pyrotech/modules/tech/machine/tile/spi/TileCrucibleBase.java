package com.codetaylor.mc.pyrotech.modules.tech.machine.tile.spi;

import com.codetaylor.mc.athenaeum.interaction.spi.IInteraction;
import com.codetaylor.mc.athenaeum.interaction.spi.InteractionBucketBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.client.render.CrucibleFluidRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.machine.recipe.spi.MachineRecipeItemInFluidOutBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileCrucibleBase<E extends MachineRecipeItemInFluidOutBase<E>>
    extends TileCombustionWorkerStoneItemInFluidOutBase<E> {

  public TileCrucibleBase() {

    this.addInteractions(new IInteraction[]{
        new TileCrucibleBase.InteractionBucket<>(this)
    });
  }

  public static class InteractionBucket<E extends MachineRecipeItemInFluidOutBase<E>>
      extends InteractionBucketBase<TileCrucibleBase<E>> {

    private final TileCrucibleBase<E> tile;

    /* package */ InteractionBucket(TileCrucibleBase<E> tile) {

      super(
          tile.getOutputFluidTank(),
          new EnumFacing[]{EnumFacing.UP},
          tile.getInputInteractionBoundsTop()
      );

      this.tile = tile;
    }

    public FluidTank getFluidTank() {

      return this.tile.getOutputFluidTank();
    }

    @SideOnly(Side.CLIENT)
    public void renderSolidPass(World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

      CrucibleFluidRenderer.INSTANCE.renderSolidPass(this, world, renderItem, pos, blockState, partialTicks);
    }
  }
}
