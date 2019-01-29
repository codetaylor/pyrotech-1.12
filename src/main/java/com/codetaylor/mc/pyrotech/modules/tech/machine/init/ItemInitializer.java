package com.codetaylor.mc.pyrotech.modules.tech.machine.init;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.item.ItemMillBlade;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    registry.registerItem(new ItemMillBlade(Item.ToolMaterial.STONE.getMaxUses() / 2), "sawmill_blade_stone");
    registry.registerItem(new ItemMillBlade(EnumMaterial.FLINT.getToolMaterial().getMaxUses()), "sawmill_blade_flint");
    registry.registerItem(new ItemMillBlade(EnumMaterial.BONE.getToolMaterial().getMaxUses()), "sawmill_blade_bone");
    registry.registerItem(new ItemMillBlade(Item.ToolMaterial.IRON.getMaxUses()), "sawmill_blade_iron");
    registry.registerItem(new ItemMillBlade(Item.ToolMaterial.DIAMOND.getMaxUses()), "sawmill_blade_diamond");
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          ModuleTechMachine.Items.STONE_MILL_BLADE,
          ModuleTechMachine.Items.FLINT_MILL_BLADE,
          ModuleTechMachine.Items.BONE_MILL_BLADE,
          ModuleTechMachine.Items.IRON_MILL_BLADE,
          ModuleTechMachine.Items.DIAMOND_MILL_BLADE
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
