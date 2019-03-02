
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

|Parameter|Description|
|---------|-----------|
|name|the name of the recipe|
|output|the recipe output|
|input|the recipe input|
|chops|the int array provided here will override the array provided in the config file; see the config file for an explanation|
|quantities|the int array provided here will override the array provided in the config file; see the config file for an explanation|

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

