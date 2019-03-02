
### Class

```java
import mods.pyrotech.Campfire
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
static void blacklistSmeltingRecipes(
  IIngredient[] output // output ingredients to blacklist
);
```


---


```java
static void whitelistSmeltingRecipes(
  IIngredient[] output // output ingredients to whitelist
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```

Remove all recipes with the given recipe output.

---

