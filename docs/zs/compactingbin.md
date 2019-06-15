
### Class

```java
import mods.pyrotech.CompactingBin;
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


### Examples

```java
import mods.pyrotech.CompactingBin;

CompactingBin.addRecipe("ash_pile_from_ash", <pyrotech:pile_ash>, <pyrotech:material:0>, 8);
```
