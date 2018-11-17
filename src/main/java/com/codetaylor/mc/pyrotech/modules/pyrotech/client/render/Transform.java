package com.codetaylor.mc.pyrotech.modules.pyrotech.client.render;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Quaternion;

public class Transform {

  public final Vec3d translation;
  public final Quaternion rotation;
  public final Vec3d scale;

  public Transform(Vec3d translation, Quaternion rotation, Vec3d scale) {

    this.translation = translation;
    this.rotation = rotation;
    this.scale = scale;
  }
}
