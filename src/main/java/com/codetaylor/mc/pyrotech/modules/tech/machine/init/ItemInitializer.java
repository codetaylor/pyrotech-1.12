package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemCog;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemSawmillBlade;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.STONE_DURABILITY), "sawmill_blade_stone");
    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.FLINT_DURABILITY), "sawmill_blade_flint");
    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.BONE_DURABILITY), "sawmill_blade_bone");
    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.IRON_DURABILITY), "sawmill_blade_iron");
    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.DIAMOND_DURABILITY), "sawmill_blade_diamond");
    registry.registerItem(new ItemSawmillBlade(ModuleTechMachineConfig.SAWMILL_BLADES.OBSIDIAN_DURABILITY), "sawmill_blade_obsidian");

    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.WOOD_DURABILITY).setBurnTime(1100), "cog_wood");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.STONE_DURABILITY), "cog_stone");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.FLINT_DURABILITY), "cog_flint");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.BONE_DURABILITY), "cog_bone");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.IRON_DURABILITY), "cog_iron");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.DIAMOND_DURABILITY), "cog_diamond");
    registry.registerItem(new ItemCog(ModuleTechMachineConfig.COGS.OBSIDIAN_DURABILITY), "cog_obsidian");
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleTechMachine.Items.STONE_MILL_BLADE,
          ModuleTechMachine.Items.FLINT_MILL_BLADE,
          ModuleTechMachine.Items.BONE_MILL_BLADE,
          ModuleTechMachine.Items.IRON_MILL_BLADE,
          ModuleTechMachine.Items.DIAMOND_MILL_BLADE,
          ModuleTechMachine.Items.OBSIDIAN_MILL_BLADE,

          ModuleTechMachine.Items.WOOD_COG,
          ModuleTechMachine.Items.STONE_COG,
          ModuleTechMachine.Items.FLINT_COG,
          ModuleTechMachine.Items.BONE_COG,
          ModuleTechMachine.Items.IRON_COG,
          ModuleTechMachine.Items.DIAMOND_COG,
          ModuleTechMachine.Items.OBSIDIAN_COG
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
