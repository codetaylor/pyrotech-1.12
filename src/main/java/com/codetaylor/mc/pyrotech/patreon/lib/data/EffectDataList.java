package com.codetaylor.mc.pyrotech.patreon.lib.data;

import java.util.List;

public class EffectDataList {

  private List<EffectDataBase> effectDataList;

  public EffectDataList() {
    // serialization
  }

  public EffectDataList(List<EffectDataBase> effectDataList) {

    this.effectDataList = effectDataList;
  }

  public List<EffectDataBase> getEffectDataList() {

    return this.effectDataList;
  }
}
