package com.codetaylor.mc.pyrotech.modules.tech.basic.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasicConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class AnvilRecipe
    extends IForgeRegistryEntry.Impl<AnvilRecipe>
    implements IRecipeSingleOutput {

  public enum EnumTier {
    GRANITE, IRONCLAD, OBSIDIAN
  }

  public enum EnumType {
    HAMMER, PICKAXE
  }

  public static boolean hasRecipe(ItemStack input, EnumTier tier) {

    for (AnvilRecipe recipe : ModuleTechBasic.Registries.ANVIL_RECIPE) {

      if (recipe.matches(input, tier, null)) {
        return true;
      }
    }

    return false;
  }

  @Nullable
  public static AnvilRecipe getRecipe(ItemStack input, EnumTier tier, @Nullable AnvilRecipe.EnumType type) {

    if (type == null) {
      return null;
    }

    for (AnvilRecipe recipe : ModuleTechBasic.Registries.ANVIL_RECIPE) {

      if (recipe.matches(input, tier, type)) {
        return recipe;
      }
    }

    return null;
  }

  @Nullable
  public static EnumType getTypeFromItemStack(ItemStack itemStack) {

    /*
      if explicitly declared in hammer config, is hammer
      else if tool is pickaxe
        if tool is not explicitly blacklisted as pickaxe, is pickaxe
      else if tool is explicitly whitelisted as pickaxe, is pickaxe
      else, is neither
     */

    Item item = itemStack.getItem();
    ResourceLocation resourceLocation = item.getRegistryName();

    if (resourceLocation == null) {
      return null;
    }

    if (ModuleTechBasicConfig.ANVIL_COMMON.getHammerHitReduction(resourceLocation) > -1) {
      // held item is hammer
      return AnvilRecipe.EnumType.HAMMER;

    } else if (item.getToolClasses(itemStack).contains("pickaxe")) {
      // held item is pickaxe
      if (!ArrayHelper.contains(ModuleTechBasicConfig.ANVIL_COMMON.PICKAXE_BLACKLIST, resourceLocation.toString())) {
        return AnvilRecipe.EnumType.PICKAXE;
      }

    } else if (ArrayHelper.contains(ModuleTechBasicConfig.ANVIL_COMMON.PICKAXE_WHITELIST, resourceLocation.toString())) {
      // held item is pickaxe
      return AnvilRecipe.EnumType.PICKAXE;
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

  public boolean matches(ItemStack input, EnumTier tier, EnumType type) {

    return this.isTier(tier) && (this.input.apply(input)) && (type == null || this.type == type);
  }

  public interface IExtendedRecipe<T extends AnvilRecipe & IExtendedRecipe> {

    void applyDamage(World world, TileAnvilBase tile);

    float getModifiedRecipeProgressIncrement(float increment, TileAnvilBase tile, EntityPlayer player);

    void onRecipeCompleted(TileAnvilBase tile, World world, ItemStackHandler stackHandler, T recipe, EntityPlayer player);

    void onAnvilHitClient(World world, TileAnvilBase tile, float hitX, float hitY, float hitZ);

    void onAnvilDurabilityExpired(World world, TileAnvilBase tile, float hitX, float hitY, float hitZ);

    /**
     * @return true to allow this recipe to be inherited by higher tier anvils
     */
    default boolean allowInheritance() {

      return false;
    }
  }
}
