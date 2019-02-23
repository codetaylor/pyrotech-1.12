package com.codetaylor.mc.pyrotech.library.crafttweaker;

import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import crafttweaker.IAction;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RemoveAllRecipesAction<V extends IForgeRegistryEntry<V>>
    implements IAction {

  private final IForgeRegistryModifiable<V> registry;
  private final String displayName;

  public RemoveAllRecipesAction(IForgeRegistryModifiable<V> registry, String displayName) {

    this.registry = registry;
    this.displayName = displayName;
  }

  @Override
  public void apply() {

    RecipeHelper.removeAllRecipes(this.registry);
  }

  @Override
  public String describe() {

    return "Removing all recipes for " + this.displayName;
  }
}
