package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.crafttweaker;

import com.codetaylor.mc.athenaeum.tools.ZenDocExporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportDocumentation {

  public static void main(String[] args) {

    String targetPath = "docs/zs/";
    Class[] classes = {
        ZenBurn.class,
        ZenCampfire.class,
        ZenChoppingBlock.class,
        ZenCompactingBin.class,
        ZenCrucibleStone.class,
        ZenDryingRack.class,
        ZenDryingRackCrude.class,
        ZenGraniteAnvil.class,
        ZenKilnPit.class,
        ZenKilnStone.class,
        ZenMillStone.class,
        ZenOvenStone.class,
        ZenSoakingPot.class,
        ZenWorktable.class
    };

    ZenDocExporter export = new ZenDocExporter();
    Path path = Paths.get(targetPath);

    try {
      Files.createDirectories(path);
      export.export(path, classes);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
