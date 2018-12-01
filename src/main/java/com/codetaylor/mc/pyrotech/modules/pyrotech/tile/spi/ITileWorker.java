package com.codetaylor.mc.pyrotech.modules.pyrotech.tile.spi;

public interface ITileWorker {

  /**
   * @return true if this worker is active and should run its update
   */
  boolean workerIsActive();

  /**
   * The worker's active flag determines if it should run it's update.
   *
   * @param active set to true to activate this worker's update
   */
  void workerSetActive(boolean active);

  /**
   * Complete one tick worth of work inside this method.
   * <p>
   * Will only be called if the worker is active.
   * <p>
   * Only called on the server.
   *
   * @return true if the worker should remain active, false to deactivate
   */
  boolean workerDoWork();

  /**
   * @return a value [0,1] that reflects this worker's work progress
   */
  float workerGetProgress();

  /**
   * By default, this method returns false.
   * <p>
   * Fuel is a loose term used to describe anything that this worker consumes
   * in order to remain active.
   *
   * @return true if the worker needs to consume fuel
   */
  default boolean workerRequiresFuel() {

    return false;
  }

  /**
   * By default, this method returns true;
   * <p>
   * Fuel is a loose term used to describe anything that this worker consumes
   * in order to remain active.
   *
   * @return true if the worker was able to consume fuel and should remain active, false to deactivate
   */
  default boolean workerConsumeFuel() {

    return true;
  }

}
