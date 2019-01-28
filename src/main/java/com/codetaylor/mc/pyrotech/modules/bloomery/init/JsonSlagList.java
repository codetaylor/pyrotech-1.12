package com.codetaylor.mc.pyrotech.modules.bloomery.init;

import java.util.ArrayList;
import java.util.List;

public class JsonSlagList {

  private String[] __comments = {
      "WARNING: All changes should be made to the file with the name 'Custom'",
      "in the title. Changes made to the 'Generated' file will be overwritten.",
      "",
      "This file defines properties of the auto-generated slag item and slag block.",
      "Adding new entries will generate new content and removing entries will",
      "remove existing content.",
      "",
      "Properties:",
      "  registryName:",
      "    - unique name used as the suffix for the auto-generated content",
      "    - NOTE: accepts only alphanumeric and underscore characters",
      "  langKey:",
      "    - the lang key used to lookup part of the generated content's display names",
      "  color:",
      "    - the hex color for the generated content's tint"
  };

  private List<JsonSlagListEntry> list = new ArrayList<>();

  public List<JsonSlagListEntry> getList() {

    return this.list;
  }

  public static class JsonSlagListEntry {

    private String registryName;
    private String langKey;
    private String color;

    /* package */ JsonSlagListEntry(String registryName, String langKey, String color) {

      this.registryName = registryName;
      this.langKey = langKey;
      this.color = color;
    }

    public String getRegistryName() {

      return this.registryName;
    }

    public String getLangKey() {

      return this.langKey;
    }

    public String getColor() {

      return this.color;
    }
  }

}
