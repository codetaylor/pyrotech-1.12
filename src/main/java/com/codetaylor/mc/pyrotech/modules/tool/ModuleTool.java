package com.codetaylor.mc.pyrotech.modules.tool;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tool.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.tool.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
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

    @GameRegistry.ObjectHolder(ItemBoneAxe.NAME)
    public static final ItemBoneAxe BONE_AXE;

    @GameRegistry.ObjectHolder(ItemBoneHoe.NAME)
    public static final ItemBoneHoe BONE_HOE;

    @GameRegistry.ObjectHolder(ItemBonePickaxe.NAME)
    public static final ItemBonePickaxe BONE_PICKAXE;

    @GameRegistry.ObjectHolder(ItemBoneShovel.NAME)
    public static final ItemBoneShovel BONE_SHOVEL;

    @GameRegistry.ObjectHolder(ItemBoneSword.NAME)
    public static final ItemBoneSword BONE_SWORD;

    @GameRegistry.ObjectHolder(ItemFlintAxe.NAME)
    public static final ItemFlintAxe FLINT_AXE;

    @GameRegistry.ObjectHolder(ItemFlintHoe.NAME)
    public static final ItemFlintHoe FLINT_HOE;

    @GameRegistry.ObjectHolder(ItemFlintPickaxe.NAME)
    public static final ItemFlintPickaxe FLINT_PICKAXE;

    @GameRegistry.ObjectHolder(ItemFlintShovel.NAME)
    public static final ItemFlintShovel FLINT_SHOVEL;

    @GameRegistry.ObjectHolder(ItemFlintSword.NAME)
    public static final ItemFlintSword FLINT_SWORD;

    static {
      CRUDE_AXE = null;
      CRUDE_HOE = null;
      CRUDE_PICKAXE = null;
      CRUDE_SHOVEL = null;
      BONE_AXE = null;
      BONE_HOE = null;
      BONE_PICKAXE = null;
      BONE_SHOVEL = null;
      BONE_SWORD = null;
      FLINT_AXE = null;
      FLINT_HOE = null;
      FLINT_PICKAXE = null;
      FLINT_SHOVEL = null;
      FLINT_SWORD = null;
    }
  }
}
