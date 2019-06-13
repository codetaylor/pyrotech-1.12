
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


### Examples

```java
import mods.pyrotech.BrickSawmill;

// cut oak logs into oak planks in 10 seconds using an iron or diamond sawblade
// and produce 8 wood chips
BrickSawmill.addRecipe("oak_planks_from_oak_logs", <minecraft:planks:0>, <minecraft:log:0>, 200, <pyrotech:sawmill_blade_iron>.or(<pyrotech:sawmill_blade_diamond>), 8);
```
