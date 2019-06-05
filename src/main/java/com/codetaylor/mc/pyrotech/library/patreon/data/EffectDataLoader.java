package com.codetaylor.mc.pyrotech.library.patreon.data;

import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectBase;
import com.codetaylor.mc.pyrotech.library.patreon.effect.EffectContainer;
import net.minecraftforge.common.MinecraftForge;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EffectDataLoader {

  private final IEffectDataJsonProvider jsonProvider;
  private final IEffectDataJsonAdapter jsonAdapter;
  private final String modId;

  public EffectDataLoader(String modId, IEffectDataJsonProvider jsonProvider, IEffectDataJsonAdapter jsonAdapter) {

    this.modId = modId;
    this.jsonProvider = jsonProvider;
    this.jsonAdapter = jsonAdapter;
  }

  public void loadEffects() {

    EffectContainer.LOADING_FINISHED.put(this.modId, false);

    Thread thread = new Thread(() -> {

      // retrieve the json

      Optional<Reader> optionalReader = this.jsonProvider.getEffectDataJson();

      if (!optionalReader.isPresent()) {
        EffectContainer.LOADING_FINISHED.put(this.modId, true);
        return;
      }

      // parse the json string into effect data

      Optional<EffectDataList> optionalEffectDataList = this.jsonAdapter.adaptJson(optionalReader.get());

      if (!optionalEffectDataList.isPresent()) {
        EffectContainer.LOADING_FINISHED.put(this.modId, true);
        return;
      }

      // build effects from the effect data

      EffectDataList effectDataList = optionalEffectDataList.get();
      List<EffectDataBase> list = effectDataList.getEffectDataList();

      for (EffectDataBase effectData : list) {
        EffectBase effect = effectData.createEffect();

        if (effect.subscribeEvents()) {
          MinecraftForge.EVENT_BUS.register(effect);
        }

        EffectContainer.EFFECTS
            .computeIfAbsent(this.modId, s -> new ArrayList<>())
            .add(effect);
        EffectContainer.LOADING_FINISHED.put(this.modId, true);
      }
    });

    thread.setName("EffectDataLoader");
    thread.run();
  }

}
