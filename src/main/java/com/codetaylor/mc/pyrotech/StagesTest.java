package com.codetaylor.mc.pyrotech;

import com.codetaylor.mc.athenaeum.integration.gamestages.Stages;

import java.util.Arrays;
import java.util.List;

public class StagesTest {

  public static void main(String[] args) {

    List<String> playerStages = Arrays.asList(
        "A", "B", "C"
    );

    System.out.println(Stages.and(new Object[]{"A", "B"}).allowed(playerStages));
    System.out.println(!Stages.and(new Object[]{"A", "B", Stages.not("C")}).allowed(playerStages));
    System.out.println(Stages.and(new Object[]{"A", "B", Stages.not(Stages.and(new Object[]{"C", "D"}))}).allowed(playerStages));

    System.out.println(Stages.or(new Object[]{"A", "D"}).allowed(playerStages));
    System.out.println(Stages.or(new Object[]{"D", "E", Stages.not("F")}).allowed(playerStages));

  }

}
