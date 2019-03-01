package com.codetaylor.mc.pyrotech.modules.ignition.block;

import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnition;
import com.codetaylor.mc.pyrotech.modules.ignition.ModuleIgnitionConfig;
import com.codetaylor.mc.pyrotech.modules.ignition.block.spi.BlockTorchBase;
import com.codetaylor.mc.pyrotech.modules.ignition.tile.TileTorchStone;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTorchStone
    extends BlockTorchBase {

  public static final String NAME = "torch_stone";

  @Override
  public TileEntity createTileEntity() {

    return new TileTorchStone();
  }

  @Override
  protected void getLitDrops(NonNullList<ItemStack> drops) {

    drops.add(new ItemStack(ModuleIgnition.Blocks.TORCH_STONE));
  }

  @Override
  protected int getLightValue() {

    return MathHelper.clamp(ModuleIgnitionConfig.STONE_TORCH.LIGHT_VALUE, 0, 15);
  }

  @Override
  protected int getFireDamage() {

    return MathHelper.clamp(ModuleIgnitionConfig.STONE_TORCH.FIRE_DAMAGE, 0, Integer.MAX_VALUE);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {

    if (ModuleIgnitionConfig.STONE_TORCH.BURNS_UP) {
      tooltip.add(TextFormatting.RED + I18n.translateToLocal("gui.pyrotech.tooltip.torch.burns"));
    }

    if (ModuleIgnitionConfig.STONE_TORCH.EXTINGUISHED_BY_RAIN) {
      tooltip.add(TextFormatting.RED + I18n.translateToLocal("gui.pyrotech.tooltip.rain.extinguishes"));
    }
  }
}
