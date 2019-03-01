package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockCrucibleBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneCrucible;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileStoneCrucibleTop;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockStoneCrucible
    extends BlockCrucibleBase {

  public static final String NAME = "stone_crucible";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileStoneCrucibleTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileStoneCrucible();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      int inputSlotSize = ModuleTechMachineConfig.STONE_CRUCIBLE.INPUT_SLOT_SIZE;
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.input.capacity.item", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, inputSlotSize));

      int outputTankSize = ModuleTechMachineConfig.STONE_CRUCIBLE.OUTPUT_TANK_SIZE;
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.output.capacity.fluid", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, outputTankSize));

      int fuelSlotSize = ModuleTechMachineConfig.STONE_CRUCIBLE.FUEL_SLOT_SIZE;
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.fuel.capacity.item", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, fuelSlotSize));

      int fuelBurnTimeModifier = (int) (ModuleTechMachineConfig.STONE_CRUCIBLE.FUEL_BURN_TIME_MODIFIER * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.fuel.modifier", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, fuelBurnTimeModifier));

      boolean hotFluids = ModuleTechMachineConfig.STONE_CRUCIBLE.HOLDS_HOT_FLUIDS;
      tooltip.add((hotFluids ? TextFormatting.GREEN : TextFormatting.RED) + I18n.translateToLocal("gui.pyrotech.tooltip.hot.fluids." + hotFluids));

      if (ModuleTechMachineConfig.STONE_CRUCIBLE.KEEP_HEAT) {
        tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("gui.pyrotech.tooltip.machine.fuel.keep.heat"));
      }

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }
  }
}
