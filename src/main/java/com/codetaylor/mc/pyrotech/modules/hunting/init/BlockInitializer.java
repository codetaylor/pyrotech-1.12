package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.library.util.RegistryHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileCarcass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    registry.registerBlockWithItem(new BlockCarcass(), BlockCarcass.NAME);

    RegistryHelper.registerTileEntities(
        registry,
        TileCarcass.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleHunting.Blocks.CARCASS
      );
    });
  }

  private BlockInitializer() {
    //
  }

}
