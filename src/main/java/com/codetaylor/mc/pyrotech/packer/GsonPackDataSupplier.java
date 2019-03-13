package com.codetaylor.mc.pyrotech.packer;

import com.google.gson.Gson;

import java.io.Reader;
import java.util.function.Supplier;

public class GsonPackDataSupplier
    implements Supplier<PackData> {

  private final Gson gson;
  private final Reader reader;

  public GsonPackDataSupplier(Gson gson, Reader reader) {

    this.gson = gson;
    this.reader = reader;
  }

  @Override
  public PackData get() {

    return this.gson.fromJson(this.reader, PackData.class);
  }
}
