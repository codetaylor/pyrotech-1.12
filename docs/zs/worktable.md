
### Class

```java
import mods.pyrotech.Worktable
```

#### Methods

```java
static void addShaped(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function, IRecipeAction action);
```


```java
static void addShaped(
  string name, 
  IItemStack output, 
  IIngredient[][] ingredients, 
  IRecipeFunction function, 
  IRecipeAction action
);
```


```java
static void addShapedMirrored(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function, IRecipeAction action);
```


```java
static void addShapedMirrored(
  string name, 
  IItemStack output, 
  IIngredient[][] input, 
  IRecipeFunction function, 
  IRecipeAction action
);
```


```java
static void addShapeless(IItemStack output, IIngredient[] ingredients, IRecipeFunction function, IRecipeAction action);
```


```java
static void addShapeless(
  string name, 
  IItemStack output, 
  IIngredient[] ingredients, 
  IRecipeFunction function, 
  IRecipeAction action
);
```


```java
static void blacklistVanillaRecipes(string[] resourceLocations);
```


```java
static void whitelistVanillaRecipes(string[] resourceLocations);
```


```java
static void removeRecipes(IIngredient output);
```

