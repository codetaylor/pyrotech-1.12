package com.codetaylor.mc.pyrotech.library.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class FloodFill {

  public interface ICandidatePredicate {

    boolean apply(World world, BlockPos pos);
  }

  public interface IAction {

    boolean execute(World world, BlockPos pos);
  }

  public static boolean apply(
      World world,
      BlockPos pos,
      ICandidatePredicate candidatePredicate,
      IAction action,
      int limit
  ) {

    Deque<BlockPos> candidateQueue = new ArrayDeque<>();
    Set<BlockPos> visitedSet = new HashSet<>();
    boolean result = false;

    candidateQueue.offer(pos);

    BlockPos candidatePos;

    while (limit > 0 && (candidatePos = candidateQueue.poll()) != null) {

      if (visitedSet.contains(candidatePos)) {
        continue;
      }

      visitedSet.add(candidatePos);

      if (candidatePredicate.apply(world, candidatePos)) {
        result = true;

        // perform action
        if (!action.execute(world, candidatePos)) {
          break;
        }

        // reduce limit
        limit -= 1;

        // enqueue neighbors
        for (EnumFacing facing : EnumFacing.VALUES) {
          BlockPos offset = candidatePos.offset(facing);

          if (!visitedSet.contains(offset)) {
            candidateQueue.offer(offset);
          }
        }
      }
    }

    return result;
  }

  private FloodFill() {
    //
  }

}
