package com.codetaylor.mc.pyrotech.modules.tech.machine.block;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.block.spi.BlockSawmillBase;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickSawmill;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileBrickSawmillTop;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockBrickSawmill
    extends BlockSawmillBase {

  public static final String NAME = "brick_sawmill";

  @Override
  protected TileEntity createTileEntityTop() {

    return new TileBrickSawmillTop();
  }

  @Override
  protected TileEntity createTileEntityBottom() {

    return new TileBrickSawmill();
  }

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  @SideOnly(Side.CLIENT)
  @ParametersAreNonnullByDefault
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      int inputSlotSize = ModuleTechMachineConfig.BRICK_SAWMILL.INPUT_SLOT_SIZE;
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.input.capacity.item", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, inputSlotSize));

      int fuelSlotSize = ModuleTechMachineConfig.BRICK_SAWMILL.FUEL_SLOT_SIZE;
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.fuel.capacity.item", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, fuelSlotSize));

      int fuelBurnTimeModifier = (int) (ModuleTechMachineConfig.BRICK_SAWMILL.FUEL_BURN_TIME_MODIFIER * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.machine.fuel.modifier", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, fuelBurnTimeModifier));

      if (ModuleTechMachineConfig.BRICK_SAWMILL.KEEP_HEAT) {
        tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("gui.pyrotech.tooltip.machine.fuel.keep.heat"));
      }

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }
  }
}
