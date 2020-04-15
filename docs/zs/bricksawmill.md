!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.BrickSawmill;
```

#### Methods

```java
static void addRecipe(
  string name,            // unique recipe name
  IItemStack output,      // recipe output
  IIngredient input,      // recipe input
  int burnTimeTicks,      // recipe duration in ticks
  IIngredient blade,      // blade(s) used
  @Optional int woodChips // amount of wood chips produced per recipe
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```


---


```java
static void removeAllRecipes();
```


---


```java
static void setGameStages(
  Stages stages // game stages
);
```

Sets game stage logic required to use the device.

---


### Examples

It is important to use `:*` for the meta value of the sawblades to ensure that the blade will continue to be valid after taking damaged.

```java
import mods.pyrotech.BrickSawmill;

// cut oak logs into oak planks in 10 seconds using an iron or diamond sawblade
// and produce 8 wood chips
BrickSawmill.addRecipe("oak_planks_from_oak_logs", <minecraft:planks:0>, <minecraft:log:0>, 200, <pyrotech:sawmill_blade_iron:*>.or(<pyrotech:sawmill_blade_diamond:*>), 8);
```
