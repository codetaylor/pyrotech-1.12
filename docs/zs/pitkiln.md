
### Class

```java
import mods.pyrotech.PitKiln;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int burnTimeTicks  // recipe duration in ticks
);
```


---


```java
static void addRecipe(
  string name,              // unique recipe name
  IItemStack output,        // recipe output
  IIngredient input,        // recipe input
  int burnTimeTicks,        // recipe duration in ticks
  float failureChance,      // chance for item to fail conversion
  IItemStack[] failureItems // array of randomly chosen failure items
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
