package com.codetaylor.mc.pyrotech.modules.storage.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.List;

public class StorageProvider
    extends BodyProviderAdapter {

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

    if (rayTraceResult == null) {
      return tooltip;
    }

    if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
      return tooltip;
    }

    if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {
      InteractionRayTraceData.List list = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

      for (InteractionRayTraceData data : list) {
        IInteraction interaction = data.getInteraction();

        if (interaction.isEnabled()
            && interaction instanceof IInteractionItemStack) {
          ItemStack stackInSlot = ((IInteractionItemStack) interaction).getStackInSlot();

          if (!stackInSlot.isEmpty()) {
            tooltip.add(WailaUtil.getStackRenderString(stackInSlot));
            tooltip.add(stackInSlot.getItem().getItemStackDisplayName(stackInSlot));
          }
        }
      }
    }

    return tooltip;
  }
}
