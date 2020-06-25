!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

The first time Pyrotech is loaded, it will generate some data and prompt you to reload.

---

### Ore Compatibility System

When Pyrotech is loaded, it will derive some data based on the contents of pre-defined oredicts and save that data to a file: `core.compat.Ore-Generated.json`. This file will be overwritten each time Pyrotech loads in order to provide the user with any generated changes.

When this file is created, it will also create another file if it doesn't already exist: `core.compat.Ore-Custom.json`. When the custom file is first created, it is simply a copy of the generated file. The custom file is the data that Pyrotech actually uses and will never be overwitten.

The data in the custom file is used to generate slag and bloomery recipes automatically.

**Pre-defined Oredicts**

These are the oredicts that Pyrotech uses when it creates `core.compat.Ore-Generated.json`. If your ore does not have an entry in one of these oredicts with a corresponding nugget entry in the `nugget*` oredict, you will have to add it to the `core.compat.Ore-Custom.json` file manually.

```
oreAluminum
oreArdite
oreCobalt
oreCopper
oreGold
oreIridium
oreIron
oreLead
oreMithril
oreNickel
oreOctine
oreOsmium
orePlatinum
oreSilver
oreSyrmorite
oreTin
oreUranium
```

**Ore Compat Entries**
```js
"oreGold": {
  "slagColor": "fcee4b",
  "langKey": [
    "minecraft:tile.oreGold"
  ],
  "output": [
    "minecraft:gold_nugget:0"
  ]
}
```

An entry in the ore compat file, shown above, consists of an oredict key, a hexidecimal slag color, a list of lang keys, and a list of output items.

The oredict key, `oreGold` in the example, must correspond to an existing oredict as it will be used for the bloomery recipe's input.

The `slagColor` is a hexidecimal string without the `#`. This defines the color used for the generated slag.

The `langKey` entries are strings in the format `(domain):(key)`. The domain is added by the generator and is useful for determining which mod a lang key came from. The domain is purely optional if you're adding entries manually. Entries in the format `(key)` will work just fine. The first valid lang key found in the list will be the lang key used for the generated content.

!!! hint
    Use the command, `/ptlang`, to copy the lang key for a held item to the clipboard.

The `output` list defines a recipe output for the generated recipes. These strings are in the familiar item string format `(domain):(path):(meta)`. Again, like the lang key list, the first item from the list that is valid will be used for the output of generated recipes. You can select which item you want to use by moving it to the top of the list or deleting all other entries.

### Adding Slag Only

!!! note
    It is recommended that you try to implement your compat using the compat files discussed above.

If you would like to just generate some slag, however, use the `module.tech.Bloomery.Slag-Custom.json` file.

The `module.tech.Bloomery.Slag-*.json` files are an artifact from before the ore compat was implemented. The custom file is still relevant, meaning it will still generate slag items. The generated file, however, won't have any contents because the mod doesn't use it to generate its slag anymore.

```js
{
  "registryName": "gold",
  "langKey": "tile.oreGold",
  "color": "fcee4b"
}
```

A slag entry, shown above, consists of a registry name, lang key, and hexadecimal color. The `registryName` is used to register the slag, the `langKey` is used to derive the display names of the slag, and the `color` is used to color the slag.

### Adding Bloomery Recipes

!!! note
    If you're using the ore compat files, you don't need to add Bloomery recipes; they will be added for you.

[See this page](zs/bloomery.md) for docs and examples for the Bloomery ZenScript.