package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemSlag(), ItemSlag.NAME);
    registry.registerItem(new ItemTongsEmptyStone(), ItemTongsEmptyStone.NAME);
    registry.registerItem(new ItemTongsFullStone(), ItemTongsFullStone.NAME, true);
    registry.registerItem(new ItemTongsEmptyFlint(), ItemTongsEmptyFlint.NAME);
    registry.registerItem(new ItemTongsFullFlint(), ItemTongsFullFlint.NAME, true);
    registry.registerItem(new ItemTongsEmptyBone(), ItemTongsEmptyBone.NAME);
    registry.registerItem(new ItemTongsFullBone(), ItemTongsFullBone.NAME, true);
    registry.registerItem(new ItemTongsEmptyIron(), ItemTongsEmptyIron.NAME);
    registry.registerItem(new ItemTongsFullIron(), ItemTongsFullIron.NAME, true);
    registry.registerItem(new ItemTongsEmptyDiamond(), ItemTongsEmptyDiamond.NAME);
    registry.registerItem(new ItemTongsFullDiamond(), ItemTongsFullDiamond.NAME, true);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(
        () -> ModelRegistrationHelper.registerItemModels(
            ModuleBloomery.Items.SLAG,
            ModuleBloomery.Items.TONGS_STONE,
            ModuleBloomery.Items.TONGS_STONE_FULL,
            ModuleBloomery.Items.TONGS_FLINT,
            ModuleBloomery.Items.TONGS_FLINT_FULL,
            ModuleBloomery.Items.TONGS_BONE,
            ModuleBloomery.Items.TONGS_BONE_FULL,
            ModuleBloomery.Items.TONGS_IRON,
            ModuleBloomery.Items.TONGS_IRON_FULL,
            ModuleBloomery.Items.TONGS_DIAMOND,
            ModuleBloomery.Items.TONGS_DIAMOND_FULL
        )
    );
  }

  private ItemInitializer() {
    //
  }
}
