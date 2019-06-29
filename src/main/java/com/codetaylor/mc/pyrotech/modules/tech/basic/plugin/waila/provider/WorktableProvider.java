package com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.provider;

import com.codetaylor.mc.pyrotech.interaction.spi.IInteraction;
import com.codetaylor.mc.pyrotech.interaction.spi.IInteractionItemStack;
import com.codetaylor.mc.pyrotech.interaction.util.InteractionRayTraceData;
import com.codetaylor.mc.pyrotech.library.spi.plugin.waila.BodyProviderAdapter;
import com.codetaylor.mc.pyrotech.library.util.plugin.waila.WailaUtil;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCoreConfig;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.WorktableRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileWorktable;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WorktableProvider
    extends BodyProviderAdapter {

  private String craftingTableRenderString;
  private List<Item> hammers;

  @Nonnull
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config
  ) {

    TileEntity tileEntity = accessor.getTileEntity();

    if (tileEntity instanceof TileWorktable) {

      TileWorktable tile;
      tile = (TileWorktable) tileEntity;

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

        if (recipe != null) {
          StringBuilder renderString = new StringBuilder();
          ItemStack recipeOutput = recipe.getRecipeOutput();

          if (!recipeOutput.isEmpty()) {

            WorktableRecipe worktableRecipe = tile.getWorktableRecipe();
            List<Item> toolList = null;

            if (worktableRecipe != null && !worktableRecipe.getToolList().isEmpty()) {
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

              if (this.craftingTableRenderString == null) {
                this.craftingTableRenderString = WailaUtil.getStackRenderString(new ItemStack(Blocks.CRAFTING_TABLE));
              }

              renderString.append(this.craftingTableRenderString);

            } else {
              int index = (int) ((Minecraft.getMinecraft().world.getTotalWorldTime() / 29) % toolList.size());
              Item item = toolList.get(index);
              renderString.append(WailaUtil.getStackRenderString(new ItemStack(item)));
            }

            renderString.append(WailaUtil.getProgressRenderString((int) (100 * progress), 100));
            renderString.append(WailaUtil.getStackRenderString(recipeOutput));
          }
          tooltip.add(renderString.toString());
          tooltip.add(recipeOutput.getDisplayName());
        }
      }

      // Add condition

      int remainingDurability = tile.getRemainingDurability();
      int durability = tile.getDurability();

      float d = remainingDurability / (float) durability;

      if (d < 0.25) {
        String condition = I18n.translateToLocal("gui.pyrotech.waila.worktable.condition.fractured");
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.waila.worktable.condition", TextFormatting.RED, condition));

      } else if (d < 0.50) {
        String condition = I18n.translateToLocal("gui.pyrotech.waila.worktable.condition.used");
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.waila.worktable.condition", TextFormatting.GOLD, condition));

      } else if (d < 0.75) {
        String condition = I18n.translateToLocal("gui.pyrotech.waila.worktable.condition.fair");
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.waila.worktable.condition", TextFormatting.YELLOW, condition));

      } else {
        String condition = I18n.translateToLocal("gui.pyrotech.waila.worktable.condition.good");
        tooltip.add(I18n.translateToLocalFormatted("gui.pyrotech.waila.worktable.condition", TextFormatting.GREEN, condition));
      }

      { // Add look-at item
        RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;

        if (rayTraceResult == null) {
          return tooltip;
        }

        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) {
          return tooltip;
        }

        if (rayTraceResult.hitInfo instanceof InteractionRayTraceData.List) {
          InteractionRayTraceData.List list = (InteractionRayTraceData.List) rayTraceResult.hitInfo;

          for (InteractionRayTraceData data : list) {
            IInteraction interaction = data.getInteraction();

            if (interaction.isEnabled()
                && interaction instanceof IInteractionItemStack) {
              ItemStack stackInSlot = ((IInteractionItemStack) interaction).getStackInSlot();

              if (!stackInSlot.isEmpty()) {
                //tooltip.add(WailaUtil.getStackRenderString(stackInSlot));

                if (stackInSlot.getCount() > 1) {
                  tooltip.add(stackInSlot.getItem().getItemStackDisplayName(stackInSlot) + ": " + stackInSlot.getCount());

                } else {
                  tooltip.add(stackInSlot.getItem().getItemStackDisplayName(stackInSlot));
                }
              }
            }
          }
        }
      }
    }

    return tooltip;
  }
}
