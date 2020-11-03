## Recipes

The Pyrotech Worktable inherits recipes from the vanilla Crafting Table, but does so on-demand. This means that the recipes aren't actually created until the last possible moment. This dramatically reduces the memory overhead required by the Worktable recipes, but it can make removing recipes a little trickier.

**Remove Exclusive Worktable Recipe**

`Worktable.removeRecipes(IIngredient)` removes any Worktable exclusive recipes that are added by the mod -- *of which there are currently none*. This method exists to remove recipes that might be explicitly added to Pyrotech's Worktable in the future.

**Remove Crafting Table and Worktable Recipe**

All of the recipes that Pyrotech adds to the Worktable are added as vanilla crafting recipes. Using `recipes.remove(IIngredient)` will remove a recipe for both the vanilla Crafting Table and Pyrotech's Worktable.

**Remove Crafting Table Recipe**

To remove a vanilla Crafting Table recipe, but leave the inherited Worktable recipe, remove both recipes using `recipes.remove(IIngredient)` and then manually add the removed recipe back to Pyrotech's Worktable using CraftTweaker.

**Remove Only Worktable Recipe**

To remove an inherited Worktable recipe, but leave the vanilla Crafting Table recipe, use `Worktable.blacklistVanillaRecipes(string[])` supplied with an array of recipe resource locations as strings.

---

## Quality of Life

**Recipe Clear**

If enabled, a player will be allowed to sneak + click using an empty hand to remove all items from the worktable's crafting grid. The removed items will be placed into the player's inventory or on top of the worktable if the player's inventory is full.

This feature must be enabled on the server.

* `module.tech.Basic.cfg`
    * `WORKTABLE_COMMON`
        * `ALLOW_RECIPE_CLEAR`: `false`

**Recipe Repeat**

Worktable quality of life feature: recipe repeat. If enabled, a player will be allowed to sneak + click using a hammer to automatically place items from their inventory into the worktable's crafting grid that match the ingredients for the last recipe completed at the cost of hammer durability.

This feature must be enabled on the server.

* `module.tech.Basic.cfg`
    * `WORKTABLE_COMMON`
        * `ALLOW_RECIPE_REPEAT`: `false`
        * `RECIPE_REPEAT_TOOL_DAMAGE`: `1`