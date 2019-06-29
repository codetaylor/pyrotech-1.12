package com.codetaylor.mc.pyrotech.modules.plugin.patchouli;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.plugin.event.PlayerJoinEventHandler;
import com.codetaylor.mc.pyrotech.packer.PackAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Optional;

public class ModulePluginPatchouli
    extends ModuleBase {

  public static final String MODULE_ID = "plugin.patchouli";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  private static final String TEMPLATE_FOLDER = "patchouli_books/book/en_us/templates/";

  private static final Logger LOGGER = LogManager.getLogger(ModulePluginPatchouli.class);

  public ModulePluginPatchouli() {

    super(0, MOD_ID);

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

    this.registerIncludes(
        "anvil_recipe",
        "bloomery_fuel",
        "compacting_bin_recipe",
        "crude_drying_recipe",
        "drying_recipe",
        "fuel_bloomery",
        "fuel_wither_forge",
        "mechanical_compactor_cog",
        "mechanical_compactor_recipe",
        "mechanical_hopper_cog",
        "mechanical_mulcher_cog",
        "pit_burn_recipe",
        "pit_kiln_recipe",
        "refractory_crucible_recipe",
        "refractory_kiln_recipe",
        "refractory_oven_recipe",
        "refractory_sawmill_recipe",
        "soaking_pot_recipe",
        "stone_crucible_recipe",
        "stone_kiln_recipe",
        "stone_oven_recipe",
        "stone_sawmill_recipe",
        "wither_forge_fuel",
        "worktable_recipe"
    );
  }

  private void registerIncludes(String... names) {

    for (String name : names) {
      this.registerInclude(name);
    }
  }

  private void registerInclude(String name) {

    name = "include/" + name;
    final ResourceLocation internalResourceLocation = new ResourceLocation(ModulePluginPatchouli.MOD_ID, TEMPLATE_FOLDER + name + ".json");
    final ResourceLocation externalResourceLocation = new ResourceLocation(ModulePluginPatchouli.MOD_ID, name);

    PatchouliAPI.instance.registerTemplateAsBuiltin(
        externalResourceLocation,
        () -> {
          try {
            Minecraft minecraft = Minecraft.getMinecraft();
            IResourceManager resourceManager = minecraft.getResourceManager();
            IResource resource = resourceManager.getResource(internalResourceLocation);
            return resource.getInputStream();

          } catch (Exception e) {
            LOGGER.error("Error loading template: " + internalResourceLocation, e);
          }
          return null;
        }
    );
  }
}
