package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
<<<<<<< HEAD
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.ITileInteractionHandler_ItemStack_Provider;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.InteractionHandler_ItemStack_SingleTransform;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

public class TileDryingRack
    extends TileDryingRackBase
    implements ITileInteractionHandler_ItemStack_Provider {

  private InteractionHandler_ItemStack_SingleTransform[] interactionHandlers;
=======

public class TileDryingRack
    extends TileDryingRackBase {
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable

  public TileDryingRack() {

    super();
<<<<<<< HEAD

    this.interactionHandlers = new InteractionHandler_ItemStack_SingleTransform[4];

    for (int slot = 0; slot < 4; slot++) {

      int qX = (slot & 1);
      int qZ = ((slot >> 1) & 1);

      this.interactionHandlers[slot] = new InteractionHandler_ItemStack_SingleTransform(
          new ItemStackHandler[]{
              this.stackHandler,
              this.outputStackHandler
          },
          slot,
          EnumFacing.VALUES,
          new AxisAlignedBB(qX * 0.5, 0, qZ * 0.5, qX * 0.5 + 0.5, 1, qZ * 0.5 + 0.5),
          new Transform(
              new Vec3d(
                  qX * 0.375 + 0.25 + 0.0625,
                  0.75 + 0.03125,
                  qZ * 0.375 + 0.25 + 0.0625
              ),
              QuaternionHelper.setFromAxisAngle(new Quaternion(), 1, 0, 0, (float) (Math.PI / 2) /* 90 */),
              new Vec3d(0.25, 0.25, 0.25)
          )
      );

    }

=======
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable
  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.DRYING_RACK.SPEED_MODIFIER);
  }
<<<<<<< HEAD

  @Override
  public InteractionHandler_ItemStack_SingleTransform[] getInteractionHandlers() {

    return this.interactionHandlers;
  }
=======
>>>>>>> parent of 03250e2... Switch drying rack, crude drying rack and campfire to interactable
}
