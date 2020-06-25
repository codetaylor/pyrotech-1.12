!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!
    
### Conditional Modifiers

The drying racks use conditional modifiers to derive their base speeds. These modifiers can be edited for each drying rack in the `module.tech.Basic.cfg` file.

---

Explicit base speeds can be defined per biome using CraftTweaker:

```java
import mods.pyrotech.DryingRack;

DryingRack.setBiomeSpeed(3.0, "minecraft:plains");
DryingRack.setBiomeSpeed(4.2, ["minecraft:ocean", "minecraft:desert"]);
```
---

Below is the logic that governs the speed calculations. I've exposed it here to make it easier to understand how the config values affect the speed of the device.

```java
double calculatedSpeed = 0;

// -------------------------------------------------------------------------
// First, look for an explicit biome base speed set with CraftTweaker.
// -------------------------------------------------------------------------

Map<String, Float> biomeSpeeds = this.getBiomeSpeeds();
ResourceLocation registryName = biome.getRegistryName();
boolean isExplicitBiomeSpeedSet = false;

if (registryName != null) {
  String registryNameString = registryName.toString();
  Float explicitSpeed = biomeSpeeds.get(registryNameString);

  if (explicitSpeed != null) {
    calculatedSpeed = explicitSpeed;
    isExplicitBiomeSpeedSet = true;
  }
}

// -------------------------------------------------------------------------
// If an explicit speed was not found, derive the base speed of the device
// using conditional modifiers.
// -------------------------------------------------------------------------

if (!isExplicitBiomeSpeedSet) {

  if (canRain && this.world.isRainingAt(this.pos.up())) {
    // If the biome can rain and the device is being directly rained on,
    // set the new speed to this device's rain speed.
    // A negative speed will reduce recipe progress.
    calculatedSpeed = modifiers.DIRECT_RAIN;

  } else if ((canRain && this.world.isRaining()) || biome.isHighHumidity()) {
    // If the biome can rain and it is raining somewhere, or the biome is high
    // humidity, the new speed is set to this:
    calculatedSpeed = modifiers.INDIRECT_RAIN;

  } else if (this.world.provider.isNether()) {
    // If the device is in the Nether, set the new speed to this:
    calculatedSpeed = modifiers.NETHER;

  } else {
    // Otherwise, set the new speed and then additively modify it based
    // on further criteria.

    calculatedSpeed = modifiers.BASE_DERIVED;

    if (BiomeDictionary.getBiomes(BiomeDictionary.Type.HOT).contains(biome)) {
      // If the biome is hot, increase the speed:
      calculatedSpeed += modifiers.DERIVED_HOT;
    }

    if (BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY).contains(biome)) {
      // If the biome is dry, increase the speed:
      calculatedSpeed += modifiers.DERIVED_DRY;
    }

    if (BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD).contains(biome)) {
      // If the biome is cold, decrease the speed:
      calculatedSpeed += modifiers.DERIVED_COLD;
    }

    if (BiomeDictionary.getBiomes(BiomeDictionary.Type.WET).contains(biome)) {
      // If the biome is wet, decrease the speed:
      calculatedSpeed += modifiers.DERIVED_WET;
    }
  }
}

// -------------------------------------------------------------------------
// Next, scan a region around the device and look for a fire block,
// or a block that counts as a fire source. Add speed bonus for each
// adjacent fire or fire source block.
// -------------------------------------------------------------------------

final float[] fireSourceBonus = new float[1];

int range = modifiers.FIRE_SOURCE_BONUS_RANGE;

BlockHelper.forBlocksInCube(this.world, this.pos, range, range, range, (w, p, bs) -> {

  if (this.isSpeedBonusBlock(bs, p)) {
    fireSourceBonus[0] += modifiers.FIRE_SOURCE_BONUS;
  }
  return true;
});

calculatedSpeed += fireSourceBonus[0];

// -------------------------------------------------------------------------
// Next, add an extra speed bonus if it's not raining, the rack is
// under the sky, and it's daytime.
// -------------------------------------------------------------------------

if (!this.world.isRaining()
    && this.world.canSeeSky(this.pos.up())
    && this.world.getWorldTime() % 24000 > 3000
    && this.world.getWorldTime() % 24000 < 9000) {
  calculatedSpeed += modifiers.DAYTIME;
}

// -------------------------------------------------------------------------
// Finally, apply the master SPEED_MODIFIER from the device's config.
// -------------------------------------------------------------------------

return (float) (calculatedSpeed * this.getMultiplicativeSpeedModifier());
```