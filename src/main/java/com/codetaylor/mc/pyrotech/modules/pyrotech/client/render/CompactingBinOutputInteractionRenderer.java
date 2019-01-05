package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.InteractionRenderers;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.api.Transform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCompactingBin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class CompactingBinOutputInteractionRenderer
    implements IInteractionRenderer<TileCompactingBin.InteractionOutput> {

  public static final CompactingBinOutputInteractionRenderer INSTANCE = new CompactingBinOutputInteractionRenderer();

  @Override
  public void renderSolidPass(TileCompactingBin.InteractionOutput interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {

    // If the handler is not empty, render the handler's item.

    if (!interaction.isEmpty()) {
      ItemStack itemStack = interaction.getStackInSlot();
      Transform transform = interaction.getTransform(world, pos, blockState, itemStack, partialTicks);
      InteractionRenderers.renderItemModel(renderItem, itemStack, transform);

    } else {
      TileCompactingBin tile = interaction.getTile();
      ItemStackHandler outputStackHandler = tile.getOutputStackHandler();
      ItemStack outputStack = outputStackHandler.getStackInSlot(1);
      Transform transform = interaction.getTransform(world, pos, blockState, outputStack, partialTicks);
      InteractionRenderers.renderItemModel(renderItem, outputStack, transform);
    }
  }

  @Override
  public void renderSolidPassText(TileCompactingBin.InteractionOutput interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    //
  }

  @Override
  public boolean renderAdditivePass(TileCompactingBin.InteractionOutput interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

    return false;
  }
}
