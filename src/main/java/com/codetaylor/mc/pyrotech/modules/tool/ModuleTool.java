package com.codetaylor.mc.pyrotech.modules.tool;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tool.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tool.init.VanillaFurnaceRecipesAdd;
import com.codetaylor.mc.pyrotech.modules.tool.item.*;
import com.codetaylor.mc.pyrotech.modules.tool.item.spi.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleTool
    extends ModuleBase {

  public static final String MODULE_ID = "module.tool";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public ModuleTool() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    ItemInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    ItemInitializer.onClientRegister(registry);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    VanillaFurnaceRecipesAdd.apply();
  }

  @GameRegistry.ObjectHolder(ModuleTool.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemCrudeAxe.NAME)
    public static final ItemCrudeAxe CRUDE_AXE;

    @GameRegistry.ObjectHolder(ItemCrudeHoe.NAME)
    public static final ItemCrudeHoe CRUDE_HOE;

    @GameRegistry.ObjectHolder(ItemCrudePickaxe.NAME)
    public static final ItemCrudePickaxe CRUDE_PICKAXE;

    @GameRegistry.ObjectHolder(ItemCrudeShovel.NAME)
    public static final ItemCrudeShovel CRUDE_SHOVEL;

    @GameRegistry.ObjectHolder(ItemCrudeFishingRod.NAME)
    public static final ItemCrudeFishingRod CRUDE_FISHING_ROD;

    @GameRegistry.ObjectHolder(ItemAxeBase.NAME_BONE)
    public static final ItemAxeBase BONE_AXE;

    @GameRegistry.ObjectHolder(ItemHoeBase.NAME_BONE)
    public static final ItemHoeBase BONE_HOE;

    @GameRegistry.ObjectHolder(ItemPickaxeBase.NAME_BONE)
    public static final ItemPickaxeBase BONE_PICKAXE;

    @GameRegistry.ObjectHolder(ItemShovelBase.NAME_BONE)
    public static final ItemShovelBase BONE_SHOVEL;

    @GameRegistry.ObjectHolder(ItemSwordBase.NAME_BONE)
    public static final ItemSwordBase BONE_SWORD;

    @GameRegistry.ObjectHolder(ItemAxeBase.NAME_BONE_DURABLE)
    public static final ItemAxeBase BONE_AXE_DURABLE;

    @GameRegistry.ObjectHolder(ItemHoeBase.NAME_BONE_DURABLE)
    public static final ItemHoeBase BONE_HOE_DURABLE;

    @GameRegistry.ObjectHolder(ItemPickaxeBase.NAME_BONE_DURABLE)
    public static final ItemPickaxeBase BONE_PICKAXE_DURABLE;

    @GameRegistry.ObjectHolder(ItemShovelBase.NAME_BONE_DURABLE)
    public static final ItemShovelBase BONE_SHOVEL_DURABLE;

    @GameRegistry.ObjectHolder(ItemAxeBase.NAME_FLINT)
    public static final ItemAxeBase FLINT_AXE;

    @GameRegistry.ObjectHolder(ItemHoeBase.NAME_FLINT)
    public static final ItemHoeBase FLINT_HOE;

    @GameRegistry.ObjectHolder(ItemPickaxeBase.NAME_FLINT)
    public static final ItemPickaxeBase FLINT_PICKAXE;

    @GameRegistry.ObjectHolder(ItemShovelBase.NAME_FLINT)
    public static final ItemShovelBase FLINT_SHOVEL;

    @GameRegistry.ObjectHolder(ItemSwordBase.NAME_FLINT)
    public static final ItemSwordBase FLINT_SWORD;

    @GameRegistry.ObjectHolder(ItemAxeBase.NAME_FLINT_DURABLE)
    public static final ItemAxeBase FLINT_AXE_DURABLE;

    @GameRegistry.ObjectHolder(ItemHoeBase.NAME_FLINT_DURABLE)
    public static final ItemHoeBase FLINT_HOE_DURABLE;

    @GameRegistry.ObjectHolder(ItemPickaxeBase.NAME_FLINT_DURABLE)
    public static final ItemPickaxeBase FLINT_PICKAXE_DURABLE;

    @GameRegistry.ObjectHolder(ItemShovelBase.NAME_FLINT_DURABLE)
    public static final ItemShovelBase FLINT_SHOVEL_DURABLE;

    @GameRegistry.ObjectHolder(ItemAxeBase.NAME_OBSIDIAN)
    public static final ItemAxeBase OBSIDIAN_AXE;

    @GameRegistry.ObjectHolder(ItemHoeBase.NAME_OBSIDIAN)
    public static final ItemHoeBase OBSIDIAN_HOE;

    @GameRegistry.ObjectHolder(ItemPickaxeBase.NAME_OBSIDIAN)
    public static final ItemPickaxeBase OBSIDIAN_PICKAXE;

    @GameRegistry.ObjectHolder(ItemShovelBase.NAME_OBSIDIAN)
    public static final ItemShovelBase OBSIDIAN_SHOVEL;

    @GameRegistry.ObjectHolder(ItemSwordBase.NAME_OBSIDIAN)
    public static final ItemSwordBase OBSIDIAN_SWORD;

    @GameRegistry.ObjectHolder(ItemRedstoneAxe.NAME)
    public static final ItemRedstoneAxe REDSTONE_AXE;

    @GameRegistry.ObjectHolder(ItemRedstoneHoe.NAME)
    public static final ItemRedstoneHoe REDSTONE_HOE;

    @GameRegistry.ObjectHolder(ItemRedstonePickaxe.NAME)
    public static final ItemRedstonePickaxe REDSTONE_PICKAXE;

    @GameRegistry.ObjectHolder(ItemRedstoneShovel.NAME)
    public static final ItemRedstoneShovel REDSTONE_SHOVEL;

    @GameRegistry.ObjectHolder(ItemRedstoneSword.NAME)
    public static final ItemRedstoneSword REDSTONE_SWORD;

    @GameRegistry.ObjectHolder(ItemQuartzAxe.NAME)
    public static final ItemQuartzAxe QUARTZ_AXE;

    @GameRegistry.ObjectHolder(ItemQuartzHoe.NAME)
    public static final ItemQuartzHoe QUARTZ_HOE;

    @GameRegistry.ObjectHolder(ItemQuartzPickaxe.NAME)
    public static final ItemQuartzPickaxe QUARTZ_PICKAXE;

    @GameRegistry.ObjectHolder(ItemQuartzShovel.NAME)
    public static final ItemQuartzShovel QUARTZ_SHOVEL;

    @GameRegistry.ObjectHolder(ItemQuartzSword.NAME)
    public static final ItemQuartzSword QUARTZ_SWORD;

    @GameRegistry.ObjectHolder(ItemUnfiredClayShears.NAME)
    public static final ItemUnfiredClayShears UNFIRED_CLAY_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_CLAY)
    public static final ItemShearsBase CLAY_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_STONE)
    public static final ItemShearsBase STONE_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_BONE)
    public static final ItemShearsBase BONE_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_FLINT)
    public static final ItemShearsBase FLINT_SHEARS;

    @GameRegistry.ObjectHolder(ItemGoldShears.NAME)
    public static final ItemGoldShears GOLD_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_DIAMOND)
    public static final ItemShearsBase DIAMOND_SHEARS;

    @GameRegistry.ObjectHolder(ItemShearsBase.NAME_OBSIDIAN)
    public static final ItemShearsBase OBSIDIAN_SHEARS;

    @GameRegistry.ObjectHolder(ItemToolRepairKit.BONE_NAME)
    public static final ItemToolRepairKit BONE_TOOL_REPAIR_KIT;

    @GameRegistry.ObjectHolder(ItemToolRepairKit.FLINT_NAME)
    public static final ItemToolRepairKit FLINT_TOOL_REPAIR_KIT;

    static {
      CRUDE_AXE = null;
      CRUDE_HOE = null;
      CRUDE_PICKAXE = null;
      CRUDE_SHOVEL = null;
      CRUDE_FISHING_ROD = null;
      BONE_AXE = null;
      BONE_HOE = null;
      BONE_PICKAXE = null;
      BONE_SHOVEL = null;
      BONE_SWORD = null;
      BONE_AXE_DURABLE = null;
      BONE_HOE_DURABLE = null;
      BONE_PICKAXE_DURABLE = null;
      BONE_SHOVEL_DURABLE = null;
      FLINT_AXE = null;
      FLINT_HOE = null;
      FLINT_PICKAXE = null;
      FLINT_SHOVEL = null;
      FLINT_SWORD = null;
      FLINT_AXE_DURABLE = null;
      FLINT_HOE_DURABLE = null;
      FLINT_PICKAXE_DURABLE = null;
      FLINT_SHOVEL_DURABLE = null;
      OBSIDIAN_AXE = null;
      OBSIDIAN_HOE = null;
      OBSIDIAN_PICKAXE = null;
      OBSIDIAN_SHOVEL = null;
      OBSIDIAN_SWORD = null;
      REDSTONE_AXE = null;
      REDSTONE_HOE = null;
      REDSTONE_PICKAXE = null;
      REDSTONE_SHOVEL = null;
      REDSTONE_SWORD = null;
      QUARTZ_AXE = null;
      QUARTZ_HOE = null;
      QUARTZ_PICKAXE = null;
      QUARTZ_SHOVEL = null;
      QUARTZ_SWORD = null;
      UNFIRED_CLAY_SHEARS = null;
      CLAY_SHEARS = null;
      STONE_SHEARS = null;
      BONE_SHEARS = null;
      FLINT_SHEARS = null;
      DIAMOND_SHEARS = null;
      OBSIDIAN_SHEARS = null;
      GOLD_SHEARS = null;
      BONE_TOOL_REPAIR_KIT = null;
      FLINT_TOOL_REPAIR_KIT = null;
    }
  }
}
