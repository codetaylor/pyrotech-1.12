package com.codetaylor.mc.pyrotech.library.patreon.data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

public final class GsonHelper {

  public static void assertName(JsonReader in) throws IOException {

    JsonToken token = in.peek();

    if (!token.equals(JsonToken.NAME)) {
      throw new IOException("Unexpected token: Expected " + JsonToken.NAME + ", found " + token);
    }
  }

  public static void assertName(JsonReader in, String expected) throws IOException {

    GsonHelper.assertName(in);

    String field = in.nextName();

    if (!expected.equals(field)) {
      throw new IOException("Unexpected field: Expected " + expected + ", found " + field);
    }
  }

  public static void assertNextName(JsonReader in) throws IOException {

    GsonHelper.assertNext(in);
    GsonHelper.assertName(in);
  }

  public static void assertNextName(JsonReader in, String expected) throws IOException {

    GsonHelper.assertNext(in);
    GsonHelper.assertName(in, expected);
  }

  public static void assertNext(JsonReader in) throws IOException {

    if (!in.hasNext()) {
      throw new IOException("Effect data missing fields");
    }
  }

  private GsonHelper() {
    //
  }
}
