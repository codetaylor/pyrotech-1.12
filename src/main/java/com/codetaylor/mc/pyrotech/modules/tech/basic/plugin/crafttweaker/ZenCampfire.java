package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.tools.ZenDocAppend;
import com.codetaylor.mc.athenaeum.tools.ZenDocArg;
import com.codetaylor.mc.athenaeum.tools.ZenDocClass;
import com.codetaylor.mc.athenaeum.tools.ZenDocMethod;
import com.codetaylor.mc.pyrotech.modules.core.plugin.crafttweaker.ZenStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.CampfireRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.pyrotech.Campfire")
@ZenDocAppend({"docs/include/campfire.example.md"})
@ZenClass("mods.pyrotech.Campfire")
public class ZenCampfire {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "unique recipe name"),
          @ZenDocArg(arg = "output", info = "recipe output"),
          @ZenDocArg(arg = "input", info = "recipe input"),
          @ZenDocArg(arg = "ticks", info = "recipe duration in ticks, defaults to config value")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient input, @Optional(valueLong = -1) int ticks) {

    CraftTweaker.LATE_ACTIONS.add(new AddRecipe(
        name,
        CraftTweakerMC.getItemStack(output),
        CraftTweakerMC.getIngredient(input),
        Math.max(1, (ticks == -1) ? ModuleTechBasicConfig.CAMPFIRE.COOK_TIME_TICKS : ticks)
    ));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredients to blacklist")
      }
  )
  @ZenMethod
  public static void blacklistSmeltingRecipes(IIngredient[] output) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (IIngredient ingredient : output) {
          CampfireRecipe.blacklistSmeltingRecipe(CraftTweakerMC.getIngredient(ingredient));
        }
      }

      @Override
      public String describe() {

        return "Blacklisting smelting recipes for campfire: " + CTLogHelper.getStackDescription(output);
      }
    });
  }

  @ZenDocMethod(
      order = 3,
      description = "Blacklist all smelting recipes."
  )
  @ZenMethod
  public static void blacklistAllSmeltingRecipes() {

    CampfireRecipe.blacklistAll();
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredients to whitelist")
      }
  )
  @ZenMethod
  public static void whitelistSmeltingRecipes(IIngredient[] output) {

    CraftTweaker.LATE_ACTIONS.add(new IAction() {

      @Override
      public void apply() {

        for (IIngredient ingredient : output) {
          CampfireRecipe.whitelistSmeltingRecipe(CraftTweakerMC.getIngredient(ingredient));
        }
      }

      @Override
      public String describe() {

        return "Whitelisting smelting recipes for campfire: " + CTLogHelper.getStackDescription(output);
      }
    });
  }

  @ZenDocMethod(
      order = 5,
      args = {
          @ZenDocArg(arg = "output", info = "output ingredient to match")
      },
      description = {
          "Removes pre-existing recipes, ie. recipes added by the mod."
      }
  )
  @ZenMethod
  public static void removeRecipes(IIngredient output) {

    CraftTweaker.LATE_ACTIONS.add(new RemoveRecipe(CraftTweakerMC.getIngredient(output)));
  }

  @ZenDocMethod(
      order = 6,
      args = {
          @ZenDocArg(arg = "fuel")
      },
      description = {
          "Whitelist ingredient for use as fuel in the campfire.",
          "Note: blacklist will take precedence over whitelist.",
          "This means you can whitelist <ore:logWood> and blacklist",
          "<minecraft:log:0> if you wanted."
      }
  )
  @ZenMethod
  public static void whitelistFuel(IIngredient fuel) {

    ModuleTechBasicConfig.CAMPFIRE_FUEL_WHITELIST.add(CTInputHelper.toIngredient(fuel));
  }

  @ZenDocMethod(
      order = 7,
      args = {
          @ZenDocArg(arg = "fuel")
      },
      description = {
          "Blacklist ingredient from use as fuel in the campfire.",
          "Note: blacklist will take precedence over whitelist.",
          "This means you can whitelist an oredict group and blacklist",
          "a single item from it if you wanted."
      }
  )
  @ZenMethod
  public static void blacklistFuel(IIngredient fuel) {

    ModuleTechBasicConfig.CAMPFIRE_FUEL_BLACKLIST.add(CTInputHelper.toIngredient(fuel));
  }

  @ZenDocMethod(
      order = 8,
      args = {
          @ZenDocArg(arg = "stages", info = "game stages")
      },
      description = {
          "Sets game stage logic required to use the device."
      }
  )
  @ZenMethod
  public static void setGameStages(ZenStages stages) {

    ModuleTechBasicConfig.STAGES_CAMPFIRE = stages.getStages();
  }

  public static class RemoveRecipe
      implements IAction {

    private final Ingredient output;

    public RemoveRecipe(Ingredient output) {

      this.output = output;
    }

    @Override
    public void apply() {

      CampfireRecipe.removeRecipes(this.output);
    }

    @Override
    public String describe() {

      return "Removing campfire recipes for " + this.output;
    }
  }

  public static class AddRecipe
      implements IAction {

    private final String name;
    private final ItemStack output;
    private final Ingredient input;
    private final int ticks;

    public AddRecipe(
        String name,
        ItemStack output,
        Ingredient input,
        int ticks
    ) {

      this.name = name;
      this.input = input;
      this.output = output;
      this.ticks = ticks;
    }

    @Override
    public void apply() {

      CampfireRecipe recipe = new CampfireRecipe(
          this.output,
          this.input,
          this.ticks
      );
      ModuleTechBasic.Registries.CAMPFIRE_RECIPE.register(recipe.setRegistryName(new ResourceLocation("crafttweaker", this.name)));
    }

    @Override
    public String describe() {

      return "Adding campfire recipe for " + this.output;
    }
  }

}
