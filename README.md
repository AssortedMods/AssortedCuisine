# Assorted Cuisine

Adds cheese, chocolate, pies, sodas and other foods to cook and eat. This is the Grim Cuisine part of the old
[Grim Pack](https://github.com/grim3212/grim-pack), brought forward from 1.12. Each group is also its own mod if you only want some of them.

- [Assorted Cuisine](mods/cuisine) has all of them in one download
- [Assorted Kitchen](mods/kitchen) adds dairy, chocolate, pies and the kitchen tools
- [Assorted Sodas](mods/sodas) adds sodas
- [Assorted Health](mods/health) adds sweets, bandages and healthpacks
- [Assorted Dragon Fruit](mods/dragonfruit) adds dragon fruit

Worlds made with Assorted Cuisine 1.x work with any of these.

Requires [Assorted Lib](https://github.com/AssortedMods/AssortedLib). Branches are per Minecraft version and `26.2` is the current one.

## Issue Reporting

Please include the following

* Minecraft version
* NeoForge version, or Fabric Loader and Fabric API versions
* Which of these mods you have and their versions
* Assorted Lib version
* The full `latest.log`, and the crash report if the game crashed

## Building

You need JDK 25. Each mod is its own folder under `mods`. The build setup comes from
[AssortedBuild](https://github.com/AssortedMods/AssortedBuild) and `assortedbuild_version` in `gradle.properties`
picks the version.

To build against a local copy of Assorted Lib, publish it first.

```bash
cd ../AssortedLib && ./gradlew publishToMavenLocal
```

Some useful commands

```bash
./gradlew build                                  # build every mod
./gradlew :kitchen:neoforge:runClient            # run one mod
./gradlew :all:neoforge:runClient                # run every mod together
./gradlew runGameTestServer runGameTest          # gametests on NeoForge and Fabric
./gradlew runClientData runServerData            # datagen
```

Generated resources are committed. The NeoForge datagen writes them for both loaders.

## License

[LGPL-3.0-only](LICENSE).
