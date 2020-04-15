!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.PitKiln;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  int burnTimeTicks,          // recipe duration in ticks
  @Optional boolean inherited // true if the recipe should be inherited
);
```


---


```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  int burnTimeTicks,          // recipe duration in ticks
  float failureChance,        // chance for item to fail conversion
  IItemStack[] failureItems,  // array of randomly chosen failure items
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


### Examples

```java
import mods.pyrotech.PitKiln;

// stone in 5 minutes, 33% chance of failure
PitKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 5 * 60 * 20, 0.33, [
    <pyrotech:rock>, // randomly chosen failure items
    <pyrotech:rock> * 2,
    <pyrotech:rock> * 3,
    <pyrotech:rock> * 4,
    <pyrotech:rock> * 5,
    <pyrotech:rock> * 6
]);
```
