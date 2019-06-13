
### Class

```java
import mods.pyrotech.SoakingPot;
```

#### Methods

```java
static void addRecipe(
  string name,             // unique recipe name
  IItemStack output,       // recipe output
  ILiquidStack inputFluid, // input fluid
  IIngredient inputItem,   // input item
  int timeTicks            // recipe duration in ticks
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

