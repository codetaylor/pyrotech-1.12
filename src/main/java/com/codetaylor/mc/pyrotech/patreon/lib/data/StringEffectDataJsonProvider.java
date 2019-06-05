package com.codetaylor.mc.pyrotech.patreon.lib.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

public class StringEffectDataJsonProvider
    implements IEffectDataJsonProvider {

  private static final Logger LOGGER = LogManager.getLogger(StringEffectDataJsonProvider.class);

  private final String json;

  public StringEffectDataJsonProvider(String json) {

    this.json = json;
  }

  @Override
  public Optional<Reader> getEffectDataJson() {

    return Optional.of(new StringReader(this.json));
  }
}
