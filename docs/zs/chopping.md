
### Class

```java
import mods.pyrotech.Chopping
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input  // recipe input
);
```


---


```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int[] chops,       // overrides the default chops array in config
  int[] quantities   // overrides the default quantities array in config
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

