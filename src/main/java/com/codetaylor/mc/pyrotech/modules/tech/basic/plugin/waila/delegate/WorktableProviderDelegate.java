package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.delegate;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.library.Stages;
import com.codetaylor.mc.pyrotech.library.waila.ProviderDelegateBase;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.core.plugin.gamestages.GameStages;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorktableProviderDelegate
    extends ProviderDelegateBase<WorktableProviderDelegate.IWorktableDisplay, TileWorktable> {

  public static final String LANG_KEY_HOVERED_ITEM_QUANTITY = "gui.pyrotech.waila.quantity";

  private List<Item> hammers;

  public WorktableProviderDelegate(IWorktableDisplay display) {

    super(display);
  }

  @Override
  public void display(TileWorktable tile) {

    this.display(tile, null);
  }

  public void display(TileWorktable tile, @Nullable EntityPlayer player) {

    float progress = tile.getRecipeProgress();

    ItemStackHandler stackHandler = tile.getInputStackHandler();
    boolean notEmpty = false;

    for (int i = 0; i < 9; i++) {

      if (!stackHandler.getStackInSlot(i).isEmpty()) {
        notEmpty = true;
        break;
      }
    }

    if (notEmpty) {

      // Display input item and recipe output.
      IRecipe recipe = tile.getRecipe();
      WorktableRecipe worktableRecipe = tile.getWorktableRecipe();
      boolean displayRecipe = false;

      if (recipe != null && worktableRecipe != null) {

        if (Loader.isModLoaded("gamestages")) {
          Stages stages = worktableRecipe.getStages();
          displayRecipe = GameStages.allowed(Minecraft.getMinecraft().player, stages);
        } else {
          displayRecipe = true;
        }
      }

      if (displayRecipe) {
        ItemStack recipeOutput = recipe.getRecipeOutput();

        if (!recipeOutput.isEmpty()) {

          List<Item> toolList;

          if (!worktableRecipe.getToolList().isEmpty()) {
            toolList = worktableRecipe.getToolList();

          } else {

            if (this.hammers == null) {
              this.hammers = new ArrayList<>();

              for (String s : ModuleCoreConfig.HAMMERS.HAMMER_LIST) {
                String[] split = s.split(";");
                Item item = Item.getByNameOrId(split[0]);

                if (item != null) {
                  this.hammers.add(item);
                }
              }
            }

            toolList = this.hammers;
          }

          if (toolList.isEmpty()) {
            this.display.setRecipeProgress(new ItemStack(Blocks.CRAFTING_TABLE), recipeOutput, (int) (100 * progress), 100);

          } else {
            int index = (int) ((tile.getWorld().getTotalWorldTime() / 29) % toolList.size());
            Item item = toolList.get(index);
            this.display.setRecipeProgress(new ItemStack(item), recipeOutput, (int) (100 * progress), 100);
          }
        }

        this.display.setRecipeOutputName(recipeOutput);
      }
    }

    // Add condition

    int remainingDurability = tile.getRemainingDurability();
    int durability = tile.getDurability();

    float d = remainingDurability / (float) durability;

    if (d < 0.25) {
      this.display.setCondition(
          "gui.pyrotech.waila.worktable.condition",
          TextFormatting.RED.toString(),
          "gui.pyrotech.waila.worktable.condition.fractured"
      );

    } else if (d < 0.50) {
      this.display.setCondition(
          "gui.pyrotech.waila.worktable.condition",
          TextFormatting.YELLOW.toString(),
          "gui.pyrotech.waila.worktable.condition.used"
      );

    } else if (d < 0.75) {
      this.display.setCondition(
          "gui.pyrotech.waila.worktable.condition",
          TextFormatting.GOLD.toString(),
          "gui.pyrotech.waila.worktable.condition.fair"
      );

    } else {
      this.display.setCondition(
          "gui.pyrotech.waila.worktable.condition",
          TextFormatting.GREEN.toString(),
          "gui.pyrotech.waila.worktable.condition.good"
      );
    }

    if (player != null) { // Add look-at item
      int distance = 5;
      Vec3d posVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
      RayTraceResult rayTraceResult = tile.getWorld().rayTraceBlocks(posVec, posVec.add(player.getLookVec().scale(distance)), false);

      if (rayTraceResult == null) {
        return;
      }

      if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
        return;
      }

      if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {
        InteractionRayTraceData.List list = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

        for (InteractionRayTraceData data : list) {
          IInteraction interaction = data.getInteraction();

          if (interaction.isEnabled()
              && interaction instanceof IInteractionItemStack) {
            ItemStack stackInSlot = ((IInteractionItemStack) interaction).getStackInSlot();

            if (!stackInSlot.isEmpty()) {
              this.display.setHoveredItem(stackInSlot);
            }
          }
        }
      }
    }
  }

  public interface IWorktableDisplay {

    void setRecipeProgress(ItemStack input, ItemStack output, int progress, int maxProgress);

    void setRecipeOutputName(ItemStack itemStack);

    void setCondition(String langKey, String textColorString, String conditionLangKey);

    void setHoveredItem(ItemStack stackInSlot);
  }
}
