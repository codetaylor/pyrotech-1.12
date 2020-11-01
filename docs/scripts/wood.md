!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

# Biomes o' Plenty

**Remove Log to Plank Vanilla Crafting Recipes**
by Discord member Woomyn

```
recipes.removeShapeless(<biomesoplenty:planks_0:15> * 4, [ <biomesoplenty:log_3:7>]);
recipes.removeShapeless(<biomesoplenty:planks_0:14> * 4, [ <biomesoplenty:log_3:6>]);
recipes.removeShapeless(<biomesoplenty:planks_0:13> * 4, [ <biomesoplenty:log_3:5>]);
recipes.removeShapeless(<biomesoplenty:planks_0:12> * 4, [ <biomesoplenty:log_3:4>]);
recipes.removeShapeless(<biomesoplenty:planks_0:11> * 4, [ <biomesoplenty:log_2:7>]);
recipes.removeShapeless(<biomesoplenty:planks_0:10> * 4, [ <biomesoplenty:log_2:6>]);
recipes.removeShapeless(<biomesoplenty:planks_0:9> * 4, [ <biomesoplenty:log_2:5>]);
recipes.removeShapeless(<biomesoplenty:planks_0:8> * 4, [ <biomesoplenty:log_2:4>]);
recipes.removeShapeless(<biomesoplenty:planks_0:7> * 4, [ <biomesoplenty:log_1:7>]);
recipes.removeShapeless(<biomesoplenty:planks_0:6> * 4, [ <biomesoplenty:log_1:6>]);
recipes.removeShapeless(<biomesoplenty:planks_0:5> * 4, [ <biomesoplenty:log_1:5>]);
recipes.removeShapeless(<biomesoplenty:planks_0:4> * 4, [ <biomesoplenty:log_1:4>]);
recipes.removeShapeless(<biomesoplenty:planks_0:3> * 4, [ <biomesoplenty:log_0:7>]);
recipes.removeShapeless(<biomesoplenty:planks_0:2> * 4, [ <biomesoplenty:log_0:6>]);
recipes.removeShapeless(<biomesoplenty:planks_0:1> * 4, [ <biomesoplenty:log_0:5>]);
recipes.removeShapeless(<biomesoplenty:planks_0> * 4, [ <biomesoplenty:log_0:4>]);
```

# Chopping Block

**Cap Plank to Slab Output at 2**
by Discord member nihiltres

By default, Pyrotech's Chopping Block can output more than two wooden slabs from one plank when using a higher quality axe. Quark provides recipes to combine two wooden slabs back into a plank and creates a dupe condition.

The following ZenScript was written to circumvent this issue.

You can read more about the issue here: [#325](https://github.com/codetaylor/pyrotech-1.12/issues/325) 

```
//Fix Chopping Block slab recipes to max out at 2 slabs per plank
	//Generate your own lines automagically from the wood compat file with some regex:
	//"([^:"]+):([^:"]+):([^:"]+)": "([^:"]+):([^:"]+slab[^:"]*):([^:"]+)",?
	//setSafeSlabChopping\("$4_$5_$6", <$4:$5:$6>, <$1:$2:$3>\);
	function setSafeSlabChopping(name as string, output as IItemStack, input as IIngredient) {
		Chopping.removeRecipes(output);
		Chopping.addRecipe(name, output, input, [6, 4, 2, 2], [1, 2, 2, 2], true);
	}
	//Minecraft
	setSafeSlabChopping(     "slab_oak", <minecraft:wooden_slab:0>, <minecraft:planks:0>);
	setSafeSlabChopping(  "slab_spruce", <minecraft:wooden_slab:1>, <minecraft:planks:1>);
	setSafeSlabChopping(   "slab_birch", <minecraft:wooden_slab:2>, <minecraft:planks:2>);
	setSafeSlabChopping(  "slab_jungle", <minecraft:wooden_slab:3>, <minecraft:planks:3>);
	setSafeSlabChopping(  "slab_acacia", <minecraft:wooden_slab:4>, <minecraft:planks:4>);
	setSafeSlabChopping("slab_dark_oak", <minecraft:wooden_slab:5>, <minecraft:planks:5>);
	//The Betweenlands
	setSafeSlabChopping( "slab_giant_root",  <thebetweenlands:giant_root_plank_slab:0>,  <thebetweenlands:giant_root_planks:0>);
	setSafeSlabChopping("slab_hearthgrove", <thebetweenlands:hearthgrove_plank_slab:0>, <thebetweenlands:hearthgrove_planks:0>);
	setSafeSlabChopping( "slab_nibbletwig",  <thebetweenlands:nibbletwig_plank_slab:0>,  <thebetweenlands:nibbletwig_planks:0>);
	setSafeSlabChopping("slab_rubber_tree", <thebetweenlands:rubber_tree_plank_slab:0>, <thebetweenlands:rubber_tree_planks:0>);
	setSafeSlabChopping(   "slab_weedwood",    <thebetweenlands:weedwood_plank_slab:0>,    <thebetweenlands:weedwood_planks:0>);
	//BOP
	setSafeSlabChopping("slab_sacred_oak", <biomesoplenty:wood_slab_0:0>, <biomesoplenty:planks_0:0>);
	setSafeSlabChopping(    "slab_cherry", <biomesoplenty:wood_slab_0:1>, <biomesoplenty:planks_0:1>);
	setSafeSlabChopping(    "slab_umbran", <biomesoplenty:wood_slab_0:2>, <biomesoplenty:planks_0:2>);
	setSafeSlabChopping(       "slab_fir", <biomesoplenty:wood_slab_0:3>, <biomesoplenty:planks_0:3>);
	setSafeSlabChopping(  "slab_ethereal", <biomesoplenty:wood_slab_0:4>, <biomesoplenty:planks_0:4>);
	setSafeSlabChopping(     "slab_magic", <biomesoplenty:wood_slab_0:5>, <biomesoplenty:planks_0:5>);
	setSafeSlabChopping(  "slab_mangrove", <biomesoplenty:wood_slab_0:6>, <biomesoplenty:planks_0:6>);
	setSafeSlabChopping(      "slab_palm", <biomesoplenty:wood_slab_0:7>, <biomesoplenty:planks_0:7>);
	setSafeSlabChopping(   "slab_redwood", <biomesoplenty:wood_slab_1:0>, <biomesoplenty:planks_0:8>);
	setSafeSlabChopping(    "slab_willow", <biomesoplenty:wood_slab_1:1>, <biomesoplenty:planks_0:9>);
	setSafeSlabChopping(      "slab_pine", <biomesoplenty:wood_slab_1:2>, <biomesoplenty:planks_0:10>);
	setSafeSlabChopping(  "slab_hellbark", <biomesoplenty:wood_slab_1:3>, <biomesoplenty:planks_0:11>);
	setSafeSlabChopping( "slab_jacaranda", <biomesoplenty:wood_slab_1:4>, <biomesoplenty:planks_0:12>);
	setSafeSlabChopping(  "slab_mahogany", <biomesoplenty:wood_slab_1:5>, <biomesoplenty:planks_0:13>);
	setSafeSlabChopping(     "slab_ebony", <biomesoplenty:wood_slab_1:6>, <biomesoplenty:planks_0:14>);
	setSafeSlabChopping("slab_eucalyptus", <biomesoplenty:wood_slab_1:7>, <biomesoplenty:planks_0:15>);
	//Mystical World
	setSafeSlabChopping("slab_charred", <mysticalworld:charred_slab:0>, <mysticalworld:charred_planks:0>);
	//Rustic
	setSafeSlabChopping(   "slab_olive",    <rustic:olive_slab_item:0>, <rustic:planks:0>);
	setSafeSlabChopping("slab_ironwood", <rustic:ironwood_slab_item:0>, <rustic:planks:1>);
//----
```