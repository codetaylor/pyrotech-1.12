
### Class

```java
import mods.pyrotech.DryingRack;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  int dryTimeTicks,           // recipe duration in ticks
  @Optional boolean inherited // true if the recipe should be inherited
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


```java
static void setBiomeSpeed(
  float speed,
  string biome
);
```

Sets the device's base speed in the given biome.

---


```java
static void setBiomeSpeed(
  float speed,   
  string[] biomes
);
```

Sets the device's base speed in the given biomes.

---


### Examples

```java
import mods.pyrotech.DryingRack;

// dried plant fibers in 30 seconds
DryingRack.addRecipe("dried_plant_fibers_from_plant_fibers", <pyrotech:material:13>, <pyrotech:material:12>, 30 * 20);
```
