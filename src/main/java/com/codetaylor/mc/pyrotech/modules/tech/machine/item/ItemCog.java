package com.codetaylor.mc.pyrotech.modules.tech.machine.item;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCog
    extends Item {

  private int burnTime;

  public ItemCog(int maxUses) {

    this.setMaxStackSize(1);
    this.setMaxDamage(maxUses);
    this.burnTime = 0;
  }

  public Item setBurnTime(int burnTime) {

    this.burnTime = burnTime;
    return this;
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return this.burnTime;
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    if (stack.getItem() == ModuleTechMachine.Items.GOLD_COG) {
      return ToolMaterial.GOLD.getEnchantability();
    }

    return super.getItemEnchantability(stack);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      Item item = stack.getItem();
      ResourceLocation registryName = item.getRegistryName();

      int transferAmount = ModuleTechMachineConfig.STONE_HOPPER.getCogTransferAmount(registryName);

      if (transferAmount > -1) {
        String unlocalizedName = Item.getItemFromBlock(ModuleTechMachine.Blocks.STONE_HOPPER).getUnlocalizedName() + ".name";
        String localizedName = I18n.translateToLocal(unlocalizedName);
        tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.cog.hopper", localizedName, Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, transferAmount));
      }

      double recipeProgress = ModuleTechMachineConfig.MECHANICAL_COMPACTING_BIN.getCogRecipeProgress(registryName);

      if (recipeProgress > -1) {
        String unlocalizedName = Item.getItemFromBlock(ModuleTechMachine.Blocks.MECHANICAL_COMPACTING_BIN).getUnlocalizedName() + ".name";
        String localizedName = I18n.translateToLocal(unlocalizedName);
        tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.cog.compactor", localizedName, Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, (int) (recipeProgress * 100)));
      }

      int[] cogData = ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.getCogData(registryName, new int[2]);

      if (cogData[0] > -1) {
        int range = cogData[0] * 2 + 1;
        int attempts = cogData[1];
        String unlocalizedName = Item.getItemFromBlock(ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER).getUnlocalizedName() + ".name";
        String localizedName = I18n.translateToLocal(unlocalizedName);
        tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.cog.spreader", localizedName, Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, attempts, range, range));
      }

      int maxDamage = this.getMaxDamage(stack);
      int damage = this.getDamage(stack);

      if (damage == 0) {
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.durability.full", maxDamage));
      }

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }
  }
}
