package com.codetaylor.mc.pyrotech.patreon.lib.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class EffectDataJsonAdapter
    extends TypeAdapter<EffectDataBase> {

  private final Map<String, IEffectDataFactory> effectDataFactoryMap;

  public EffectDataJsonAdapter(Map<String, IEffectDataFactory> effectDataFactoryMap) {

    this.effectDataFactoryMap = effectDataFactoryMap;
  }

  @Override
  public void write(JsonWriter out, EffectDataBase value) throws IOException {

    throw new UnsupportedOperationException("Effect data is read only.");
  }

  @Override
  public EffectDataBase read(JsonReader in) throws IOException {

    in.beginObject(); // begin effect object

    EffectDataJsonAdapter.assertNext(in);
    EffectDataJsonAdapter.assertName(in, "uuid");

    String uuidString = in.nextString();
    UUID uuid = UUID.fromString(uuidString);

    EffectDataJsonAdapter.assertNext(in);
    EffectDataJsonAdapter.assertName(in, "effect");

    String effectID = in.nextString();
    IEffectDataFactory effectDataFactory = this.effectDataFactoryMap.get(effectID);

    if (effectDataFactory == null) {
      throw new IOException("No IEffectDataFactory registered for effect");
    }

    EffectDataJsonAdapter.assertNext(in);
    EffectDataJsonAdapter.assertName(in, "params");

    in.beginObject(); // begin params object
    EffectDataBase effectData = effectDataFactory.createEffectData(uuid);
    effectData.read(in);
    in.endObject(); // end params object

    in.endObject(); // end effect object

    return effectData;
  }

  public static void assertName(JsonReader in) throws IOException {

    JsonToken token = in.peek();

    if (!token.equals(JsonToken.NAME)) {
      throw new IOException("Unexpected token: Expected " + JsonToken.NAME + ", found " + token);
    }
  }

  public static void assertName(JsonReader in, String expected) throws IOException {

    EffectDataJsonAdapter.assertName(in);

    String field = in.nextName();

    if (!expected.equals(field)) {
      throw new IOException("Unexpected field: Expected " + expected + ", found " + field);
    }
  }

  public static void assertNext(JsonReader in) throws IOException {

    if (!in.hasNext()) {
      throw new IOException("Effect data missing fields");
    }
  }
}
