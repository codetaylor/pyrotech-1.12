### Class

```java
import mods.pyrotech.Bloomery
```

#### Methods

```java
static void removeAllBloomeryRecipes();
```


```java
static void removeAllWitherForgeRecipes();
```


```java
static void removeBloomeryRecipes(
  IIngredient output
);
```


```java
static void removeWitherForgeRecipes(
  IIngredient output
);
```


```java
static Bloomery createBloomeryBuilder(
  string name, 
  IItemStack output, 
  IIngredient input
);
```


```java
static Bloomery createWitherForgeBuilder(
  string name, 
  IItemStack output, 
  IIngredient input
);
```

```java
Bloomery setBurnTimeTicks(
  int burnTimeTicks
);
```


```java
Bloomery setFailureChance(
  float failureChance
);
```


```java
Bloomery setBloomYield(
  int min, 
  int max
);
```


```java
Bloomery setSlagItem(
  IItemStack slagItem, 
  int slagCount
);
```


```java
Bloomery addFailureItem(
  IItemStack itemStack, 
  int weight
);
```


```java
Bloomery setLangKey(
  string langKey
);
```


```java
void register();
```

