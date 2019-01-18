
### Class

```java
import mods.pyrotech.Chopping
```

#### Methods

```java
static void addRecipe(string name, IItemStack output, IIngredient input);
```


```java
static void addRecipe(
  string name, 
  IItemStack output, 
  IIngredient input, 
  int[] chops, 
  int[] quantities
);
```

|Parameter|Description|
|---------|-----------|
|name|the name of the recipe|
|output|the recipe output|
|input|the recipe input|
|chops|the int array provided here will override the array provided in the config file; see the config file for an explanation|
|quantities|the int array provided here will override the array provided in the config file; see the config file for an explanation|
{: .zen-description }


```java
static void removeRecipes(IIngredient output);
```

