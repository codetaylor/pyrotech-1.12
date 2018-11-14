package com.codetaylor.mc.pyrotech.modules.pyrotech.compat;

import com.codetaylor.mc.dropt.api.DroptAPI;
import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.api.event.DroptLoadRulesEvent;
import com.codetaylor.mc.dropt.api.reference.EnumHarvesterType;
import com.codetaylor.mc.dropt.api.reference.EnumListType;
import com.codetaylor.mc.dropt.api.reference.EnumReplaceStrategy;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotech;
import com.codetaylor.mc.pyrotech.modules.pyrotech.block.BlockRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.item.ItemMaterial;
import net.minecraft.block.BlockStone;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class DroptRules {

  @SubscribeEvent
  public static void on(DroptLoadRulesEvent event) {

    List<IDroptRuleBuilder> list = new ArrayList<>();

    // -------------------------------------------------------------------------
    // - Crude Shovel
    // -------------------------------------------------------------------------

    list.add(DroptAPI.rule()
        .matchBlocks(new String[]{
            "minecraft:gravel"
        })
        .matchHarvester(DroptAPI.harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(EnumListType.BLACKLIST, "shovel;1;-1")
        )
        .replaceStrategy(EnumReplaceStrategy.REPLACE_ALL_IF_SELECTED)
        .addDrops(new IDroptDropBuilder[]{
            DroptAPI.drop()
                .items(new String[]{
                    "pyrotech:rock:" + BlockRock.EnumType.STONE.getMeta(),
                    "pyrotech:rock:" + BlockRock.EnumType.GRANITE.getMeta(),
                    "pyrotech:rock:" + BlockRock.EnumType.DIORITE.getMeta(),
                    "pyrotech:rock:" + BlockRock.EnumType.ANDESITE.getMeta()
                }, DroptAPI.range(1, 3))
                .selector(DroptAPI.weight(88)),
            DroptAPI.drop()
                .items(new String[]{
                    "pyrotech:material:" + ItemMaterial.EnumType.FLINT_SHARD.getMeta()
                })
                .selector(DroptAPI.weight(12)),
        })
    );

    // -------------------------------------------------------------------------
    // - Crude Pickaxe
    // -------------------------------------------------------------------------

    list.add(DroptAPI.rule()
        .matchBlocks(new String[]{
            "minecraft:stone",
            "minecraft:cobblestone"
        })
        .matchHarvester(DroptAPI.harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                "pyrotech:crude_pickaxe:*"
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            DroptAPI.drop().items(new String[]{
                "pyrotech:rock:" + BlockRock.EnumType.STONE.getMeta()
            }, DroptAPI.range(1, 3))
        })
    );

    list.add(DroptAPI.rule()
        .matchBlocks(new String[]{
            "minecraft:stone:" + BlockStone.EnumType.DIORITE.getMetadata(),
            "minecraft:stone:" + BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()
        })
        .matchHarvester(DroptAPI.harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                "pyrotech:crude_pickaxe:*"
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            DroptAPI.drop().items(new String[]{
                "pyrotech:rock:" + BlockRock.EnumType.DIORITE.getMeta()
            }, DroptAPI.range(1, 3))
        })
    );

    list.add(DroptAPI.rule()
        .matchBlocks(new String[]{
            "minecraft:stone:" + BlockStone.EnumType.ANDESITE.getMetadata(),
            "minecraft:stone:" + BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()
        })
        .matchHarvester(DroptAPI.harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                "pyrotech:crude_pickaxe:*"
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            DroptAPI.drop().items(new String[]{
                "pyrotech:rock:" + BlockRock.EnumType.ANDESITE.getMeta()
            }, DroptAPI.range(1, 3))
        })
    );

    list.add(DroptAPI.rule()
        .matchBlocks(new String[]{
            "minecraft:stone:" + BlockStone.EnumType.GRANITE.getMetadata(),
            "minecraft:stone:" + BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()
        })
        .matchHarvester(DroptAPI.harvester()
            .type(EnumHarvesterType.PLAYER)
            .mainHand(new String[]{
                "pyrotech:crude_pickaxe:*"
            })
        )
        .addDrops(new IDroptDropBuilder[]{
            DroptAPI.drop().items(new String[]{
                "pyrotech:rock:" + BlockRock.EnumType.GRANITE.getMeta()
            }, DroptAPI.range(1, 3))
        })
    );

    // -------------------------------------------------------------------------
    // - Registration
    // -------------------------------------------------------------------------

    ResourceLocation resourceLocation = new ResourceLocation(ModulePyrotech.MOD_ID, "dropt");
    DroptAPI.registerRuleList(resourceLocation, 0, list);
  }
}
