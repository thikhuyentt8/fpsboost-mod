# FPS Boost Toggle

A client-side Fabric mod for Minecraft Java 1.21.1 through 1.21.11. Run `/fpsboost` in a world to lower render distance to at most 8 chunks and simulation distance to at most 6. Run it again to restore the values that were active before the toggle. Use `/fpsboost status` to check whether boost is active, and `/fpsboost toggle` to force a toggle without retyping the base command.

The mod only changes these two vanilla options. It does not promise a fixed FPS gain: render distance can help when chunk rendering is the bottleneck, while the result depends on hardware, shaders, other mods, and the world.

## Build

Use a 64-bit Java 21 JDK. The Gradle wrapper downloads Gradle and the required Minecraft/Fabric dependencies on the first build.

Build the default Minecraft 1.21.1 jar:

```powershell
.\gradlew.bat build
```

Build every supported release from 1.21.1 through 1.21.11:

```powershell
.\build-all.ps1
```

The remapped jars are written to `build/libs/`. Install the jar matching your Minecraft version in that instance's `mods` folder, together with Fabric API.

## Recommended performance setup

- Sodium: the main client renderer optimization; use its build for your exact Minecraft version.
- Lithium: optimizes game logic, including single-player integrated server work.
- ImmediatelyFast: optimizes several immediate-mode rendering paths.
- Entity Culling: skips rendering entities and block entities hidden from view.
- Iris: only needed for shader packs; use a Sodium-compatible Iris build.

Check each mod's supported Minecraft version and compatibility notes before combining them. Do not install multiple mods that replace the same renderer.