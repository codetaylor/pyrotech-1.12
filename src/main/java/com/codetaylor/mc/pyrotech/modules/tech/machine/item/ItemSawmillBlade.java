package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSawmillBlade
    extends Item {

  public ItemSawmillBlade(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    if (stack.getItem() == ModuleTechMachine.Items.GOLD_MILL_BLADE) {
      return ToolMaterial.GOLD.getEnchantability();
    }

    return super.getItemEnchantability(stack);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (ModuleTechMachineConfig.isSawbladeIndestructible(stack.getItem())) {
      tooltip.add(I18n.translateToLocal("gui.pyrotech.tooltip.durability.indestructible"));

    } else {
      int damage = this.getDamage(stack);

      if (damage == 0) {
        int maxDamage = this.getMaxDamage(stack);
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", maxDamage));
      }
    }
  }
}
