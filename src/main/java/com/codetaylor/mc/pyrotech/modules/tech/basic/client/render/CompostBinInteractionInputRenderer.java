package com.codetaylor.mc.pyrotech.modules.tech.basic.client.render;

import com.codetaylor.mc.athenaeum.interaction.api.InteractionRenderers;
import com.codetaylor.mc.athenaeum.interaction.api.Transform;
import com.codetaylor.mc.athenaeum.interaction.spi.IInteractionRenderer;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCompostBin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CompostBinInteractionInputRenderer
    implements IInteractionRenderer<TileCompostBin.InteractionInput> {

  public static final CompostBinInteractionInputRenderer INSTANCE = new CompostBinInteractionInputRenderer();

  @Override
  public void renderSolidPass(TileCompostBin.InteractionInput interaction, World world, RenderItem renderItem, BlockPos pos, IBlockState blockState, float partialTicks) {
    //
  }

  @Override
  public void renderSolidPassText(TileCompostBin.InteractionInput interaction, World world, FontRenderer fontRenderer, int yaw, Vec3d offset, BlockPos pos, IBlockState blockState, float partialTicks) {
    //
  }

  @Override
  public boolean renderAdditivePass(TileCompostBin.InteractionInput interaction, World world, RenderItem renderItem, EnumFacing hitSide, Vec3d hitVec, BlockPos hitPos, IBlockState blockState, ItemStack heldItemMainHand, float partialTicks) {

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
