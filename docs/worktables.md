### Recipe Clear

If enabled, a player will be allowed to sneak + click using an empty hand to remove all items from the worktable's crafting grid. The removed items will be placed into the player's inventory or on top of the worktable if the player's inventory is full.

This feature must be enabled on the server.

* `module.tech.Basic.cfg`
    * `WORKTABLE_COMMON`
        * `ALLOW_RECIPE_CLEAR`: `false`

### Recipe Repeat

Worktable quality of life feature: recipe repeat. If enabled, a player will be allowed to sneak + click using a hammer to automatically place items from their inventory into the worktable's crafting grid that match the ingredients for the last recipe completed at the cost of hammer durability.

This feature must be enabled on the server.

* `module.tech.Basic.cfg`
    * `WORKTABLE_COMMON`
        * `ALLOW_RECIPE_REPEAT`: `false`
        * `RECIPE_REPEAT_TOOL_DAMAGE`: `1`