!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

### Class

```java
import mods.pyrotech.MechanicalCompactor;
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
import mods.pyrotech.MechanicalCompactor;

CompactingBin.addRecipe("ash_pile_from_ash", <pyrotech:pile_ash>, <pyrotech:material:0>, 8);
```
