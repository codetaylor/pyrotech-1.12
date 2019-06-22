
### Class

```java
import mods.pyrotech.BrickCrucible;
```

#### Methods

```java
static void addRecipe(
  string name,         // unique recipe name
  ILiquidStack output, // recipe output
  IIngredient input,   // recipe input
  int burnTimeTicks    // recipe duration in ticks
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


### Examples

```java
import mods.pyrotech.BrickCrucible;

// melt ice block into a bucket of water in one minute
BrickCrucible.addRecipe("water_from_ice", <liquid:water> * 1000, <minecraft:ice>, 1 * 60 * 20);

// melt cobblestone block into a bucket of lava in five minutes
BrickCrucible.addRecipe("lava_from_cobblestone", <liquid:lava> * 1000, <minecraft:cobblestone>, 5 * 60 * 20);
```
