package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemAxeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBoneAxe
    extends ItemAxeBase {

  public static final String NAME = "bone_axe";

  public ItemBoneAxe() {

    super(EnumMaterial.BONE.getToolMaterial(), 8f, -3.2f);

    Integer maxDamage = ModuleToolConfig.DURABILITY.get("bone");

    if (maxDamage != null) {
      this.setMaxDamage(maxDamage);
    }
  }

  @Override
  public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {

    if (this.getToolClasses(stack).contains(toolClass)) {
      int harvestLevel = ModuleToolConfig.getHarvestLevel("bone");
      return ((harvestLevel == -1) ? super.getHarvestLevel(stack, toolClass, player, blockState) : harvestLevel);
    }

    return -1;
  }
}
