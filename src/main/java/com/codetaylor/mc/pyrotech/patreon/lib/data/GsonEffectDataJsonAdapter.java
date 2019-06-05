package com.codetaylor.mc.pyrotech.patreon.lib.data;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.util.Optional;

public class GsonEffectDataJsonAdapter
    implements IEffectDataJsonAdapter {

  private static final Logger LOGGER = LogManager.getLogger(GsonEffectDataJsonAdapter.class);

  private final Gson gson;

  public GsonEffectDataJsonAdapter(Gson gson) {

    this.gson = gson;
  }

  @Override
  public Optional<EffectDataList> adaptJson(Reader in) {

    try {
      EffectDataList effectDataList = this.gson.fromJson(in, EffectDataList.class);
      return Optional.of(effectDataList);

    } catch (Exception e) {
      LOGGER.error("", e);
    }

    return Optional.empty();
  }
}
