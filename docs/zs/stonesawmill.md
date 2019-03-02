
### Class

```java
import mods.pyrotech.StoneSawmill
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int burnTimeTicks, // recipe duration in ticks
  IIngredient blade, // blade(s) used
  int woodChips      // amount of wood chips produced per recipe
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

