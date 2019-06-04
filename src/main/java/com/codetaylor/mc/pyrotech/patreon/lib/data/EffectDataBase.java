package com.codetaylor.mc.pyrotech.patreon.lib.data;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.UUID;

public abstract class EffectDataBase {

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
}
