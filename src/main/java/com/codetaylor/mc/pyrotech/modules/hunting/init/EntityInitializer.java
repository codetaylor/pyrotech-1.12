package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.hunting.item.EntityItemHideScraped;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public final class EntityInitializer {

  public static void onRegister(Registry registry) {

    registry.createEntityEntry(EntityItemHideScraped.NAME, EntityEntryBuilder.create()
        .entity(EntityItemHideScraped.class)
        .tracker(80, 4, true)
    );
  }

  private EntityInitializer() {
    //
  }
}
