package com.codetaylor.mc.pyrotech.interaction.api;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Transform {

  public static final Transform IDENTITY = new Transform(
      new Vec3d(0, 0, 0),
      new Quaternion(),
      new Vec3d(1, 1, 1)
  );

  public static final Quaternion NO_ROTATION = new Quaternion();
  public static final Vec3d NO_TRANSLATION = new Vec3d(0, 0, 0);
  public static final Vec3d NO_SCALE = new Vec3d(1, 1, 1);

  public static Vec3d translate() {

    return NO_TRANSLATION;
  }

  public static Vec3d translate(double x, double y, double z) {

    return new Vec3d(x, y, z);
  }

  public static Quaternion rotate() {

    return NO_ROTATION;
  }

  public static Quaternion rotate(Quaternion[] rotations) {

    Quaternion result = new Quaternion(rotations[0]);

    for (int i = 1; i < rotations.length; i++) {
      result.multLocal(rotations[i]);
    }

    return result;
  }

  public static Quaternion rotate(double x, double y, double z, double angle) {

    return new Quaternion().setFromAxisAngle((float) x, (float) y, (float) z, (float) Math.toRadians(angle));
  }

  public static Vec3d scale() {

    return NO_SCALE;
  }

  public static Vec3d scale(double x, double y, double z) {

    return new Vec3d(x, y, z);
  }

  public final Vec3d translation;
  public final Quaternion rotation;
  public final Vec3d scale;

  @SideOnly(Side.CLIENT)
  public org.lwjgl.util.vector.Quaternion lwjglRotation;

  public Transform(Vec3d translation, Quaternion rotation, Vec3d scale) {

    this.translation = translation;
    this.rotation = rotation;
    this.scale = scale;
  }
}
