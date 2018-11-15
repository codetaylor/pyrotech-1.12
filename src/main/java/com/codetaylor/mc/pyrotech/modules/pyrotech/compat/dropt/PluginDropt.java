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
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRockGrass;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemBonePickaxe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemCrudePickaxe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemFlintPickaxe;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.block.BlockStone;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

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
    String stone = item("minecraft", "stone");
    String diorite = item("minecraft", "stone", BlockStone.EnumType.DIORITE.getMetadata());
    String andesite = item("minecraft", "stone", BlockStone.EnumType.ANDESITE.getMetadata());
    String granite = item("minecraft", "stone", BlockStone.EnumType.GRANITE.getMetadata());
    String dioriteSmooth = item("minecraft", "stone", BlockStone.EnumType.DIORITE_SMOOTH.getMetadata());
    String andesiteSmooth = item("minecraft", "stone", BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata());
    String graniteSmooth = item("minecraft", "stone", BlockStone.EnumType.GRANITE_SMOOTH.getMetadata());
    String cobblestone = item("minecraft", "cobblestone");
    String gravel = item("minecraft", "gravel");
    String flint = item("minecraft", "flint");
    String tallGrassAny = item("minecraft", "tallgrass", OreDictionary.WILDCARD_VALUE);
    String sandstoneAny = item("minecraft", "sandstone", OreDictionary.WILDCARD_VALUE);

    String stonePickaxe = item("minecraft", "stone_pickaxe");

    String rockStone = item(BlockRock.NAME, BlockRock.EnumType.STONE.getMeta());
    String rockGranite = item(BlockRock.NAME, BlockRock.EnumType.GRANITE.getMeta());
    String rockDiorite = item(BlockRock.NAME, BlockRock.EnumType.DIORITE.getMeta());
    String rockAndesite = item(BlockRock.NAME, BlockRock.EnumType.ANDESITE.getMeta());
    String rockDirt = item(BlockRock.NAME, BlockRock.EnumType.DIRT.getMeta());
    String rockSand = item(BlockRock.NAME, BlockRock.EnumType.SAND.getMeta());
    String rockSandstone = item(BlockRock.NAME, BlockRock.EnumType.SANDSTONE.getMeta());
    String rockGrass = item(BlockRockGrass.NAME, 0);

    String flintShard = item(ItemMaterial.NAME, ItemMaterial.EnumType.FLINT_SHARD.getMeta());
    String plantFibers = item(ItemMaterial.NAME, ItemMaterial.EnumType.PLANT_FIBERS.getMeta());

    String crudePickaxe = item(ItemCrudePickaxe.NAME, OreDictionary.WILDCARD_VALUE);

    String flintPickaxe = item(ItemFlintPickaxe.NAME, OreDictionary.WILDCARD_VALUE);

    String bonePickaxe = item(ItemBonePickaxe.NAME, OreDictionary.WILDCARD_VALUE);

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
            drop().items(new String[]{rockStone}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{rockGranite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{rockDiorite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{rockAndesite}, range(3, 6)).selector(weight(2)),
            drop().items(new String[]{flintShard}).selector(weight(1))
        })
    );

    // -------------------------------------------------------------------------
    // - Sandstone
    // -------------------------------------------------------------------------

    // Crude Pickaxe
    // Drops sandstone rocks
    list.add(rule()
        .matchBlocks(new String[]{
            sandstoneAny
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{crudePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockSandstone}, range(2, 4))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops sandstone rocks
    list.add(rule()
        .matchBlocks(new String[]{
            sandstoneAny
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{flintPickaxe, bonePickaxe, stonePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockSandstone}, range(3, 6))
        })
    );

    // -------------------------------------------------------------------------
    // - Stone / Cobblestone
    // -------------------------------------------------------------------------

    // Crude Pickaxe
    // Drops stone rocks
    list.add(rule()
        .matchBlocks(new String[]{
            stone,
            cobblestone
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{crudePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(2, 4))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops stone rocks
    list.add(rule()
        .matchBlocks(new String[]{
            stone,
            cobblestone
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{flintPickaxe, bonePickaxe, stonePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}, range(3, 6))
        })
    );

    // -------------------------------------------------------------------------
    // - Diorite
    // -------------------------------------------------------------------------

    // Crude Pickaxe
    // Drops diorite rocks
    list.add(rule()
        .matchBlocks(new String[]{
            diorite,
            dioriteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{crudePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDiorite}, range(2, 4))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops diorite rocks
    list.add(rule()
        .matchBlocks(new String[]{
            diorite,
            dioriteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{flintPickaxe, bonePickaxe, stonePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockDiorite}, range(3, 6))
        })
    );

    // -------------------------------------------------------------------------
    // - Andesite
    // -------------------------------------------------------------------------

    // Crude Pickaxe
    // Drops andesite rocks
    list.add(rule()
        .matchBlocks(new String[]{
            andesite,
            andesiteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{crudePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockAndesite}, range(1, 3))
        })
    );

    // Flint / Bone / Stone Pickaxe
    // Drops andesite rocks
    list.add(rule()
        .matchBlocks(new String[]{
            andesite,
            andesiteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{flintPickaxe, bonePickaxe, stonePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockAndesite}, range(2, 5))
        })
    );

    // -------------------------------------------------------------------------
    // - Granite
    // -------------------------------------------------------------------------

    list.add(rule()
        .matchBlocks(new String[]{
            granite,
            graniteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{crudePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockGranite}, range(1, 3))
        })
    );

    list.add(rule()
        .matchBlocks(new String[]{
            granite,
            graniteSmooth
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{flintPickaxe, bonePickaxe, stonePickaxe})
        )
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockGranite}, range(2, 5))
        })
    );

    // -------------------------------------------------------------------------
    // - Registration
    // -------------------------------------------------------------------------

    ResourceLocation resourceLocation = new ResourceLocation(ModulePyrotech.MOD_ID, "dropt");
    registerRuleList(resourceLocation, 0, list);
  }
}
