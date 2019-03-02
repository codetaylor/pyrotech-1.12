
### Class

```java
import mods.pyrotech.StoneCrucible
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

