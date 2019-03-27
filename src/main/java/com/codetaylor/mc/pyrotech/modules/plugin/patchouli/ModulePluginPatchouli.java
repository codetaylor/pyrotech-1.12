package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.event.PlayerJoinEventHandler;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.init.EntityInitializer;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.plugin.patchouli.item.ItemBook;
import com.codetaylor.mc.pyrotech.packer.PackAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ModulePluginPatchouli
    extends ModuleBase {

  public static final String MODULE_ID = "plugin.patchouli";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  private static final Logger LOGGER = LogManager.getLogger(ModulePluginPatchouli.class);

  public ModulePluginPatchouli() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(new PlayerJoinEventHandler());
  }

  @Override
  public void onClientPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onClientPreInitializationEvent(event);

    String resourcePath = "textures/gui/book/atlas/packed.json";
    ResourceLocation resourceLocation = new ResourceLocation(ModPyrotech.MOD_ID, resourcePath);

    PackAPI.register(resourceLocation, () -> {
      try {
        Minecraft minecraft = Minecraft.getMinecraft();
        IResourceManager resourceManager = minecraft.getResourceManager();
        IResource resource = resourceManager.getResource(resourceLocation);
        return Optional.of(resource.getInputStream());

      } catch (Exception e) {
        LOGGER.error("Error loading packed atlas data: " + resourceLocation, e);
      }
      return Optional.empty();
    });
  }

  @Override
  public void onRegister(Registry registry) {

    ItemInitializer.onRegister(registry);
    EntityInitializer.onRegister(registry);
  }

  @Override
  public void onClientRegister(Registry registry) {

    ItemInitializer.onClientRegister(registry);
  }

  @GameRegistry.ObjectHolder(ModulePluginPatchouli.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemBook.NAME)
    public static final ItemBook BOOK;

    static {
      BOOK = null;
    }
  }
}
