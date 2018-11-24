package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.waila;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileWoodRack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class WoodRackDataProvider
    implements IWailaDataProvider {

  @Nonnull
  @Override
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

    return ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public List<String> getWailaHead(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    return tooltip;
  }

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    if (!config.getConfig(WailaRegistrar.CONFIG_CONTENTS)) {
      return tooltip;
    }

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileWoodRack) {

      TileWoodRack tile;
      tile = (TileWoodRack) tileEntity;

      ItemStackHandler stackHandler = tile.getStackHandler();
      StringBuilder renderString = new StringBuilder();

      for (int i = 0; i < stackHandler.getSlots(); i++) {
        ItemStack input = stackHandler.getStackInSlot(i);

        if (!input.isEmpty()) {
          renderString.append(WailaUtil.getStackRenderString(input));
        }
      }

      if (renderString.length() > 0) {
        tooltip.add(renderString.toString());
      }

    }

    return tooltip;
  }

  @Nonnull
  @Override
  public List<String> getWailaTail(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    return tooltip;
  }

  @Nonnull
  @Override
  public NBTTagCompound getNBTData(
      EntityPlayerMP player,
      TileEntity tileEntity,
      NBTTagCompound tag,
      World world,
      BlockPos pos
  ) {

    return tag;
  }
}
