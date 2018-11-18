package com.codetaylor.mc.pyrotech.modules.pyrotech.tile;

import com.codetaylor.mc.athenaeum.util.QuaternionHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechConfig;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.ITileInteractable;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.InteractionBounds;
import com.codetaylor.mc.pyrotech.modules.pyrotech.client.render.Transform;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.vector.Quaternion;

public class TileDryingRack
    extends TileDryingRackBase
    implements ITileInteractable {

  private InteractionHandler_ItemStack_SingleTransform[] interactionHandlers;

  public TileDryingRack() {

    super();

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
          new InteractionBounds(qX * 0.5, qZ * 0.5, qX * 0.5 + 0.5, qZ * 0.5 + 0.5),
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

  }

  @Override
  protected int getSlotCount() {

    return 4;
  }

  @Override
  protected float getSpeedModified(float speed) {

    return (float) (speed * ModulePyrotechConfig.DRYING_RACK.SPEED_MODIFIER);
  }

  @Override
  public InteractionHandler_ItemStack_SingleTransform[] getInteractionHandlers() {

    return this.interactionHandlers;
  }
}
