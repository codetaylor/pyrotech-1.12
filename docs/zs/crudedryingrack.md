!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.CrudeDryingRack;
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
import mods.pyrotech.CrudeDryingRack;

// dried plant fibers in 5 minutes
CrudeDryingRack.addRecipe("dried_plant_fibers_from_plant_fibers", <pyrotech:material:13>, <pyrotech:material:12>, 5 * 60 * 20);
```
