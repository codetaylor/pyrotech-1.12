
### Class

```java
import mods.pyrotech.Burn
```

#### Methods

```java
static void addRecipe(
  string name, 
  IItemStack output, 
  string blockString, 
  int burnStages, 
  int totalBurnTimeTicks, 
  ILiquidStack fluidProduced, 
  float failureChance, 
  IItemStack[] failureItems, 
  boolean requiresRefractoryBlocks, 
  boolean fluidLevelAffectsFailureChance
);
```

|Parameter|Description|
|---------|-----------|
|name|the name of the recipe|
|output|the output for each completed burn stage|
|burnStages|the number of burn stages|
|totalBurnTimeTicks|the total number of ticks required to complete all burn stages|
|fluidProduced|the fluid produced for each completed burn stage|
|failureChance|the chance a failure item will be substituted for each burn stage result|
|failureItems|a list of items from which to pick a substitute for each failed burn stage result; items chosen randomly|
|requiresRefractoryBlocks|true if the recipe requires using refractory blocks|
|fluidLevelAffectsFailureChance|true if the build-up of fluid in burning blocks increases the failure chance of burn stages|
{: .zen-description }


```java
static void removeRecipes(IIngredient output);
```

Remove all recipes with the given recipe output.
{: .zen-description }


```java
static void removeAllRecipes();
```

