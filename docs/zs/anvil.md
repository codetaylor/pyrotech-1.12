
### Class

```java
import mods.pyrotech.Anvil
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int hits,          // base number of hammer hits required
  string type        // hammer | pickaxe
);
```


---


```java
static void removeRecipes(
  IIngredient output // recipe output to match
);
```


---


```java
static void removeAllRecipes();
```


---

