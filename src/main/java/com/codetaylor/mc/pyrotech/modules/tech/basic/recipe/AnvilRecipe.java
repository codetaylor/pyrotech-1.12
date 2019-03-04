package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class AnvilRecipe
    extends IForgeRegistryEntry.Impl<AnvilRecipe>
    implements IRecipeSingleOutput {

  public enum EnumTier {
    GRANITE, IRONCLAD
  }

  public enum EnumType {
    HAMMER, PICKAXE
  }

  @Nullable
  public static AnvilRecipe getRecipe(ItemStack input, EnumTier tier) {

    for (AnvilRecipe recipe : ModuleTechBasic.Registries.ANVIL_RECIPE) {

      if (recipe.matches(input, tier)) {
        return recipe;
      }
    }

    return null;
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModuleTechBasic.Registries.ANVIL_RECIPE, output);
  }

  private final Ingredient input;
  private final ItemStack output;
  private final int hits;
  private final EnumType type;
  private final EnumTier[] tiers;

  public AnvilRecipe(
      ItemStack output,
      Ingredient input,
      int hits,
      EnumType type,
      EnumTier tier
  ) {

    this(
        output,
        input,
        hits,
        type,
        new EnumTier[]{tier}
    );
  }

  public AnvilRecipe(
      ItemStack output,
      Ingredient input,
      int hits,
      EnumType type,
      EnumTier[] tiers
  ) {

    this.input = input;
    this.output = output;
    this.hits = hits;
    this.type = type;
    this.tiers = tiers;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getHits() {

    return this.hits;
  }

  public EnumType getType() {

    return this.type;
  }

  public boolean isTier(EnumTier tier) {

    return ArrayHelper.contains(this.tiers, tier);
  }

  public boolean matches(ItemStack input, EnumTier tier) {

    return this.isTier(tier) && (this.input.apply(input));
  }

  public interface IExtendedRecipe<T extends AnvilRecipe & IExtendedRecipe> {

    void applyDamage(World world, TileAnvilBase tile);

    float getModifiedRecipeProgressIncrement(float increment, TileAnvilBase tile, EntityPlayer player);

    void onRecipeCompleted(TileAnvilBase tile, World world, ItemStackHandler stackHandler, T recipe, EntityPlayer player);

    void onAnvilHitClient(World world, TileAnvilBase tile, float hitX, float hitY, float hitZ);

    /**
     * @return true to allow this recipe to be inherited by higher tier anvils
     */
    default boolean allowInheritance() {

      return false;
    }
  }
}
