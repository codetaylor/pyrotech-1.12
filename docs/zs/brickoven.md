
### Class

```java
import mods.pyrotech.BrickOven
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output,
  IIngredient input 
);
```


---


```java
static void blacklistSmeltingRecipes(
  IIngredient[] output
);
```


---


```java
static void blacklistAllSmeltingRecipes();
```

Blacklist all smelting recipes.

---


```java
static void whitelistSmeltingRecipes(
  IIngredient[] output
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```


---

