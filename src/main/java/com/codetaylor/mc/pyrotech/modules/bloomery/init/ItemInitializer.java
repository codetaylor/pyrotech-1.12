package com.codetaylor.mc.pyrotech.modules.bloomery.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.bloomery.ModuleBloomery;
import com.codetaylor.mc.pyrotech.modules.bloomery.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ModuleItems {

  public static void onRegister(Registry registry) {

    registry.registerItem(ModuleBloomery.Items.SLAG, ItemSlag.NAME);

    registry.registerItem(ModuleBloomery.Items.TONGS_STONE, ItemTongsEmptyStone.NAME);
    registry.registerItem(ModuleBloomery.Items.TONGS_STONE_FULL, ItemTongsFullStone.NAME, true);
    registry.registerItem(ModuleBloomery.Items.TONGS_FLINT, ItemTongsEmptyFlint.NAME);
    registry.registerItem(ModuleBloomery.Items.TONGS_FLINT_FULL, ItemTongsFullFlint.NAME, true);
    registry.registerItem(ModuleBloomery.Items.TONGS_BONE, ItemTongsEmptyBone.NAME);
    registry.registerItem(ModuleBloomery.Items.TONGS_BONE_FULL, ItemTongsFullBone.NAME, true);
    registry.registerItem(ModuleBloomery.Items.TONGS_IRON, ItemTongsEmptyIron.NAME);
    registry.registerItem(ModuleBloomery.Items.TONGS_IRON_FULL, ItemTongsFullIron.NAME, true);
    registry.registerItem(ModuleBloomery.Items.TONGS_DIAMOND, ItemTongsEmptyDiamond.NAME);
    registry.registerItem(ModuleBloomery.Items.TONGS_DIAMOND_FULL, ItemTongsFullDiamond.NAME, true);
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

  @SideOnly(Side.CLIENT)
  public static void onClientInitialization() {

    // -------------------------------------------------------------------------
    // - Item Colors
    // -------------------------------------------------------------------------

    Minecraft minecraft = Minecraft.getMinecraft();
    ItemColors itemColors = minecraft.getItemColors();

    itemColors.registerItemColorHandler((stack, tintIndex) -> {

      if (tintIndex == 1) {

        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound != null
            && tagCompound.hasKey("color")) {
          return tagCompound.getInteger("color");
        }
      }

      return 0xFFFFFF;
    }, ModuleBloomery.Items.SLAG);
  }

  private ModuleItems() {
    //
  }
}
