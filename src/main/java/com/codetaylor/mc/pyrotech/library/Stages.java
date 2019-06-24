package com.codetaylor.mc.pyrotech.library;

import java.util.*;

public class Stages {

  public enum Mode {
    OR, AND, NOT
  }

  private final Mode mode;
  private final List<Object> list;

  private Stages(Mode mode, List<Object> list) {

    this.mode = mode;
    this.list = Collections.unmodifiableList(this.toLowercase(list));
  }

  private List<Object> toLowercase(List<Object> list) {

    List<Object> result = new ArrayList<>(list.size());

    for (Object o : list) {

      if (o instanceof String) {
        result.add(((String) o).toLowerCase());

      } else {
        result.add(o);
      }
    }

    return result;
  }

  public Mode getMode() {

    return this.mode;
  }

  public List<Object> getList() {

    return this.list;
  }

  public boolean allowed(Collection<String> playerStages) {

    return Stages.allowed(playerStages, this);
  }

  private static boolean allowed(Collection<String> playerStages, Stages stages) {

    if (stages.mode == Mode.AND) {

      for (Object o : stages.list) {

        if (o instanceof String) {

          if (!playerStages.contains(o)) {
            return false;
          }

        } else if (o instanceof Stages) {

          if (!Stages.allowed(playerStages, (Stages) o)) {
            return false;
          }
        }
      }

      return true;

    } else if (stages.mode == Mode.OR) {

      for (Object o : stages.list) {

        if (o instanceof String) {

          if (playerStages.contains(o)) {
            return true;
          }

        } else if (o instanceof Stages) {

          if (Stages.allowed(playerStages, (Stages) o)) {
            return true;
          }
        }
      }

      return false;

    } else if (stages.mode == Mode.NOT) {

      for (Object o : stages.list) {

        if (o instanceof String) {

          if (playerStages.contains(o)) {
            return false;
          }

        } else if (o instanceof Stages) {

          if (Stages.allowed(playerStages, (Stages) o)) {
            return false;
          }
        }
      }

      return true;
    }

    return false;
  }

  public static Stages and(Object[] stages) {

    return new Stages(Mode.AND, Arrays.asList(stages));
  }

  public static Stages or(Object[] stages) {

    return new Stages(Mode.OR, Arrays.asList(stages));
  }

  public static Stages not(String stage) {

    return new Stages(Mode.NOT, Collections.singletonList(stage));
  }

  public static Stages not(Stages stages) {

    return new Stages(Mode.NOT, Collections.singletonList(stages));
  }
}
