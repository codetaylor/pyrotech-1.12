package com.codetaylor.mc.pyrotech.modules.plugin.patchouli.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.ModulePluginPatchouli;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.item.ItemBook;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemBook(), ItemBook.NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModulePluginPatchouli.Items.BOOK
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
