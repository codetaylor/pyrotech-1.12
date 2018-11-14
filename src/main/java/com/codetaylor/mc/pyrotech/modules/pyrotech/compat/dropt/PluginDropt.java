package com.codetaylor.mc.pyrotech.modules.pyrotech.compat.dropt;

import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.api.event.DroptLoadRulesEvent;
import com.codetaylor.mc.dropt.api.reference.EnumHarvesterType;
import com.codetaylor.mc.dropt.api.reference.EnumListType;
import com.codetaylor.mc.dropt.api.reference.EnumReplaceStrategy;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
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

    if (!ModulePyrotechConfig.COMPAT.ENABLE_DROPT_RULES) {
      return;
    }

    // -------------------------------------------------------------------------
    // - Item / Block Strings
    // -------------------------------------------------------------------------

    String stone = item("minecraft", "stone");
    String diorite = item("minecraft", "stone", BlockStone.EnumType.DIORITE.getMetadata());
    String andesite = item("minecraft", "stone", BlockStone.EnumType.ANDESITE.getMetadata());
    String granite = item("minecraft", "stone", BlockStone.EnumType.GRANITE.getMetadata());
    String dioriteSmooth = item("minecraft", "stone", BlockStone.EnumType.DIORITE_SMOOTH.getMetadata());
    String andesiteSmooth = item("minecraft", "stone", BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata());
    String graniteSmooth = item("minecraft", "stone", BlockStone.EnumType.GRANITE_SMOOTH.getMetadata());
    String cobblestone = item("minecraft", "cobblestone");
    String gravel = item("minecraft", "gravel");

    String stonePickaxe = item("minecraft", "stone_pickaxe");

    String rockStone = item(BlockRock.NAME, BlockRock.EnumType.STONE.getMeta());
    String rockGranite = item(BlockRock.NAME, BlockRock.EnumType.GRANITE.getMeta());
    String rockDiorite = item(BlockRock.NAME, BlockRock.EnumType.DIORITE.getMeta());
    String rockAndesite = item(BlockRock.NAME, BlockRock.EnumType.ANDESITE.getMeta());

    String flintShard = item(ItemMaterial.NAME, ItemMaterial.EnumType.FLINT_SHARD.getMeta());

    String crudePickaxe = item(ItemCrudePickaxe.NAME, OreDictionary.WILDCARD_VALUE);

    String flintPickaxe = item(ItemFlintPickaxe.NAME, OreDictionary.WILDCARD_VALUE);

    String bonePickaxe = item(ItemBonePickaxe.NAME, OreDictionary.WILDCARD_VALUE);

    List<IDroptRuleBuilder> list = new ArrayList<>();

    // -------------------------------------------------------------------------
    // - Crude Shovel
    // -------------------------------------------------------------------------

    list.add(rule()
        .matchBlocks(new String[]{
            gravel
        })
        .matchHarvester(harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .replaceStrategy(EnumReplaceStrategy.REPLACE_ALL_IF_SELECTED)
        .dropCount(range(1, 3))
        .addDrops(new IDroptDropBuilder[]{
            drop().items(new String[]{rockStone}).selector(weight(10)),
            drop().items(new String[]{rockGranite}).selector(weight(10)),
            drop().items(new String[]{rockDiorite}).selector(weight(10)),
            drop().items(new String[]{rockAndesite}).selector(weight(10)),
            drop().items(new String[]{flintShard}).selector(weight(5)),
        })
    );

    // -------------------------------------------------------------------------
    // - Crude Pickaxe
    // -------------------------------------------------------------------------

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
            drop().items(new String[]{rockStone}, range(1, 3))
        })
    );

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
            drop().items(new String[]{rockDiorite}, range(1, 3))
        })
    );

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

    // -------------------------------------------------------------------------
    // - Flint / Bone / Stone Pickaxe
    // -------------------------------------------------------------------------

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
            drop().items(new String[]{rockStone}, range(2, 5)).selector(weight(2)),
            drop().items(new String[]{cobblestone}).selector(weight(1))
        })
    );

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
            drop().items(new String[]{rockDiorite}, range(2, 5))
        })
    );

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
