
### Class

```java
import mods.pyrotech.Campfire
```

#### Methods

```java
static void addRecipe(string name, IItemStack output, IIngredient input);
```


```java
static void blacklistSmeltingRecipes(IIngredient[] output);
```

|Parameter|Description|
|---------|-----------|
|output|furnace recipes that have an output that matches any of the given ingredients will be disallowed|

```java
static void whitelistSmeltingRecipes(IIngredient[] output);
```

|Parameter|Description|
|---------|-----------|
|output|only furnace recipes that have an output that matches any of the given ingredients will be allowed|

```java
static void removeRecipes(IIngredient output);
```

Remove all recipes with the given recipe output.
