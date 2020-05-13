package com.codetaylor.mc.pyrotech.modules.tech.basic.item;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMarshmallowBurned
    extends ItemMarshmallow {

  public static final String NAME = "marshmallow_burned";

  public ItemMarshmallowBurned() {

    super(ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_HUNGER, (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SATURATION, false);
    this.setMaxStackSize(64);
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_HUNGER;
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return (float) ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SATURATION;
  }

  @Override
  protected int getEffectDurationTicks(World world, long roastedAtTimestamp) {

    return ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.BURNED_MARSHMALLOW_SLOW_DURATION_TICKS;
  }

  @Override
  protected int getMaxEffectDurationTicks() {

    // Not used since we return false from stackEffect().
    return 0;
  }

  @Override
  protected Potion getEffect() {

    return MobEffects.SLOWNESS;
  }

  @Override
  protected boolean stackEffect() {

    return false;
  }

  @Override
  protected void setCooldownOnMarshmallows(EntityPlayer player) {

    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW_STICK_EMPTY, 10);
    player.getCooldownTracker().setCooldown(ModuleTechBasic.Items.MARSHMALLOW, 10);
    player.getCooldownTracker().setCooldown(this, 10);
  }

  @Nonnull
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {

    if (!world.isRemote
        && ModuleTechBasicConfig.CAMPFIRE_MARSHMALLOWS.ENABLE_BURNED_MARSHMALLOW_EAT_BROADCAST_MESSAGE
        && entityLiving instanceof EntityPlayer) {
      MinecraftServer minecraftServer = world.getMinecraftServer();

      if (minecraftServer != null) {
        PlayerList playerList = minecraftServer.getPlayerList();

        for (EntityPlayerMP playerMP : playerList.getPlayers()) {
          playerMP.sendMessage(new TextComponentTranslation("gui.pyrotech.marshmallow.burned.eat.broadcast.message", entityLiving.getName()));
        }
      }
    }

    return super.onItemUseFinish(stack, world, entityLiving);
  }
}
