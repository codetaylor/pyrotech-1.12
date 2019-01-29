package com.codetaylor.mc.pyrotech.modules.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.bloomery.block.BlockBloom;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public final class EntityInitializer {

  public static void onRegister(Registry registry) {

    registry.createEntityEntry(BlockBloom.ItemBlockBloom.EntityItemBloom.NAME, EntityEntryBuilder.create()
        .entity(BlockBloom.ItemBlockBloom.EntityItemBloom.class)
        .tracker(80, 4, true)
    );
  }

  private EntityInitializer() {
    //
  }
}
