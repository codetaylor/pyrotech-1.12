package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.library.util.RegistryHelper;
import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockBushBase;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockButchersBlock;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemBlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.tile.TileCarcass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockInitializer {

  public static void onRegister(Registry registry) {

    {
      BlockCarcass block = new BlockCarcass();
      registry.registerBlock(block, new ItemBlockCarcass(block), BlockCarcass.NAME);
    }

    registry.registerBlockWithItem(new BlockButchersBlock(), BlockButchersBlock.NAME);

    RegistryHelper.registerTileEntities(
        registry,
        TileCarcass.class
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerBlockItemModels(
          ModuleHunting.Blocks.CARCASS,
          ModuleHunting.Blocks.BUTCHERS_BLOCK
      );
    });
  }

  private BlockInitializer() {
    //
  }

}
