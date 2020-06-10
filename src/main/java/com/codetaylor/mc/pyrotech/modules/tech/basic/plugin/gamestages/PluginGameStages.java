package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.gamestages;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;
import com.codetaylor.mc.athenaeum.integration.gamestages.GameStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.PluginJEI;
import com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.category.JEIRecipeCategoryWorktable;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * Handles adding and removing worktable recipes from JEI on the client when
 * a game stage is added or removed.
 */
public class PluginGameStages {

  public PluginGameStages() {

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageAddedEvent(GameStageEvent.Added event) {

    this.processStagedRecipes();
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageRemovedEvent(GameStageEvent.Removed event) {

    this.processStagedRecipes();
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageClientSyncEvent(StagesSyncedEvent event) {

    this.processStagedRecipes();
  }

  @SideOnly(Side.CLIENT)
  private void processStagedRecipes() {

    if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
      return;
    }

    if (PluginJEI.RECIPE_REGISTRY == null) {
      return;
    }

    EntityPlayerSP player = Minecraft.getMinecraft().player;
    Collection<WorktableRecipe> recipes = ModuleTechBasic.Registries.WORKTABLE_RECIPE.getValuesCollection();

    for (WorktableRecipe recipe : recipes) {
      IRecipeWrapper recipeWrapper = PluginJEI.RECIPE_REGISTRY.getRecipeWrapper(recipe, JEIRecipeCategoryWorktable.UID);

      if (recipeWrapper == null) {
        continue;
      }

      Stages stages = recipe.getStages();

      //noinspection unchecked
      if (GameStages.allowed(player, stages)) {
        PluginJEI.RECIPE_REGISTRY.unhideRecipe(recipeWrapper, JEIRecipeCategoryWorktable.UID);

      } else {
        PluginJEI.RECIPE_REGISTRY.hideRecipe(recipeWrapper, JEIRecipeCategoryWorktable.UID);
      }
    }
  }
}