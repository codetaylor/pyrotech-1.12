package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.provider;

import com.codetaylor.mc.pyrotech.modules.core.plugin.top.PluginTOP;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate.WorktableProviderDelegate;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorktableProvider
    implements IProbeInfoProvider,
    WorktableProviderDelegate.IWorktableDisplay {

  private final WorktableProviderDelegate delegate;

  private IProbeInfo probeInfo;

  public WorktableProvider() {

    this.delegate = new WorktableProviderDelegate(this);
  }

  @Override
  public String getID() {

    return ModuleTechBasic.MOD_ID + ":" + this.getClass().getName();
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    BlockPos pos = data.getPos();
    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileWorktable) {
      this.probeInfo = probeInfo;
      this.delegate.display((TileWorktable) tileEntity, player);
      this.probeInfo = null;
    }
  }

  @Override
  public void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress) {

    this.probeInfo.horizontal()
        .item(input)
        .progress(progress, maxProgress, new ProgressStyle().height(18).width(64).showText(false))
        .item(output);
  }

  @Override
  public void setRecipeOutputName(ItemStack itemStack) {

    this.probeInfo.itemLabel(itemStack);
  }

  @Override
  public void setCondition(String langKey, String textColorString, String conditionLangKey) {

    this.probeInfo.element(new PluginTOP.ElementTextLocalized(langKey, textColorString, conditionLangKey));
  }

  @Override
  public void setHoveredItem(ItemStack stackInSlot) {

    if (stackInSlot.getCount() == 1) {
      this.probeInfo.itemLabel(stackInSlot);

    } else {
      this.probeInfo.element(new PluginTOP.ElementTextLocalized(WorktableProviderDelegate.LANG_KEY_HOVERED_ITEM_QUANTITY, stackInSlot, stackInSlot.getCount()));
    }
  }
}
