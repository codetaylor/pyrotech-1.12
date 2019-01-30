package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.recipe.CompactingBinRecipe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCompactingBin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CompactingBinInteractionInputRenderer
    implements IInteractionRenderer<TileCompactingBin.InteractionInput> {

  public static final CompactingBinInteractionInputRenderer INSTANCE = new CompactingBinInteractionInputRenderer();

  @Override
  public void renderSolidPass(TileCompactingBin.InteractionInput interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    TileCompactingBin tile = interaction.getTile();
    CompactingBinRecipe currentRecipe = tile.getCurrentRecipe();

    if (currentRecipe == null) {
      return;
    }

    double max = ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY * currentRecipe.getAmount();
    double currentTotal = tile.getInputStackHandler().getTotalItemCount();
    double height = (currentTotal / max) * (13.0 / 16.0) + (1.5 / 16.0);

    Transform transform = new Transform(
        Transform.translate(0.5, height, 0.5),
        Transform.rotate(),
        Transform.scale(12.0 / 16.0, 0.0625 /* 1.0 / 16.0 */, 12.0 / 16.0)
    );

    InteractionRenderers.renderItemModel(renderItem, currentRecipe.getOutput(), transform);
  }

  @Override
  public void renderSolidPassText(TileCompactingBin.InteractionInput interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {

    TileCompactingBin tile = interaction.getTile();
    CompactingBinRecipe currentRecipe = tile.getCurrentRecipe();

    if (!interaction.isEmpty()
        && currentRecipe != null
        && currentRecipe.getAmount() > 0) {

      //int max = ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY * currentRecipe.getAmount();
      int currentTotal = tile.getInputStackHandler().getTotalItemCount();
      int count = currentTotal / currentRecipe.getAmount();

      ItemStack itemStack = interaction.getStackInSlot();
      Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);

      GlStateManager.pushMatrix();
      {
        if (transform != Transform.IDENTITY) {
          GlStateManager.translate(transform.translation.x, transform.translation.y, transform.translation.z);
        }
        InteractionRenderers.renderItemCount(fontRenderer, yaw, count, offset);
      }
      GlStateManager.popMatrix();
    }
  }

  @Override
  public boolean renderAdditivePass(TileCompactingBin.InteractionInput interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    TileCompactingBin tile = interaction.getTile();
    CompactingBinRecipe currentRecipe = tile.getCurrentRecipe();

    if (currentRecipe != null) {
      double max = ModulePyrotechConfig.COMPACTING_BIN.MAX_CAPACITY * currentRecipe.getAmount();
      double currentTotal = tile.getInputStackHandler().getTotalItemCount();

      if (currentTotal == max) {
        return false;
      }
    }

    if (interaction.isItemStackValid(heldItemMainHand)) {
      Transform transform = interaction.getTransform(world, hitPos, blockState, heldItemMainHand, partialTicks);
      InteractionRenderers.setupAdditiveGLState();
      InteractionRenderers.renderItemModelCustom(renderItem, heldItemMainHand, transform);
      InteractionRenderers.cleanupAdditiveGLState();
      return true;
    }

    return false;
  }
}
