
### Class

```java
import mods.pyrotech.MechanicalCompactor
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int amount         // number of input items required
);
```


---


```java
static void addRecipe(
  string name,           // unique recipe name
  IItemStack output,     // recipe output
  IIngredient input,     // recipe input
  int amount,            // number of input items required
  int[] toolUsesRequired // overrides default provided in config
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

