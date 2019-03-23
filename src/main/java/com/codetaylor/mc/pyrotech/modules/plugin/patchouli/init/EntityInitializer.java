package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.item.ItemBook;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public final class EntityInitializer {

  public static void onRegister(Registry registry) {

    registry.createEntityEntry(ItemBook.EntityItemBook.NAME, EntityEntryBuilder.create()
        .entity(ItemBook.EntityItemBook.class)
        .tracker(80, 4, true)
    );
  }

  private EntityInitializer() {
    //
  }
}
