package com.codetaylor.mc.pyrotech.library.patreon.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class EffectDataGsonAdapter
    extends TypeAdapter<EffectDataBase> {

  private final Map<String, IEffectDataFactory> effectDataFactoryMap;

  public EffectDataGsonAdapter(Map<String, IEffectDataFactory> effectDataFactoryMap) {

    this.effectDataFactoryMap = effectDataFactoryMap;
  }

  @Override
  public void write(JsonWriter out, EffectDataBase value) throws IOException {

    throw new UnsupportedOperationException("Effect data is read only.");
  }

  @Override
  public EffectDataBase read(JsonReader in) throws IOException {

    in.beginObject(); // begin effect object

    GsonHelper.assertNextName(in, "uuid");

    String uuidString = in.nextString();
    UUID uuid = UUID.fromString(uuidString);

    GsonHelper.assertNextName(in, "effect");

    String effectID = in.nextString();
    IEffectDataFactory effectDataFactory = this.effectDataFactoryMap.get(effectID);

    if (effectDataFactory == null) {
      throw new IOException("No IEffectDataFactory registered for effect");
    }

    GsonHelper.assertNextName(in, "params");

    in.beginObject(); // begin params object
    EffectDataBase effectData = effectDataFactory.createEffectData(uuid);
    effectData.read(in);
    in.endObject(); // end params object

    in.endObject(); // end effect object

    return effectData;
  }

}
