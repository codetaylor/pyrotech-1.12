The first time Pyrotech is loaded, it will generate some data and prompt you to reload.

---

### Wood Compatibility System

When Pyrotech is loaded, it will derive some data based on the recipes available and the contents of the `logWood`, `plankWood`, and `slabWood` oredicts and save that derived data to a file: `core.compat.Wood-Generated.json`. This file will be overwritten each time Pyrotech loads in order to provide the user with any generated changes.

When this file is created, it will also create another file if it doesn't already exist: `core.compat.Wood-Custom.json`. When the custom file is first created, it is simply a copy of the generated file. The custom file is the data that Pyrotech actually uses and will never be overwitten.

The data in the custom file is used to generate wood recipes for Pyrotech's Chopping Block and sawmills automatically.

**Ore Compat Entries**
```js
"entries": {
  "minecraft:log:0": "minecraft:planks:0",
  "minecraft:planks:0": "minecraft:wooden_slab:0"
}
```

An entry in the wood compat file, shown above, consists of an input item key mapped to an output item value.

The item strings are in the familiar item string format `(domain):(path):(meta)`.