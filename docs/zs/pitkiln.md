
### Class

```java
import mods.pyrotech.PitKiln
```

#### Methods

```java
static void addRecipe(
  string name, 
  IItemStack output, 
  IIngredient input, 
  int burnTimeTicks
);
```


```java
static void addRecipe(
  string name, 
  IItemStack output, 
  IIngredient input, 
  int burnTimeTicks, 
  float failureChance, 
  IItemStack[] failureItems
);
```


```java
static void removeRecipes(
  IIngredient output
);
```


```java
static void removeAllRecipes();
```

