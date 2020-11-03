
### Class

```java
import mods.pyrotech.StoneCrucible;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  ILiquidStack output,        // recipe output
  IIngredient input,          // recipe input
  int burnTimeTicks,          // recipe duration in ticks
  @Optional boolean inherited // true if the recipe should be inherited
);
```


---


```java
static void removeRecipes(
  ILiquidStack output // output ingredient to match
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

```java
import mods.pyrotech.StoneCrucible;

// melt ice block into a bucket of water in one minute
StoneCrucible.addRecipe("water_from_ice", <liquid:water> * 1000, <minecraft:ice>, 1 * 60 * 20);
```
