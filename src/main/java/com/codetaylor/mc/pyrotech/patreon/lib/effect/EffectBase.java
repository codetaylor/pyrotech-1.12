package com.codetaylor.mc.pyrotech.patreon.lib.effect;

import java.util.UUID;

public abstract class EffectBase {

  private final UUID uuid;

  protected EffectBase(UUID uuid) {

    this.uuid = uuid;
  }

  public UUID getUuid() {

    return this.uuid;
  }
}
