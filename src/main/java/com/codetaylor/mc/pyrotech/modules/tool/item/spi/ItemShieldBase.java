package com.codetaylor.mc.pyrotech.modules.tool.item.spi;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.IntSupplier;

public abstract class ItemShieldBase
    extends Item {

  protected final IntSupplier durabilitySupplier;

  protected ItemShieldBase(IntSupplier durabilitySupplier) {

    this.durabilitySupplier = durabilitySupplier;

    this.setMaxStackSize(1);

    this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {

      @SideOnly(Side.CLIENT)
      public float apply(@Nonnull ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entity) {

        return (entity != null && entity.isHandActive() && entity.getActiveItemStack() == itemStack) ? 1 : 0;
      }
    });

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
  }

  @Override
  public boolean isShield(@Nonnull ItemStack itemStack, @Nullable EntityLivingBase entity) {

    return (itemStack.getItem() == this);
  }

  @Nonnull
  @Override
  public EnumAction getItemUseAction(@Nonnull ItemStack itemStack) {

    return EnumAction.BLOCK;
  }

  @Override
  public int getMaxItemUseDuration(@Nonnull ItemStack stack) {

    return 72000;
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack itemStack = player.getHeldItem(hand);
    player.setActiveHand(hand);
    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
  }

  @Override
  public int getMaxDamage(@Nonnull ItemStack stack) {

    return this.durabilitySupplier.getAsInt();
  }
}
