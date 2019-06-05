package com.codetaylor.mc.pyrotech.library.patreon.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EffectDataList {

  @SerializedName("effects")
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
