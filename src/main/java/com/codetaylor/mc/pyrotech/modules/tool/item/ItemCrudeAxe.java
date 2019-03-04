package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemAxeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCrudeAxe
    extends ItemAxeBase {

  public static final String NAME = "crude_axe";

  public ItemCrudeAxe() {

    super(ToolMaterial.STONE);
    this.setMaxDamage(ToolMaterial.STONE.getMaxUses() / 4);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("crude");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    return super.getDestroySpeed(stack, state) * 0.5f;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {

    if (this.getToolClasses(stack).contains(toolClass)) {
      int harvestLevel = ModuleToolConfig.getHarvestLevel("crude");
      return ((harvestLevel == -1) ? super.getHarvestLevel(stack, toolClass, player, blockState) : harvestLevel);
    }

    return -1;
  }
}
