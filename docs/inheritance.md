# Recipe Inheritance

Many of Pyrotech's devices allow for their more advanced counterparts to inherit recipes.

For example, a recipe for the Crude Drying Rack might be inherited like so: `Crude Drying Rack -> Drying Rack -> Stone Oven -> Refractory Oven`

Pyrotech uses config values to transform recipes as they're passed down the chain of inheritance.

For example, the Drying Rack has a recipe duration modifier, `INHERITED_CRUDE_DRYING_RACK_RECIPE_DURATION_MODIFIER`, that is applied to recipes inherited from the Crude Drying Rack.

## Pyrotech Recipes

The recipes that Pyrotech adds are subject to inheritance.

Inheritance for a device's recipes can be disabled in the config.

For example, the Drying Rack has a toggle, `INHERIT_CRUDE_DRYING_RACK_RECIPES`.

Disabling inheritance for devices in the config will only disable inheritance for recipes added by the mod. It will not disable inheritance for recipes added by CraftTweaker.

Any recipes that Pyrotech adds can be disabled using CraftTweaker, including inherited recipes.

## CraftTweaker Recipes

Many devices' CraftTweaker recipe methods allow passing an optional boolean parameter, `inherited`.

Passing true for this parameter will signal that the recipe being registered is intended to be inherited by devices in the inheritance chain, while applying the same recipe transform values found in the configs.

For example, using CraftTweaker to create an inherited Crude Drying Rack recipe would also cause a new recipe to be created for the Drying Rack with a duration transformed by the Drying Rack's `INHERITED_CRUDE_DRYING_RACK_RECIPE_DURATION_MODIFIER` config value. This would subsequently cause a new recipe to be created for the Stone Oven and then the Refractory Oven with the appropriate recipe transforms.

## Inheritance Chains

Below is a list of Pyrotech's recipe inheritance chains.

Creating an inherited CraftTweaker recipe for any of the devices that are not the last device in their chain will also make a transformed recipe for each device that is downstream in their chain.

* Crude Drying Rack -> Drying Rack -> Stone Oven -> Refractory Oven
* Pit Kiln -> Stone Kiln -> Refractory Kiln
* Chopping Block -> Stone Sawmill -> Refractory Sawmill
* Stone Crucible -> Refractory Crucible
* Compacting Bin -> Mechanical Compactor
* Bloomery -> Wither Forge