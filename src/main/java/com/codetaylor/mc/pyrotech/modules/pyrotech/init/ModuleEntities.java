package com.codetaylor.mc.pyrotech.modules.pyrotech.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.pyrotech.entity.EntityRock;
import com.codetaylor.mc.pyrotech.modules.pyrotech.entity.EntityRockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import javax.annotation.Nonnull;

public class ModuleEntities {

  public static void onRegister(Registry registry) {

    registry.createEntityEntry(EntityRock.NAME, EntityEntryBuilder.create()
        .entity(EntityRock.class)
        .tracker(80, 1, true)
    );
    registry.createEntityEntry(EntityRockGrass.NAME, EntityEntryBuilder.create()
        .entity(EntityRockGrass.class)
        .tracker(80, 1, true)
    );
  }

  public static void onClientRegister() {

    RenderingRegistry.registerEntityRenderingHandler(EntityRock.class, manager -> {
      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
      return new RenderSnowball<EntityRock>(manager, ModuleItems.ROCK, renderItem) {

        @Nonnull
        @Override
        public ItemStack getStackToRender(EntityRock entity) {

          int meta = entity.getMeta();
          return new ItemStack(this.item, 1, meta);
        }
      };
    });

    RenderingRegistry.registerEntityRenderingHandler(EntityRockGrass.class, manager -> {
      RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
      return new RenderSnowball<EntityRockGrass>(manager, ModuleItems.ROCK_GRASS, renderItem) {

        @Nonnull
        @Override
        public ItemStack getStackToRender(EntityRockGrass entity) {

          int meta = entity.getMeta();
          return new ItemStack(this.item, 1, meta);
        }
      };
    });
  }

}
