package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemPickaxeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemCrudePickaxe
    extends ItemPickaxeBase {

  public static final String NAME = "crude_pickaxe";

  public ItemCrudePickaxe() {

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
  public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {

    return 0;
  }
}
