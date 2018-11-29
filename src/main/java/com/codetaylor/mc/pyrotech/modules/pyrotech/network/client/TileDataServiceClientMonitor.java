package com.codetaylor.mc.pyrotech.modules.pyrotech.network.client;

import com.codetaylor.mc.pyrotech.modules.pyrotech.network.TileDataServiceLogger;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class TileDataServiceClientMonitor {

  // ---------------------------------------------------------------------------
  // - Events
  // ---------------------------------------------------------------------------

  // TODO: Config
  private static final short UPDATE_INTERVAL_TICKS = 20;
  // TODO: Config
  private static final short TOTAL_INTERVAL_COUNT = 120;

  private static final int CACHE_CLEANUP_INTERVAL_TICKS = 10 * 20;

  /**
   * Monitors all network traffic for all tile data services.
   */
  public static final TileDataServiceClientMonitor TOTAL;

  /**
   * Monitors network traffic, indexed by world position.
   */
  private static final LoadingCache<BlockPos, TileDataServiceClientMonitor> PER_POS_TOTAL;

  private static final CacheLoader<BlockPos, TileDataServiceClientMonitor> CACHE_LOADER = new CacheLoader<BlockPos, TileDataServiceClientMonitor>() {

    public TileDataServiceClientMonitor load(@Nonnull BlockPos pos) {

      return new TileDataServiceClientMonitor(UPDATE_INTERVAL_TICKS, TOTAL_INTERVAL_COUNT);
    }
  };

  private static int cacheCleanupCounter;

  static {
    TOTAL = new TileDataServiceClientMonitor(UPDATE_INTERVAL_TICKS, TOTAL_INTERVAL_COUNT);

    PER_POS_TOTAL = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build(CACHE_LOADER);
  }

  @SubscribeEvent
  public static void onEvent(TickEvent.ClientTickEvent event) {

    // Update all the monitors when the client ticks, limit phase so it only
    // updates once.

    if (event.phase == TickEvent.Phase.START) {
      TOTAL.update();

      for (TileDataServiceClientMonitor value : PER_POS_TOTAL.asMap().values()) {
        value.update();
      }

      cacheCleanupCounter += 1;

      if (cacheCleanupCounter >= CACHE_CLEANUP_INTERVAL_TICKS) {
        cacheCleanupCounter = 0;
        PER_POS_TOTAL.cleanUp();
      }
    }
  }

  /**
   * Called when a packet from the tile entity data service is received on
   * the client.
   *
   * @param serviceId the service index
   * @param pos       the pos of the TE
   * @param size      the size of the packet's TE update buffer in bytes
   */
  public static void onClientPacketReceived(int serviceId, BlockPos pos, int size) {

    // --- Total ---

    TOTAL.receiveBytes(size);

    // --- Per Position ---

    TileDataServiceClientMonitor monitor = null;

    try {
      monitor = PER_POS_TOTAL.get(pos);

    } catch (ExecutionException e) {
      TileDataServiceLogger.LOGGER.error("", e);
    }

    if (monitor != null) {
      monitor.receiveBytes(size);
    }
  }

  // ---------------------------------------------------------------------------
  // - Static Accessors
  // ---------------------------------------------------------------------------

  @Nullable
  public static TileDataServiceClientMonitor findMonitorForPosition(BlockPos pos) {

    if (PER_POS_TOTAL.asMap().containsKey(pos)) {

      try {
        return PER_POS_TOTAL.get(pos);

      } catch (ExecutionException e) {
        TileDataServiceLogger.LOGGER.error("", e);
      }
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Monitor Instance
  // ---------------------------------------------------------------------------

  private final IntArrayList totalBytesReceivedPerSecond;
  private final int updateIntervalTicks;
  private final int totalIntervalCount;

  private int totalBytesReceived;
  private short tickCounter;

  public TileDataServiceClientMonitor(short updateIntervalTicks, short totalIntervalCount) {

    totalBytesReceivedPerSecond = new IntArrayList(totalIntervalCount);
    this.updateIntervalTicks = updateIntervalTicks;
    this.totalIntervalCount = totalIntervalCount;
  }

  /**
   * Call once per tick to update the monitor.
   */
  public void update() {

    this.tickCounter += 1;

    if (this.tickCounter >= this.updateIntervalTicks) {
      this.tickCounter = 0;
      this.totalBytesReceivedPerSecond.add(0, this.totalBytesReceived);
      this.totalBytesReceived = 0;

      if (this.totalBytesReceivedPerSecond.size() > this.totalIntervalCount) {
        this.totalBytesReceivedPerSecond.removeInt(this.totalBytesReceivedPerSecond.size() - 1);
      }
    }
  }

  private void receiveBytes(int size) {

    this.totalBytesReceived += size;
  }

  public int size() {

    return this.totalBytesReceivedPerSecond.size();
  }

  public int get(int index) {

    return this.totalBytesReceivedPerSecond.getInt(index);
  }

  public int getTotalIntervalCount() {

    return this.totalIntervalCount;
  }

}
