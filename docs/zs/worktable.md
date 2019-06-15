
### Class

```java
import mods.pyrotech.Worktable;
```

#### Methods

```java
static Worktable buildShaped(
  IItemStack output,         
  IIngredient[][] ingredients
);
```


---


```java
static Worktable buildShapeless(
  IItemStack output,       
  IIngredient[] ingredients
);
```


---


```java
static void addShaped(
  string name,                       
  IItemStack output,                 
  IIngredient[][] ingredients,       
  IIngredient tool,                  
  int toolDamage,                    
  @Optional boolean mirrored,        
  @Optional boolean hidden,          
  @Optional IRecipeFunction function,
  @Optional IRecipeAction action     
);
```


---


```java
static void addShapeless(
  string name,                       
  IItemStack output,                 
  IIngredient[] ingredients,         
  IIngredient tool,                  
  int toolDamage,                    
  @Optional boolean hidden,          
  @Optional IRecipeFunction function,
  @Optional IRecipeAction action     
);
```


---


```java
static void blacklistVanillaRecipes(
  string[] resourceLocations
);
```


---


```java
static void blacklistAllVanillaRecipes();
```

Blacklist all vanilla crafting recipes.

---


```java
static void whitelistVanillaRecipes(
  string[] resourceLocations
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```

Removes pre-existing recipes, ie. recipes added by the mod.

---

```java
Worktable setName(
  string name
);
```


---


```java
Worktable setTool(
  IIngredient tool,
  int toolDamage   
);
```


---


```java
Worktable setMirrored(
  boolean mirrored
);
```


---


```java
Worktable setHidden(
  boolean hidden
);
```


---


```java
Worktable setRecipeFunction(
  IRecipeFunction recipeFunction
);
```


---


```java
Worktable setRecipeAction(
  IRecipeAction recipeAction
);
```


---

