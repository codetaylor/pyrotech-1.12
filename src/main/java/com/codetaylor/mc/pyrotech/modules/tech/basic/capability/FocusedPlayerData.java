package com.codetaylor.mc.pyrotech.modules.tech.basic.capability;

import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class FocusedPlayerData
    implements IFocusedPlayerData,
    Capability.IStorage<IFocusedPlayerData> {

  private double remainingBonus;
  private boolean dirty;

  @Override
  public double getRemainingBonus() {

    return this.remainingBonus;
  }

  @Override
  public void setRemainingBonus(double remainingBonus) {

    this.remainingBonus = Math.min(remainingBonus, ModuleTechBasicConfig.CAMPFIRE_EFFECTS.FOCUSED_MAXIMUM_ACCUMULATED_BONUS);
    this.setDirty(true);
  }

  @Override
  public boolean isDirty() {

    return this.dirty;
  }

  @Override
  public void setDirty(boolean dirty) {

    this.dirty = dirty;
  }

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<IFocusedPlayerData> capability, IFocusedPlayerData instance, EnumFacing side) {

    NBTTagCompound tag = new NBTTagCompound();
    tag.setDouble("remainingBonus", instance.getRemainingBonus());
    return tag;
  }

  @Override
  public void readNBT(Capability<IFocusedPlayerData> capability, IFocusedPlayerData instance, EnumFacing side, NBTBase nbt) {

    instance.setRemainingBonus(((NBTTagCompound) nbt).getDouble("remainingBonus"));
  }
}
