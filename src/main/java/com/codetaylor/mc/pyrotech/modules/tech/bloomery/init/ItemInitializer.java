package com.codetaylor.mc.pyrotech.modules.tech.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
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
    registry.registerItem(new ItemTongsEmptyGold(), ItemTongsEmptyGold.NAME);
    registry.registerItem(new ItemTongsFullGold(), ItemTongsFullGold.NAME, true);
    registry.registerItem(new ItemTongsEmptyDiamond(), ItemTongsEmptyDiamond.NAME);
    registry.registerItem(new ItemTongsFullDiamond(), ItemTongsFullDiamond.NAME, true);
    registry.registerItem(new ItemTongsEmptyObsidian(), ItemTongsEmptyObsidian.NAME);
    registry.registerItem(new ItemTongsFullObsidian(), ItemTongsFullObsidian.NAME, true);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(
        () -> ModelRegistrationHelper.registerItemModels(
            ModuleTechBloomery.Items.SLAG,
            ModuleTechBloomery.Items.TONGS_STONE,
            ModuleTechBloomery.Items.TONGS_STONE_FULL,
            ModuleTechBloomery.Items.TONGS_FLINT,
            ModuleTechBloomery.Items.TONGS_FLINT_FULL,
            ModuleTechBloomery.Items.TONGS_BONE,
            ModuleTechBloomery.Items.TONGS_BONE_FULL,
            ModuleTechBloomery.Items.TONGS_IRON,
            ModuleTechBloomery.Items.TONGS_IRON_FULL,
            ModuleTechBloomery.Items.TONGS_GOLD,
            ModuleTechBloomery.Items.TONGS_GOLD_FULL,
            ModuleTechBloomery.Items.TONGS_DIAMOND,
            ModuleTechBloomery.Items.TONGS_DIAMOND_FULL,
            ModuleTechBloomery.Items.TONGS_OBSIDIAN,
            ModuleTechBloomery.Items.TONGS_OBSIDIAN_FULL
        )
    );
  }

  private ItemInitializer() {
    //
  }
}
