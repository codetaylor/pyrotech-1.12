package com.codetaylor.mc.pyrotech.modules.tool.init;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.reference.ModuleMaterials;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleTool;
import com.codetaylor.mc.pyrotech.modules.tool.ModuleToolConfig;
import com.codetaylor.mc.pyrotech.modules.tool.item.*;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.*;
import com.google.common.base.Preconditions;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public final class ItemInitializer {

  public static void onRegister(Registry registry) {

    // crude
    registry.registerItem(new ItemCrudeAxe(), ItemCrudeAxe.NAME);
    registry.registerItem(new ItemCrudeHoe(), ItemCrudeHoe.NAME);
    registry.registerItem(new ItemCrudePickaxe(), ItemCrudePickaxe.NAME);
    registry.registerItem(new ItemCrudeShovel(), ItemCrudeShovel.NAME);
    registry.registerItem(new ItemCrudeFishingRod(), ItemCrudeFishingRod.NAME);

    // bone
    {
      Item.ToolMaterial material = EnumMaterial.BONE.getToolMaterial();
      registry.registerItem(new ItemAxeBase(material, 8f, -3.2f, "bone"), ItemAxeBase.NAME_BONE);
      registry.registerItem(new ItemHoeBase(material, "bone"), ItemHoeBase.NAME_BONE);
      registry.registerItem(new ItemPickaxeBase(material, "bone"), ItemPickaxeBase.NAME_BONE);
      registry.registerItem(new ItemShovelBase(material, "bone"), ItemShovelBase.NAME_BONE);
      registry.registerItem(new ItemSwordBase(material, "bone"), ItemSwordBase.NAME_BONE);
    }

    // bone, durable
    {
      Item.ToolMaterial material = EnumMaterial.BONE.getToolMaterial();
      registry.registerItem(new ItemAxeBase(material, 8f, -3.2f, "bone_durable"), ItemAxeBase.NAME_BONE_DURABLE);
      registry.registerItem(new ItemHoeBase(material, "bone_durable"), ItemHoeBase.NAME_BONE_DURABLE);
      registry.registerItem(new ItemPickaxeBase(material, "bone_durable"), ItemPickaxeBase.NAME_BONE_DURABLE);
      registry.registerItem(new ItemShovelBase(material, "bone_durable"), ItemShovelBase.NAME_BONE_DURABLE);
    }

    // flint
    {
      Item.ToolMaterial material = EnumMaterial.FLINT.getToolMaterial();
      registry.registerItem(new ItemAxeBase(material, 8f, -3.2f, "flint"), ItemAxeBase.NAME_FLINT);
      registry.registerItem(new ItemHoeBase(material, "flint"), ItemHoeBase.NAME_FLINT);
      registry.registerItem(new ItemPickaxeBase(material, "flint"), ItemPickaxeBase.NAME_FLINT);
      registry.registerItem(new ItemShovelBase(material, "flint"), ItemShovelBase.NAME_FLINT);
      registry.registerItem(new ItemSwordBase(material, "flint"), ItemSwordBase.NAME_FLINT);
    }

    // flint, durable
    {
      Item.ToolMaterial material = EnumMaterial.FLINT.getToolMaterial();
      registry.registerItem(new ItemAxeBase(material, 8f, -3.2f, "flint_durable"), ItemAxeBase.NAME_FLINT_DURABLE);
      registry.registerItem(new ItemHoeBase(material, "flint_durable"), ItemHoeBase.NAME_FLINT_DURABLE);
      registry.registerItem(new ItemPickaxeBase(material, "flint_durable"), ItemPickaxeBase.NAME_FLINT_DURABLE);
      registry.registerItem(new ItemShovelBase(material, "flint_durable"), ItemShovelBase.NAME_FLINT_DURABLE);
    }

    // redstone
    {
      registry.registerItem(new ItemRedstoneAxe(8f, -2.8f), ItemRedstoneAxe.NAME);
      registry.registerItem(new ItemRedstoneHoe(), ItemRedstoneHoe.NAME);
      registry.registerItem(new ItemRedstonePickaxe(), ItemRedstonePickaxe.NAME);
      registry.registerItem(new ItemRedstoneShovel(), ItemRedstoneShovel.NAME);
      registry.registerItem(new ItemRedstoneSword(), ItemRedstoneSword.NAME);
    }

    // quartz
    {
      registry.registerItem(new ItemQuartzAxe(8f, -2.8f), ItemQuartzAxe.NAME);
      registry.registerItem(new ItemQuartzHoe(), ItemQuartzHoe.NAME);
      registry.registerItem(new ItemQuartzPickaxe(), ItemQuartzPickaxe.NAME);
      registry.registerItem(new ItemQuartzShovel(), ItemQuartzShovel.NAME);
      registry.registerItem(new ItemQuartzSword(), ItemQuartzSword.NAME);
    }

    // obsidian
    {
      Item.ToolMaterial material = Preconditions.checkNotNull(ModuleMaterials.OBSIDIAN);
      registry.registerItem(new ItemAxeBase(material, 8f, -3.0f, "obsidian"), ItemAxeBase.NAME_OBSIDIAN);
      registry.registerItem(new ItemHoeBase(material, "obsidian"), ItemHoeBase.NAME_OBSIDIAN);
      registry.registerItem(new ItemPickaxeBase(material, "obsidian"), ItemPickaxeBase.NAME_OBSIDIAN);
      registry.registerItem(new ItemShovelBase(material, "obsidian"), ItemShovelBase.NAME_OBSIDIAN);
      registry.registerItem(new ItemSwordBase(material, "obsidian"), ItemSwordBase.NAME_OBSIDIAN);
    }

    registry.registerItem(new ItemUnfiredClayShears(), ItemUnfiredClayShears.NAME);
    registry.registerItem(new ItemShearsBase("clay"), ItemShearsBase.NAME_CLAY);
    registry.registerItem(new ItemShearsBase("stone"), ItemShearsBase.NAME_STONE);
    registry.registerItem(new ItemShearsBase("bone"), ItemShearsBase.NAME_BONE);
    registry.registerItem(new ItemShearsBase("flint"), ItemShearsBase.NAME_FLINT);
    registry.registerItem(new ItemGoldShears(), ItemGoldShears.NAME);
    registry.registerItem(new ItemShearsBase("diamond"), ItemShearsBase.NAME_DIAMOND);
    registry.registerItem(new ItemShearsBase("obsidian"), ItemShearsBase.NAME_OBSIDIAN);

    registry.registerItem(new ItemToolRepairKit(ModuleToolConfig.BONE_TOOL_REPAIR_KIT.USES), ItemToolRepairKit.BONE_NAME);
    registry.registerItem(new ItemToolRepairKit(ModuleToolConfig.FLINT_TOOL_REPAIR_KIT.USES), ItemToolRepairKit.FLINT_NAME);
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {

      ModelRegistrationHelper.registerItemModels(
          "tool",
          ModuleTool.Items.CRUDE_AXE,
          ModuleTool.Items.CRUDE_HOE,
          ModuleTool.Items.CRUDE_PICKAXE,
          ModuleTool.Items.CRUDE_SHOVEL,
          ModuleTool.Items.CRUDE_FISHING_ROD,

          ModuleTool.Items.BONE_AXE,
          ModuleTool.Items.BONE_HOE,
          ModuleTool.Items.BONE_PICKAXE,
          ModuleTool.Items.BONE_SHOVEL,
          ModuleTool.Items.BONE_SWORD,

          ModuleTool.Items.BONE_AXE_DURABLE,
          ModuleTool.Items.BONE_HOE_DURABLE,
          ModuleTool.Items.BONE_PICKAXE_DURABLE,
          ModuleTool.Items.BONE_SHOVEL_DURABLE,

          ModuleTool.Items.FLINT_AXE,
          ModuleTool.Items.FLINT_HOE,
          ModuleTool.Items.FLINT_PICKAXE,
          ModuleTool.Items.FLINT_SHOVEL,
          ModuleTool.Items.FLINT_SWORD,

          ModuleTool.Items.FLINT_AXE_DURABLE,
          ModuleTool.Items.FLINT_HOE_DURABLE,
          ModuleTool.Items.FLINT_PICKAXE_DURABLE,
          ModuleTool.Items.FLINT_SHOVEL_DURABLE,

          ModuleTool.Items.OBSIDIAN_AXE,
          ModuleTool.Items.OBSIDIAN_HOE,
          ModuleTool.Items.OBSIDIAN_PICKAXE,
          ModuleTool.Items.OBSIDIAN_SHOVEL,
          ModuleTool.Items.OBSIDIAN_SWORD,

          ModuleTool.Items.REDSTONE_PICKAXE,
          ModuleTool.Items.REDSTONE_HOE,
          ModuleTool.Items.REDSTONE_SHOVEL,
          ModuleTool.Items.REDSTONE_AXE,
          ModuleTool.Items.REDSTONE_SWORD,

          ModuleTool.Items.QUARTZ_PICKAXE,
          ModuleTool.Items.QUARTZ_HOE,
          ModuleTool.Items.QUARTZ_SHOVEL,
          ModuleTool.Items.QUARTZ_AXE,
          ModuleTool.Items.QUARTZ_SWORD,

          ModuleTool.Items.UNFIRED_CLAY_SHEARS,
          ModuleTool.Items.CLAY_SHEARS,
          ModuleTool.Items.STONE_SHEARS,
          ModuleTool.Items.BONE_SHEARS,
          ModuleTool.Items.FLINT_SHEARS,
          ModuleTool.Items.DIAMOND_SHEARS,
          ModuleTool.Items.OBSIDIAN_SHEARS,
          ModuleTool.Items.GOLD_SHEARS,

          ModuleTool.Items.BONE_TOOL_REPAIR_KIT,
          ModuleTool.Items.FLINT_TOOL_REPAIR_KIT
      );
    });
  }

  private ItemInitializer() {
    //
  }
}
