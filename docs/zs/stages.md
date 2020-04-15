!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.Stages;
```

#### Methods

```java
static Stages and(
  Object[] stages // accepts an array of string or Stages objects
);
```


---


```java
static Stages or(
  Object[] stages // accepts an array of string or Stages objects
);
```


---


```java
static Stages not(
  string stage
);
```


---


```java
static Stages not(
  Stages stages
);
```


---


### Examples

```java
import mods.pyrotech.Stages;

// allow if player has stageA
Stages.and(["stageA"]);

// allow if player does not have both stageA and stageB
Stages.not(Stages.and(["stageA", "stageB"]));

// allow if player has both stageA and stageB and not stageC
Stages.and(["stageA", "stageB", Stages.not("stageC")]);

// allow if player has either stageA or stageB and not stageC
Stages.and([Stages.or(["stageA", "stageB"]), Stages.not("stageC")]);

// allow if player has either stageA or stageB or not (stageC and stageD)
Stages.or(["stageA", "stageB", Stages.not(Stages.and(["stageC", "stageD"]))]);
```

```java
import mods.pyrotech.Stages;
import mods.pyrotech.Worktable;

Worktable.setGameStages(Stages.and(["stageA"]));
```
