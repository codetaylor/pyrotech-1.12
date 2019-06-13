package com.codetaylor.mc.pyrotech.interaction.api;

public class Quaternion {

  public float x;
  public float y;
  public float z;
  public float w;

  public Quaternion() {
    //
  }

  public Quaternion(Quaternion rotation) {

    this.x = rotation.x;
    this.y = rotation.y;
    this.z = rotation.z;
    this.w = rotation.w;
  }

  /**
   * Sets the given {@link Quaternion} from the given axis angle.
   *
   * @param x
   * @param y
   * @param z
   * @param angle the angle in radians
   * @return the given {@link Quaternion}
   */
  public Quaternion setFromAxisAngle(float x, float y, float z, float angle) {

    this.x = x;
    this.y = y;
    this.z = z;
    float n = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    // zero-div may occur.
    float s = (float) (Math.sin(0.5 * angle) / n);
    this.x *= s;
    this.y *= s;
    this.z *= s;
    this.w = (float) Math.cos(0.5 * angle);

    return this;
  }

  public Quaternion multLocal(Quaternion q1) {

    return this.mult(q1, this);
  }

  /**
   * Multiplies q0 by q1. The result is returned in res. It should be noted that
   * quaternion multiplication is not commutative so q * p != p * q.
   * <p>
   * It is safe if this == res || q1 == res.
   *
   * @param q1  the quaternion to multiply
   * @param res the quaternion to store the result in.
   * @return the new quaternion.
   */
  public Quaternion mult(Quaternion q1, Quaternion res) {

    if (res == null) {
      res = new Quaternion();
    }
    float q0w = this.w, q0x = this.x, q0y = this.y, q0z = this.z;
    float q1w = q1.w, q1x = q1.x, q1y = q1.y, q1z = q1.z;
    res.x = q0x * q1w + q0y * q1z - q0z * q1y + q0w * q1x;
    res.y = -q0x * q1z + q0y * q1w + q0z * q1x + q0w * q1y;
    res.z = q0x * q1y - q0y * q1x + q0z * q1w + q0w * q1z;
    res.w = -q0x * q1x - q0y * q1y - q0z * q1z + q0w * q1w;
    return res;
  }
}
