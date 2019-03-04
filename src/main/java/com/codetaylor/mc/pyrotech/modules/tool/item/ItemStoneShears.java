package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShearsBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStoneShears
    extends ItemShearsBase {

  public static final String NAME = "stone_shears";

  public ItemStoneShears() {

    this.setMaxDamage(ModuleToolConfig.SHEARS_DURABILITY.get("stone"));
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {

    int harvestLevel = ModuleToolConfig.getHarvestLevel("obsidian");
    return ((harvestLevel == -1) ? super.getHarvestLevel(stack, toolClass, player, blockState) : harvestLevel);
  }
}
