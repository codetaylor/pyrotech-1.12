package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemPickaxeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlintPickaxe
    extends ItemPickaxeBase {

  public static final String NAME = "flint_pickaxe";

  public ItemFlintPickaxe() {

    super(EnumMaterial.FLINT.getToolMaterial());

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("flint");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {

    if (this.getToolClasses(stack).contains(toolClass)) {
      int harvestLevel = ModuleToolConfig.getHarvestLevel("flint");
      return ((harvestLevel == -1) ? super.getHarvestLevel(stack, toolClass, player, blockState) : harvestLevel);
    }

    return -1;
  }
}
