package com.codetaylor.mc.pyrotech.library.patreon.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class UrlEffectDataJsonProvider
    implements IEffectDataJsonProvider {

  private static final Logger LOGGER = LogManager.getLogger(UrlEffectDataJsonProvider.class);

  private final String url;

  public UrlEffectDataJsonProvider(String url) {

    this.url = url;
  }

  @Override
  public Optional<Reader> getEffectDataJson() {

    URLConnection connection;

    try {
      connection = new URL(this.url).openConnection();
      InputStream inputStream = connection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      return Optional.of(bufferedReader);

    } catch (Exception e) {
      LOGGER.error("", e);
    }

    return Optional.empty();
  }
}
