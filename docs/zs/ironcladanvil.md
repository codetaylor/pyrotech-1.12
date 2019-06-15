
### Class

```java
import mods.pyrotech.IroncladAnvil;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int hits,          // base number of hammer hits required
  string type        // hammer | pickaxe
);
```


---


```java
static void removeRecipes(
  IIngredient output // recipe output to match
);
```


---


```java
static void removeAllRecipes();
```


---


### Examples

```java
import mods.pyrotech.IroncladAnvil;

IroncladAnvil.addRecipe("cobblestone_from_stone", <minecraft:cobblestone>, <minecraft:stone>, 8, "hammer");
```
