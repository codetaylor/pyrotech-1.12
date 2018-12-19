package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.dropt;

import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.api.event.DroptLoadRulesEvent;
import com.codetaylor.mc.dropt.api.reference.EnumDropStrategy;
import com.codetaylor.mc.dropt.api.reference.EnumHarvesterType;
import com.codetaylor.mc.dropt.api.reference.EnumListType;
import com.codetaylor.mc.dropt.api.reference.EnumReplaceStrategy;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockCobblestone;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockOre;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRockGrass;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemCrudePickaxe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.block.BlockStone;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.codetaylor.mc.dropt.api.DroptAPI.*;

public class PluginDropt {

  public PluginDropt() {

    MinecraftForge.EVENT_BUS.register(this);
  }

  private static String item(String modId, String name) {

    return item(modId, name, 0);
  }

  private static String item(String name, int meta) {

    return item(ModulePyrotech.MOD_ID, name, meta);
  }

  private static String item(String modId, String name, int meta) {

    return itemString(modId, name, meta);
  }

  @SubscribeEvent
  public void on(DroptLoadRulesEvent event) {

    if (!ModulePyrotechConfig.COMPAT_DROPT.ENABLE) {
      return;
    }

    // -------------------------------------------------------------------------
    // - Item / Block Strings
    // -------------------------------------------------------------------------

    String sand = item("minecraft", "sand");
    String dirtAny = item("minecraft", "dirt", OreDictionary.WILDCARD_VALUE);
    String grass = item("minecraft", "grass");
    String gravel = item("minecraft", "gravel");
    String tallGrassAny = item("minecraft", "tallgrass", OreDictionary.WILDCARD_VALUE);
    String leaves = item("minecraft", "leaves", OreDictionary.WILDCARD_VALUE);
    String stick = item("minecraft", "stick");

    String flint = item("minecraft", "flint");
    String boneMeal = item("minecraft", "dye", 15);

    String fossilOre = item(BlockOre.NAME, BlockOre.EnumType.FOSSIL_ORE.getMeta());
    String rockStone = item(BlockRock.NAME, BlockRock.EnumType.STONE.getMeta());
    String rockGranite = item(BlockRock.NAME, BlockRock.EnumType.GRANITE.getMeta());
    String rockDiorite = item(BlockRock.NAME, BlockRock.EnumType.DIORITE.getMeta());
    String rockAndesite = item(BlockRock.NAME, BlockRock.EnumType.ANDESITE.getMeta());
    String rockDirt = item(BlockRock.NAME, BlockRock.EnumType.DIRT.getMeta());
    String rockSand = item(BlockRock.NAME, BlockRock.EnumType.SAND.getMeta());
    String rockGrass = item(BlockRockGrass.NAME, 0);

    String flintShard = item(ItemMaterial.NAME, ItemMaterial.EnumType.FLINT_SHARD.getMeta());
    String boneShard = item(ItemMaterial.NAME, ItemMaterial.EnumType.BONE_SHARD.getMeta());
    String plantFibers = item(ItemMaterial.NAME, ItemMaterial.EnumType.PLANT_FIBERS.getMeta());

    List<IDroptRuleBuilder> list = new ArrayList<>();

    // -------------------------------------------------------------------------
    // - Tall Grass
    // -------------------------------------------------------------------------

    list.add(rule()
        .matchBlocks(new String[]{
            tallGrassAny
        })
        .replaceStrategy(EnumReplaceStrategy.ADD)
        .addDrops(new IDroptDropBuilder[]{
            drop().selector(weight(85)),
            drop().items(new String[]{plantFibers}, range(1, 2)).selector(weight(15))
        })
    );

    // -------------------------------------------------------------------------
    // - Leaves
    // -------------------------------------------------------------------------

    list.add(rule()
        .matchBlocks(new String[]{
            leaves
        })
        .replaceStrategy(EnumReplaceStrategy.ADD)
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{stick}).selector(weight(1)),
            drop().selector(weight(2))
        })
    );

    // -------------------------------------------------------------------------
    // - Dirt
    // -------------------------------------------------------------------------

    // Dirt
    // Not a shovel
    // Drops dirt clumps
    list.add(rule()
        .matchBlocks(new String[]{
            dirtAny
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;0;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(1, 3))
        })
    );

    // Dirt
    // Shovel 0
    // Drops dirt clumps
    list.add(rule()
        .matchBlocks(new String[]{
            dirtAny
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .dropStrategy(EnumDropStrategy.UNIQUE)
        .dropCount(range(1, 2))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(2, 4))
        })
    );

    // Dirt
    // Shovel 1
    // Drops dirt clumps
    list.add(rule()
        .matchBlocks(new String[]{
            dirtAny
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;2;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(3, 6))
        })
    );

    // -------------------------------------------------------------------------
    // - Grass
    // -------------------------------------------------------------------------

    // Grass
    // Not a shovel
    // Drops dirt clumps
    list.add(rule()
        .matchBlocks(new String[]{
            grass
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;0;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(1, 3))
        })
    );

    // Grass
    // Shovel 0
    // Drops dirt clumps and grass clumps
    list.add(rule()
        .matchBlocks(new String[]{
            grass
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .dropStrategy(EnumDropStrategy.UNIQUE)
        .dropCount(range(1, 2))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(2, 4)).selector(weight(4)),
            drop().items(new String[]{rockGrass}).selector(weight(1))
        })
    );

    // Grass
    // Shovel 1
    // Drops dirt clumps and grass clumps
    list.add(rule()
        .matchBlocks(new String[]{
            grass
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;2;-1")
        )
        .dropStrategy(EnumDropStrategy.UNIQUE)
        .dropCount(range(2))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDirt}, range(1, 3)).selector(weight(1)),
            drop().items(new String[]{rockGrass}, range(1, 3)).selector(weight(2))
        })
    );

    // Grass
    // Shovel Any
    // Adds grass clumps to drops
    list.add(rule()
        .matchBlocks(new String[]{
            grass
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand("shovel;-1;-1")
        )
        .replaceStrategy(EnumReplaceStrategy.ADD)
        .dropCount(range(0, 1))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockGrass}, range(1, 3))
        })
    );

    // -------------------------------------------------------------------------
    // - Sand
    // -------------------------------------------------------------------------

    // Sand
    // Not a shovel
    list.add(rule()
        .matchBlocks(new String[]{
            sand
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;0;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockSand}, range(1, 3))
        })
    );

    // Sand
    // Shovel 0
    list.add(rule()
        .matchBlocks(new String[]{
            sand
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockSand}, range(2, 4))
        })
    );

    // Sand
    // Shovel 1
    list.add(rule()
        .matchBlocks(new String[]{
            sand
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;2;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockSand}, range(3, 6))
        })
    );

    // -------------------------------------------------------------------------
    // - Gravel
    // -------------------------------------------------------------------------

    // Gravel
    // Not a shovel
    list.add(rule()
        .matchBlocks(new String[]{
            gravel
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;0;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(1, 3)).selector(weight(10)),
            drop().items(new String[]{rockGranite}, range(1, 3)).selector(weight(10)),
            drop().items(new String[]{rockDiorite}, range(1, 3)).selector(weight(10)),
            drop().items(new String[]{rockAndesite}, range(1, 3)).selector(weight(10))
        })
    );

    // Gravel
    // Shovel 0
    list.add(rule()
        .matchBlocks(new String[]{
            gravel
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(2, 4)).selector(weight(2)),
            drop().items(new String[]{rockGranite}, range(2, 4)).selector(weight(2)),
            drop().items(new String[]{rockDiorite}, range(2, 4)).selector(weight(2)),
            drop().items(new String[]{rockAndesite}, range(2, 4)).selector(weight(2)),
            drop().items(new String[]{flintShard}).selector(weight(1))
        })
    );

    // Gravel
    // Shovel 1
    list.add(rule()
        .matchBlocks(new String[]{
            gravel
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;2;-1")
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(3, 6)).selector(weight(1)),
            drop().items(new String[]{rockGranite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{rockDiorite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{rockAndesite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{flintShard}).selector(weight(2)),
            drop().items(new String[]{flint}).selector(weight(1))
        })
    );

    // -------------------------------------------------------------------------
    // - Fossil Ore
    // -------------------------------------------------------------------------

    // Crude Pickaxe
    // Drops rocks
    list.add(rule()
        .matchBlocks(new String[]{
            fossilOre
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                item(ItemCrudePickaxe.NAME, OreDictionary.WILDCARD_VALUE)
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(2, 4)).selector(weight(7)),
            drop().items(new String[]{boneMeal}, range(1)).selector(weight(1)),
            drop().items(new String[]{boneShard}, range(1)).selector(weight(1))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops rocks
    list.add(rule()
        .matchBlocks(new String[]{
            fossilOre
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "pickaxe;2;-1")
        )
        .dropStrategy(EnumDropStrategy.UNIQUE)
        .dropCount(range(1, 2))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(2, 4)).selector(weight(6)),
            drop().items(new String[]{boneMeal}, range(1)).selector(weight(1)),
            drop().items(new String[]{boneShard}, range(1)).selector(weight(2))
        })
    );

    // -------------------------------------------------------------------------
    // - Sandstone
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("minecraft", "sandstone", OreDictionary.WILDCARD_VALUE);
      String rock = item(BlockRock.NAME, BlockRock.EnumType.SANDSTONE.getMeta());
      this.addRockDrops(matchBlock, rock, list);
    }

    // -------------------------------------------------------------------------
    // - Limestone
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("pyrotech", "limestone");
      String rock = item(BlockRock.NAME, BlockRock.EnumType.LIMESTONE.getMeta());
      this.addRockDrops(matchBlock, rock, list);
    }

    // -------------------------------------------------------------------------
    // - Stone / Cobblestone
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.STONE.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.STONE.getMeta());
      this.addRockDrops(matchBlock, rock, list);
    }
    {
      String matchBlock = item("minecraft", "cobblestone");
      String rock = item(BlockRock.NAME, BlockRock.EnumType.STONE.getMeta());
      this.addRockDrops(matchBlock, rock, list);
    }

    // -------------------------------------------------------------------------
    // - Diorite
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.DIORITE.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.DIORITE.getMeta());
      String replaceBlock = item(BlockCobblestone.NAME, BlockCobblestone.EnumType.DIORITE.getMeta());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.DIORITE_SMOOTH.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.DIORITE.getMeta());
      String replaceBlock = item("minecraft", "stone", BlockStone.EnumType.DIORITE.getMetadata());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }

    // -------------------------------------------------------------------------
    // - Andesite
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.ANDESITE.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.ANDESITE.getMeta());
      String replaceBlock = item(BlockCobblestone.NAME, BlockCobblestone.EnumType.ANDESITE.getMeta());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.ANDESITE.getMeta());
      String replaceBlock = item("minecraft", "stone", BlockStone.EnumType.ANDESITE.getMetadata());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }

    // -------------------------------------------------------------------------
    // - Granite
    // -------------------------------------------------------------------------
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.GRANITE.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.GRANITE.getMeta());
      String replaceBlock = item(BlockCobblestone.NAME, BlockCobblestone.EnumType.GRANITE.getMeta());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }
    {
      String matchBlock = item("minecraft", "stone", BlockStone.EnumType.GRANITE_SMOOTH.getMetadata());
      String rock = item(BlockRock.NAME, BlockRock.EnumType.GRANITE.getMeta());
      String replaceBlock = item("minecraft", "stone", BlockStone.EnumType.GRANITE.getMetadata());
      this.addRockDrops(matchBlock, rock, replaceBlock, list);
      this.addBlockReplace(matchBlock, replaceBlock, list);
    }

    // -------------------------------------------------------------------------
    // - Registration
    // -------------------------------------------------------------------------

    ResourceLocation resourceLocation = new ResourceLocation(ModulePyrotech.MOD_ID, "dropt");
    registerRuleList(resourceLocation, 0, list);
  }

  private void addBlockReplace(String matchBlock, String replaceBlock, List<IDroptRuleBuilder> list) {

    list.add(rule()
        .matchBlocks(new String[]{
            matchBlock
        })
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{replaceBlock})
        })
    );
  }

  private void addRockDrops(String matchBlock, String rock, List<IDroptRuleBuilder> list) {

    this.addRockDrops(matchBlock, rock, null, list);
  }

  private void addRockDrops(String matchBlock, String rock, @Nullable String replaceBlock, List<IDroptRuleBuilder> list) {

    // Crude Pickaxe
    // Drops rocks
    list.add(rule()
        .matchBlocks(new String[]{
            matchBlock
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                item(ItemCrudePickaxe.NAME, OreDictionary.WILDCARD_VALUE)
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rock}, range(2, 4))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops rocks
    list.add(rule()
        .matchBlocks(new String[]{
            matchBlock
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "pickaxe;2;-1")
        )
        .replaceStrategy(EnumReplaceStrategy.REPLACE_ALL_IF_SELECTED)
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rock}, range(3, 6)).selector(weight(4)),
            (replaceBlock == null) ? drop() : drop().items(new String[]{replaceBlock})
        })
    );
  }
}
