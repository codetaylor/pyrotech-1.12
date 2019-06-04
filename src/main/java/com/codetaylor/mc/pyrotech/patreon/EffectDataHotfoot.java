package com.codetaylor.mc.pyrotech.patreon;

import com.codetaylor.mc.pyrotech.patreon.lib.data.EffectDataBase;
import com.codetaylor.mc.pyrotech.patreon.lib.data.EffectDataJsonAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.UUID;

public class EffectDataHotfoot
    extends EffectDataBase {

  private String color;

  public EffectDataHotfoot(UUID playerUuid) {

    super(playerUuid);
  }

  public String getColor() {

    return this.color;
  }

  @Override
  public void read(JsonReader in) throws IOException {

    EffectDataJsonAdapter.assertNext(in);

    while (in.hasNext()) {
      EffectDataJsonAdapter.assertName(in);
      String name = in.nextName();

      switch (name) {
        case "color":
          this.color = in.nextString();
          break;
        default:
          throw new IOException("Unknown parameter: " + name);
      }
    }

    if (this.color == null) {
      throw new IOException("Missing parameter: color");
    }
  }
}
