package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.api.QuartzToolDelegate;
import com.codetaylor.mc.pyrotech.modules.tool.item.api.RedstoneToolDelegate;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IQuartzTool;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemShovelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemQuartzShovel
    extends ItemShovelBase
    implements IQuartzTool {

  public static final String NAME = "quartz_shovel";

  public ItemQuartzShovel() {

    super(ModuleCore.Materials.QUARTZ, "quartz");

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "active"),
        (itemStack, world, entity) -> QuartzToolDelegate.isActive(itemStack) ? 1 : 0
    );
  }

  @Override
  public float getDestroySpeed(ItemStack itemStack, IBlockState state) {

    if (QuartzToolDelegate.isActive(itemStack)) {
      // Store the efficiency, then modify it to be used by the call to super.
      // Revert the efficiency when we're done.
      // This ensures that the tool will only buff for blocks it is efficient at breaking.
      float efficiency = this.efficiency;
      this.efficiency *= Math.max(1, ModuleToolConfig.QUARTZ_TOOLS.ACTIVE_HARVEST_SPEED_SCALAR);
      float result = super.getDestroySpeed(itemStack, state);
      this.efficiency = efficiency;
      return result;
    }

    return super.getDestroySpeed(itemStack, state);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    return repair.getItem() == ModuleCore.Items.MATERIAL
        && repair.getMetadata() == ItemMaterial.EnumType.DENSE_QUARTZ.getMeta();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      int harvestSpeedScalar = (int) (ModuleToolConfig.QUARTZ_TOOLS.ACTIVE_HARVEST_SPEED_SCALAR * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.quartz.active.efficiency", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, harvestSpeedScalar, Reference.Tooltip.COLOR_EXTENDED_INFO, Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, Reference.Tooltip.COLOR_EXTENDED_INFO));

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }

    super.addInformation(stack, world, tooltip, flag);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

    QuartzToolDelegate.onUpdate(world, itemStack);
  }

  @Override
  public boolean isQuartzToolActive(ItemStack itemStack) {

    return QuartzToolDelegate.isActive(itemStack);
  }
}
