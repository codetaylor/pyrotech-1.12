package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tool.item.api.QuartzToolDelegate;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IQuartzTool;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemHoeBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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

public class ItemQuartzHoe
    extends ItemHoeBase
    implements IQuartzTool {

  public static final String NAME = "quartz_hoe";

  public ItemQuartzHoe() {

    super(ModuleCore.Materials.QUARTZ, "quartz");

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "active"),
        (itemStack, world, entity) -> QuartzToolDelegate.isActive(itemStack) ? 1 : 0
    );
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    return repair.getItem() == ModuleCore.Items.MATERIAL
        && repair.getMetadata() == ItemMaterial.EnumType.DENSE_QUARTZ.getMeta();
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (QuartzToolDelegate.isActive(player.getHeldItemMainhand())) {

      for (int x = -1; x <= 1; x++) {
        for (int z = -1; z <= 1; z++) {

          if (!(x == 0 && z == 0)) {
            super.onItemUse(player, world, pos.add(x, 0, z), hand, facing, hitX, hitY, hitZ);
          }
        }
      }
    }

    return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.quartz.active.hoe", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, Reference.Tooltip.COLOR_EXTENDED_INFO, Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, Reference.Tooltip.COLOR_EXTENDED_INFO));

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
