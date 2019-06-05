package com.codetaylor.mc.pyrotech.library.patreon.data;

import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectBase;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.UUID;

public abstract class EffectDataBase<E extends EffectBase> {

  private UUID playerUuid;

  public EffectDataBase() {
    // serialization
  }

  public EffectDataBase(UUID playerUuid) {

    this.playerUuid = playerUuid;
  }

  public UUID getPlayerUuid() {

    return this.playerUuid;
  }

  public abstract void read(JsonReader in) throws IOException;

  public abstract E createEffect();
}
