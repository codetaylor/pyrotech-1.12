package com.codetaylor.mc.pyrotech.modules.patreon.data;

import com.codetaylor.mc.pyrotech.library.patreon.data.EffectDataBase;
import com.codetaylor.mc.pyrotech.modules.patreon.effect.EffectHotfoot;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.UUID;

public class EffectDataHotfoot
    extends EffectDataBase<EffectHotfoot> {

  public EffectDataHotfoot(UUID playerUuid) {

    super(playerUuid);
  }

  @Override
  public void read(JsonReader in) throws IOException {

    // Leaving this as an example of reading effect params
    /*
    EffectDataGsonAdapter.assertNext(in);

    while (in.hasNext()) {
      EffectDataGsonAdapter.assertName(in);
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
    */
  }

  @Override
  public EffectHotfoot createEffect() {

    return new EffectHotfoot(this.getPlayerUuid());
  }
}
