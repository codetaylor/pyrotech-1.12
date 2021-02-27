
### Class

```java
import mods.pyrotech.TanningRack;
```

#### Methods

```java
static void addRecipe(
  string name,            // unique recipe name
  IItemStack output,      // recipe output
  IIngredient input,      // recipe input
  IItemStack failureItem, // rain failure item
  int timeTicks           // recipe duration in ticks
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

```java
import mods.pyrotech.TanningRack;

// diamond to emerald in 10 minutes with an iron ingot failure item
TanningRack.addRecipe(
  "emerald_from_diamond",
  <minecraft:emerald>,
  <minecraft:diamond>,
  <minecraft:iron_ingot>,
  10 * 60 * 20
);
```
