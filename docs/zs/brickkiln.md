
### Class

```java
import mods.pyrotech.BrickKiln
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

