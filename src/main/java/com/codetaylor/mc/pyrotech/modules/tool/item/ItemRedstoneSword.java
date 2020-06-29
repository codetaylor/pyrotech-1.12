package com.codetaylor.mc.pyrotech.modules.tool.item;

import com.codetaylor.mc.pyrotech.Reference;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.api.RedstoneToolDelegate;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.IRedstoneTool;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.ItemSwordBase;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemRedstoneSword
    extends ItemSwordBase
    implements IRedstoneTool {

  public static final String NAME = "redstone_sword";

  public ItemRedstoneSword() {

    super(ModuleCore.Materials.REDSTONE, "redstone");

    this.addPropertyOverride(
        new ResourceLocation(ModuleTechBasic.MOD_ID, "active"),
        (itemStack, world, entity) -> RedstoneToolDelegate.isActive(itemStack) ? 1 : 0
    );
  }

  @Override
  public float getAttackDamage() {

    return (float) (super.getAttackDamage() * Math.max(1, ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_SWORD_DAMAGE_SCALAR));
  }

  @Override
  public float getDestroySpeed(ItemStack itemStack, IBlockState state) {

    return RedstoneToolDelegate.isActive(itemStack) ? (float) (super.getDestroySpeed(itemStack, state) * Math.max(1, ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_HARVEST_SPEED_SCALAR)) : super.getDestroySpeed(itemStack, state);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    return repair.getItem() == ModuleCore.Items.MATERIAL
        && repair.getMetadata() == ItemMaterial.EnumType.DENSE_REDSTONE.getMeta();
  }

  @Override
  public void activateRedstoneTool(ItemStack itemStack) {

    if (!this.isRedstoneToolActive(itemStack)) {
      RedstoneToolDelegate.playActivationSound();
    }

    RedstoneToolDelegate.setActive(itemStack, ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_DURATION_TICKS);
  }

  @Override
  public boolean isRedstoneToolActive(ItemStack itemStack) {

    return RedstoneToolDelegate.isActive(itemStack);
  }

  @Override
  public int getRedstoneToolDamage(ItemStack itemStack) {

    return this.getDamage(itemStack);
  }

  @Override
  public void setRedstoneToolDamage(@Nonnull ItemStack itemStack, int damage) {

    super.setDamage(itemStack, damage);
  }

  @Override
  public void setDamage(@Nonnull ItemStack itemStack, int damage) {

    RedstoneToolDelegate.setDamage(this, itemStack, damage);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      int inactiveActivationChance = (int) (ModuleToolConfig.REDSTONE_TOOLS.INACTIVE_ACTIVATION_CHANCE * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.redstone.activation.chance", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, inactiveActivationChance, Reference.Tooltip.COLOR_EXTENDED_INFO));

      int invertedDamageChance = (int) ((1 - ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_DAMAGE_CHANCE) * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.redstone.active.durability", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, invertedDamageChance, Reference.Tooltip.COLOR_EXTENDED_INFO));

      int swordActiveDamageScalar = (int) (ModuleToolConfig.REDSTONE_TOOLS.ACTIVE_SWORD_DAMAGE_SCALAR * 100);
      tooltip.add(Reference.Tooltip.COLOR_EXTENDED_INFO + I18n.translateToLocalFormatted("gui.pyrotech.tooltip.redstone.active.damage", Reference.Tooltip.COLOR_EXTENDED_INFO_HIGHLIGHT, swordActiveDamageScalar, Reference.Tooltip.COLOR_EXTENDED_INFO));

    } else {
      tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.tooltip.extended.shift", Reference.Tooltip.COLOR_EXTENDED_INFO, TextFormatting.GRAY));
    }

    super.addInformation(stack, world, tooltip, flag);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

    RedstoneToolDelegate.activateNearbyDenseRedstoneOre(itemStack, world, entity);
  }

}
