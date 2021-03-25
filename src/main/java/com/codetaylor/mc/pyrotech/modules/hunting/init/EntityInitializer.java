package com.codetaylor.mc.pyrotech.modules.hunting.init;

import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityBoneArrow;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntityFlintArrow;
import com.codetaylor.mc.pyrotech.modules.hunting.item.EntityItemHideScraped;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public final class EntityInitializer {

  public static void onRegister(Registry registry) {

    registry.createEntityEntry(EntityItemHideScraped.NAME, EntityEntryBuilder.create()
        .entity(EntityItemHideScraped.class)
        .tracker(80, 4, true)
    );

    registry.createEntityEntry(EntityFlintArrow.NAME, EntityEntryBuilder.create()
        .entity(EntityFlintArrow.class)
        .tracker(80, 1, true)
    );

    registry.createEntityEntry(EntityBoneArrow.NAME, EntityEntryBuilder.create()
        .entity(EntityBoneArrow.class)
        .tracker(80, 1, true)
    );
  }

  @SideOnly(Side.CLIENT)
  public static void onClientRegister() {

    RenderingRegistry.registerEntityRenderingHandler(EntityFlintArrow.class, manager -> new RenderArrow<EntityFlintArrow>(manager) {

      private final ResourceLocation texture = new ResourceLocation(ModuleHunting.MOD_ID, "textures/entity/projectiles/arrow_flint.png");

      @Override
      protected ResourceLocation getEntityTexture(@Nonnull EntityFlintArrow entity) {

        return this.texture;
      }
    });

    RenderingRegistry.registerEntityRenderingHandler(EntityBoneArrow.class, manager -> new RenderArrow<EntityBoneArrow>(manager) {

      private final ResourceLocation texture = new ResourceLocation(ModuleHunting.MOD_ID, "textures/entity/projectiles/arrow_bone.png");

      @Override
      protected ResourceLocation getEntityTexture(@Nonnull EntityBoneArrow entity) {

        return this.texture;
      }
    });
  }

  private EntityInitializer() {
    //
  }
}
