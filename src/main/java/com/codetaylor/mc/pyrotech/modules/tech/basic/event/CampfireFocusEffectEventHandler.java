package com.codetaylor.mc.pyrotech.modules.tech.basic.event;

import com.codetaylor.mc.athenaeum.util.EnchantmentHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.CapabilityFocused;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.FocusedPlayerData;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.IFocusedPlayerData;
import com.codetaylor.mc.pyrotech.modules.tech.basic.network.SCPacketCapabilitySyncFocused;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CampfireFocusEffectEventHandler {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void on(PlayerPickupXpEvent event) {

    if (!ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_EFFECT_ENABLED) {
      return;
    }

    EntityPlayer player = event.getEntityPlayer();

    if (player.world.isRemote
        || player.xpCooldown > 0
        || !player.getActivePotionMap().containsKey(ModuleTechBasic.Potions.FOCUSED)) {
      return;
    }

    EntityXPOrb orb = event.getOrb();

    if (orb.delayBeforeCanPickup > 0) {
      return;
    }

    IFocusedPlayerData data = CapabilityFocused.get(player);
    int xpValue = orb.getXpValue();
    int experienceToNextLevel = EnchantmentHelper.getExperienceToNextLevel(player.experienceLevel);

    int additionalXP = (int) (xpValue * Math.max(0, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_BONUS));
    xpValue = xpValue + additionalXP;
    orb.xpValue = xpValue;

    double toReduceBy = xpValue / (double) experienceToNextLevel;
    double remainingBonus = data.getRemainingBonus() - toReduceBy;

    if (remainingBonus > 0) {
      data.setRemainingBonus(remainingBonus);

    } else {
      player.removePotionEffect(ModuleTechBasic.Potions.FOCUSED);
    }

    if (ModuleTechBasicConfig.CAMPFIRE_EFFECTS.DEBUG) {

    }
  }

  @SubscribeEvent
  public void on(TickEvent.PlayerTickEvent event) {

    if (!ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_EFFECT_ENABLED) {
      return;
    }

    if (event.player.world.isRemote || event.phase != TickEvent.Phase.START) {
      return;
    }

    if (event.player.getActivePotionMap().containsKey(ModuleTechBasic.Potions.FOCUSED)) {
      IFocusedPlayerData data = event.player.getCapability(CapabilityFocused.FOCUSED, null);

      if (data != null && data.isDirty()) {
        data.setDirty(false);
        ModuleTechBasic.PACKET_SERVICE.sendTo(new SCPacketCapabilitySyncFocused(data), (EntityPlayerMP) event.player);
      }
    }
  }

  @SubscribeEvent
  public void on(AttachCapabilitiesEvent<Entity> event) {

    Entity entity = event.getObject();

    if (entity instanceof EntityPlayer && !(entity instanceof FakePlayer)) {
      event.addCapability(new ResourceLocation(ModuleTechBasic.MOD_ID, "focused"), new Provider());
    }
  }

  private static class Provider
      implements ICapabilitySerializable<NBTTagCompound> {

    private FocusedPlayerData data;

    public Provider() {

      this.data = new FocusedPlayerData();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

      return (capability == CapabilityFocused.FOCUSED);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

      if (capability == CapabilityFocused.FOCUSED) {
        //noinspection unchecked
        return (T) this.data;
      }

      return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {

      return (NBTTagCompound) this.data.writeNBT(CapabilityFocused.FOCUSED, this.data, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

      this.data.readNBT(CapabilityFocused.FOCUSED, this.data, null, nbt);
    }
  }
}
