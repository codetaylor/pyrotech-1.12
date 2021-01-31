package com.codetaylor.mc.pyrotech.modules.hunting.event;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class EntityLivingDropsEventHandler {

  private static Map<ResourceLocation, List<DropData>> DROP_MAP;
  private static List<ResourceLocation> DROP_CAPTURE_LIST;

  /**
   * This event handler is called from the core event handler directly after
   * processing the sheep wool directive.
   *
   * @param event the event
   */
  public static void on(LivingDropsEvent event) {

    EntityLivingBase entityLiving = event.getEntityLiving();

    if (entityLiving.world.isRemote) {
      return;
    }

    if (DROP_MAP == null) {
      DROP_MAP = DropMapFactory.create();
    }

    if (DROP_CAPTURE_LIST == null) {
      DROP_CAPTURE_LIST = DropCaptureFactory.create();
    }

    List<EntityItem> drops = event.getDrops();
    List<ItemStack> capturedDrops = new ArrayList<>();

    // Remove leather and capture drops.

    for (Iterator<EntityItem> iterator = drops.iterator(); iterator.hasNext(); ) {
      EntityItem entityItem = iterator.next();
      ItemStack itemStack = entityItem.getItem();
      Item item = itemStack.getItem();

      if (ModuleHuntingConfig.DROPS.REMOVE_LEATHER_DROPS && item == Items.LEATHER) {
        iterator.remove();

      } else if (DROP_CAPTURE_LIST.contains(item.getRegistryName())) {
        capturedDrops.add(itemStack);
        iterator.remove();
      }
    }

    ResourceLocation resourceLocation = EntityList.getKey(entityLiving);

    List<DropData> dropData = DROP_MAP.get(resourceLocation);

    if (dropData != null) {

      for (DropData data : dropData) {

        if (RandomHelper.random().nextFloat() <= data.chance) {
          capturedDrops.add(new ItemStack(data.item, data.count, data.meta));
        }
      }
    }

    // Special sheep handling
    if (entityLiving instanceof EntitySheep) {
      SheepDropFactory.create((EntitySheep) entityLiving, capturedDrops);
    }

    if (!capturedDrops.isEmpty()) {
      ItemStack itemStack = CarcassFactory.create(capturedDrops);
      drops.add(new EntityItem(entityLiving.world, entityLiving.posX, entityLiving.posY, entityLiving.posZ, itemStack));
    }
  }

  private static class SheepDropFactory {

    private static void create(EntitySheep entitySheep, List<ItemStack> result) {

      if (RandomHelper.random().nextFloat() > ModuleHuntingConfig.DROPS.SHEEP_PELT_CHANCE) {
        return;
      }

      if (entitySheep.getSheared()) {
        result.add(new ItemStack(ModuleHunting.Items.HIDE_SHEEP_SHEARED, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));

      } else {
        EnumDyeColor fleeceColor = entitySheep.getFleeceColor();

        switch (fleeceColor) {
          case BLUE:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_BLUE, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case RED:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_RED, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case GRAY:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_GRAY, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case BLACK:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_BLACK, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case BROWN:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_BROWN, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case GREEN:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_GREEN, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case WHITE:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_WHITE, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case SILVER:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_GRAY_LIGHT, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case YELLOW:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_YELLOW, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case CYAN:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_CYAN, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case LIME:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_LIME, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case PINK:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_PINK, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case ORANGE:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_ORANGE, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case PURPLE:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_PURPLE, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case MAGENTA:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_MAGENTA, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          case LIGHT_BLUE:
            result.add(new ItemStack(ModuleHunting.Items.PELT_SHEEP_BLUE_LIGHT, ModuleHuntingConfig.DROPS.SHEEP_PELT_COUNT));
            return;
          default:
            throw new IllegalStateException("Unknown enum: " + fleeceColor);
        }
      }
    }
  }

  private static class DropMapFactory {

    private static Map<ResourceLocation, List<DropData>> create() {

      Map<ResourceLocation, List<DropData>> result = new HashMap<>();

      for (Map.Entry<String, String> entry : ModuleHuntingConfig.DROPS.DROP_MAP.entrySet()) {
        String key = entry.getKey();
        ResourceLocation entityResourceLocation = new ResourceLocation(key);
        Class<? extends Entity> entityClass = EntityList.getClass(entityResourceLocation);

        if (entityClass == null) {
          ModuleHunting.LOGGER.error("Unknown entity: " + entityResourceLocation);
          continue;
        }

        String value = entry.getValue();
        String[] split = value.split(";");

        if (split.length != 3) {
          ModuleHunting.LOGGER.error("Invalid drop string: " + value);
          continue;
        }

        ParseResult parseResult;

        try {
          parseResult = RecipeItemParser.INSTANCE.parse(split[0]);

        } catch (MalformedRecipeItemException e) {
          ModuleHunting.LOGGER.error("Unable to parse item string: " + split[0]);
          continue;
        }

        ResourceLocation itemResourceLocation = new ResourceLocation(parseResult.getDomain(), parseResult.getPath());
        Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);

        if (item == null) {
          ModuleHunting.LOGGER.error("Unable to find item: " + split[0]);
          continue;
        }

        int count;

        try {
          count = Integer.parseInt(split[1]);

        } catch (NumberFormatException e) {
          ModuleHunting.LOGGER.error("Invalid integer: " + split[1]);
          continue;
        }

        float chance;

        try {
          chance = Float.parseFloat(split[2]);

        } catch (NumberFormatException e) {
          ModuleHunting.LOGGER.error("Invalid float: " + split[2]);
          continue;
        }

        List<DropData> list = result.computeIfAbsent(entityResourceLocation, resourceLocation -> new ArrayList<>());
        list.add(new DropData(item, parseResult.getMeta(), count, chance));
      }

      return result;
    }
  }

  private static class DropCaptureFactory {

    private static List<ResourceLocation> create() {

      List<ResourceLocation> result = new ArrayList<>(ModuleHuntingConfig.DROPS.DROP_CAPTURE_LIST.length);

      for (String key : ModuleHuntingConfig.DROPS.DROP_CAPTURE_LIST) {
        result.add(new ResourceLocation(key));
      }

      return result;
    }
  }

  private static class CarcassFactory {

    private static ItemStack create(List<ItemStack> itemStacks) {

      ItemStackHandler itemStackHandler = new ItemStackHandler(itemStacks.size());

      for (int i = 0; i < itemStacks.size(); i++) {
        itemStackHandler.insertItem(i, itemStacks.get(i), false);
      }

      ItemStack itemStack = new ItemStack(ModuleHunting.Blocks.CARCASS);
      NBTTagCompound tag = StackHelper.getTagSafe(itemStack);
      NBTTagCompound tileEntityTag = new NBTTagCompound();
      tag.setTag(StackHelper.BLOCK_ENTITY_TAG, tileEntityTag);
      tileEntityTag.setTag("stackHandler", itemStackHandler.serializeNBT());
      return itemStack;
    }
  }

  private static class DropData {

    private final Item item;
    private final int meta;
    private final int count;
    private final float chance;

    private DropData(Item item, int meta, int count, float chance) {

      this.item = item;
      this.meta = meta;
      this.count = count;
      this.chance = chance;
    }
  }
}
