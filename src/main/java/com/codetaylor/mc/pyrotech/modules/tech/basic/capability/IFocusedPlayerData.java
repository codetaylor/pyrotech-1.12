package com.codetaylor.mc.pyrotech.modules.tech.basic.capability;

public interface IFocusedPlayerData {

  double getRemainingBonus();

  void setRemainingBonus(double remainingBonus);

  boolean isDirty();

  void setDirty(boolean dirty);
}
