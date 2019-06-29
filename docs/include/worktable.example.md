### Examples

```java
import mods.pyrotech.Worktable;

Worktable.addShaped("custom_recipe_name", <minecraft:furnace>, [
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, null, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>]]);

// Builder examples:

// bare-bones
Worktable.buildShaped(<minecraft:furnace>, [
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, null, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>]])
  .register();

// custom name, custom tools
Worktable.buildShaped(<minecraft:furnace>, [
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, null, <minecraft:cobblestone>],
  [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>]])
  .setName("custom_recipe_name")
  .setTool(<minecraft:iron_pickaxe> | <minecraft:diamond_pickaxe>, 10)
  .register();
```