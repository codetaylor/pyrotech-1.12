package com.codetaylor.mc.pyrotech.modules.hunting.client;

import com.codetaylor.mc.athenaeum.util.MathConstants;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.CapabilitySpear;
import com.codetaylor.mc.pyrotech.modules.hunting.capability.ISpearEntityData;
import com.codetaylor.mc.pyrotech.modules.hunting.entity.EntitySpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Random;

public class LayerSpear
    implements LayerRenderer<EntityLivingBase> {

  private final RenderLivingBase<?> renderer;

  public LayerSpear(RenderLivingBase<?> renderer) {

    this.renderer = renderer;
  }

  @Override
  public void doRenderLayer(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    ISpearEntityData data = entity.getCapability(CapabilitySpear.INSTANCE, null);

    if (data == null) {
      return;
    }

    if (data.getItemStackCount() > 0) {
      RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
      RenderLivingBase<?> entityRender = (RenderLivingBase<?>) renderManager.getEntityRenderObject(entity);

      if (entityRender == null) {
        return;
      }

      EntitySpear itemEntity = new EntitySpear(entity.world, entity.posX, entity.posY, entity.posZ);
      itemEntity.setItemStack(new ItemStack(ModuleHunting.Items.FLINT_SPEAR));
      Random random = new Random(data.getSeed());
      RenderHelper.disableStandardItemLighting();

//      EntityPlayerSP player = Minecraft.getMinecraft().player;
//      double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
//      double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
//      double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
//
//      double eX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
//      double eY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
//      double eZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;

      for (int j = 0; j < data.getItemStackCount(); ++j) {

        ModelRenderer modelrenderer = entityRender.getMainModel().getRandomModelBox(random);
        ModelBox modelbox = modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));

        GlStateManager.pushMatrix();
        {
          float rX = random.nextFloat();
          float rY = random.nextFloat();
          float rZ = random.nextFloat();

          float x = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * rX) / 16;
          float y = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * rY) / 16;
          float z = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * rZ) / 16;

          GlStateManager.translate(x, y, z);
//          GlStateManager.translate(x + eX - pX, y + eY - pY, z + eZ - pZ);
//          GlStateManager.rotate(45, 1, 0, 0);

          rX = (rX * 2 - 1) * -1;
          rY = (rY * 2 - 1) * -1;
          rZ = (rZ * 2 - 1) * -1;

          float distance = MathHelper.sqrt(rX * rX + rZ * rZ);

          itemEntity.rotationYaw = random.nextFloat() * 360;//(float) (Math.atan2(rX, rZ) * MathConstants.RAD_TO_DEG);
          itemEntity.rotationPitch = 45;//(float) (Math.atan2(rY, distance) * MathConstants.RAD_TO_DEG);
          itemEntity.prevRotationYaw = itemEntity.rotationYaw;
          itemEntity.prevRotationPitch = itemEntity.rotationPitch;

          renderManager.renderEntity(itemEntity, 0, 0, 0, 0, partialTicks, true);
        }
        GlStateManager.popMatrix();
      }

      RenderHelper.enableStandardItemLighting();
    }
  }

  @Override
  public boolean shouldCombineTextures() {

    return false;
  }
}