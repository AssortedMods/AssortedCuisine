# Assorted Cuisine

Adds cheese, chocolate, pies, sodas and other foods to cook and eat. This is the Grim Cuisine part
of the old [Grim Pack](https://github.com/grim3212/grim-pack), brought forward from 1.12 and
rebuilt on the modern APIs.

## What is in it

* **Dairy** - a butter churn and a cheese maker that take a bucket of milk and give back butter or a
  block of cheese; cheese, bread slices, cheese burgers and sandwiches; cracked, mixed and
  scrambled eggs; a knife and a mixer that wear out a point at a time instead of being used up.
* **Chocolate** - cocoa trees that generate in the overworld and grow from cocoa fruit, cocoa dust,
  bowls of chocolate hot and cold, a mould that sets hot chocolate into bars, wrapped bars,
  chocolate blocks and a chocolate cake.
* **Pies** - a pie pan and dough, and apple, melon, pumpkin, chocolate and pork pies to fill and
  bake.
* **Health** - sugar sweets, powered sugar and sweets, bandages and health packs that heal on use.
* **Soda** - a bottle, a CO2 canister, carbonated water and eleven flavours, each healing a
  different amount. One of them does not heal you at all.
* **Dragon fruit** - cacti drop it.

Recipes take tags wherever the 1.12 version took an ore dictionary name, so another mod's cheese,
milk bucket or knife works here too.

Each of the six is a part that `config/assortedcuisine-common` can switch off under `parts`. A
disabled part keeps its blocks and items registered, so worlds still load, but loses its recipes,
creative tab entries and manual chapters, along with the cocoa trees or the cactus drop. The knife
stays while either dairy or pies is on, and the chocolate pie needs both pies and chocolate.

Minecraft 26.2, on both NeoForge and Fabric from a single source tree. Requires
[Assorted Lib](https://github.com/grim3212/AssortedLib). Branches are per Minecraft version; `26.2`
is the current one.

## Issue Reporting

Please include the following

* Minecraft version
* Loader and its version — NeoForge, or Fabric Loader together with Fabric API
* Assorted Cuisine version
* Assorted Lib version
* The full `latest.log`, plus the crash report if the game crashed

## Building

JDK 25 and the bundled Gradle wrapper. `common/` holds the loader-agnostic code; both loader
modules compile those sources inline rather than depending on a common jar, so there is nothing to
install between them.

How the build works - the Minecraft and loader versions, the runs, the tests, publishing - lives in
[AssortedBuild](https://github.com/grim3212/AssortedBuild), pinned by `assortedbuild_version` in
`gradle.properties`. This repository only says what the mod is.

Assorted Lib is consumed as a Maven artifact. To build against an unreleased one, publish it first:

```bash
cd ../AssortedLib && ./gradlew publishToMavenLocal
```

Then from this repository:

```bash
./gradlew build                        # every module; jars land in <module>/build/libs
./gradlew :neoforge:runClient
./gradlew :fabric:runClient
./gradlew :neoforge:runGameTestServer  # headless gametests, non-zero exit on failure
./gradlew :fabric:runGameTest
./gradlew :neoforge:runClientData      # datagen
./gradlew :neoforge:runServerData
./gradlew :fabric:runDatagenClient
```

Generated resources are committed. Datagen output is regenerated, never hand-edited.

## License

[LGPL-3.0-only](LICENSE).
