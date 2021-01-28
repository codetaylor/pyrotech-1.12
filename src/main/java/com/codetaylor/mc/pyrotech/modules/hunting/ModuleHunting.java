package com.codetaylor.mc.pyrotech.modules.hunting;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.hunting.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemHide;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleHunting
    extends ModuleBase {

  public static final String MODULE_ID = "module.hunting";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleHunting() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockCarcass.NAME)
    public static final BlockCarcass CARCASS;

    static {
      CARCASS = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemHide.NAME_COW)
    public static final ItemHide HIDE_COW;

    @GameRegistry.ObjectHolder(ItemHide.NAME_MOOSHROOM)
    public static final ItemHide HIDE_MOOSHROOM;

    @GameRegistry.ObjectHolder(ItemHide.NAME_POLAR_BEAR)
    public static final ItemHide HIDE_POLAR_BEAR;

    @GameRegistry.ObjectHolder(ItemHide.NAME_PIG)
    public static final ItemHide HIDE_PIG;

    @GameRegistry.ObjectHolder(ItemHide.NAME_BAT)
    public static final ItemHide HIDE_BAT;

    @GameRegistry.ObjectHolder(ItemHide.NAME_HORSE)
    public static final ItemHide HIDE_HORSE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_SHEARED)
    public static final ItemHide HIDE_SHEEP_SHEARED;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_YELLOW)
    public static final ItemHide HIDE_SHEEP_YELLOW;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_WHITE)
    public static final ItemHide HIDE_SHEEP_WHITE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_GRAY_LIGHT)
    public static final ItemHide HIDE_SHEEP_GRAY_LIGHT;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_RED)
    public static final ItemHide HIDE_SHEEP_RED;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_PURPLE)
    public static final ItemHide HIDE_SHEEP_PURPLE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_PINK)
    public static final ItemHide HIDE_SHEEP_PINK;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_ORANGE)
    public static final ItemHide HIDE_SHEEP_ORANGE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_MAGENTA)
    public static final ItemHide HIDE_SHEEP_MAGENTA;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_LIME)
    public static final ItemHide HIDE_SHEEP_LIME;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_BLUE_LIGHT)
    public static final ItemHide HIDE_SHEEP_BLUE_LIGHT;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_GREEN)
    public static final ItemHide HIDE_SHEEP_GREEN;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_GRAY)
    public static final ItemHide HIDE_SHEEP_GRAY;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_CYAN)
    public static final ItemHide HIDE_SHEEP_CYAN;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_BROWN)
    public static final ItemHide HIDE_SHEEP_BROWN;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_BLUE)
    public static final ItemHide HIDE_SHEEP_BLUE;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_BLACK)
    public static final ItemHide HIDE_SHEEP_BLACK;

    static {
      HIDE_COW = null;
      HIDE_MOOSHROOM = null;
      HIDE_POLAR_BEAR = null;
      HIDE_PIG = null;
      HIDE_BAT = null;
      HIDE_HORSE = null;
      HIDE_SHEEP_SHEARED = null;
      HIDE_SHEEP_YELLOW = null;
      HIDE_SHEEP_WHITE = null;
      HIDE_SHEEP_GRAY_LIGHT = null;
      HIDE_SHEEP_RED = null;
      HIDE_SHEEP_PURPLE = null;
      HIDE_SHEEP_PINK = null;
      HIDE_SHEEP_ORANGE = null;
      HIDE_SHEEP_MAGENTA = null;
      HIDE_SHEEP_LIME = null;
      HIDE_SHEEP_BLUE_LIGHT = null;
      HIDE_SHEEP_GREEN = null;
      HIDE_SHEEP_GRAY = null;
      HIDE_SHEEP_CYAN = null;
      HIDE_SHEEP_BROWN = null;
      HIDE_SHEEP_BLUE = null;
      HIDE_SHEEP_BLACK = null;
    }
  }

}
