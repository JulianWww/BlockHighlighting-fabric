# Fabric Example Mod

[![Build](https://github.com/JulianWww/StoppableSound-fabric/actions/workflows/build.yml/badge.svg)](https://github.com/JulianWww/StoppableSound-fabric/actions/workflows/build.yml)
[![GitHub downloads](https://img.shields.io/github/downloads/JulianWww/StoppableSound-fabric/total?label=Github%20downloads&logo=github)](https://github.com/JulianWww/StoppableSound-fabric/releases)
[![GitHub contributors](https://img.shields.io/github/contributors/JulianWww/BlockHighlighting-fabric?label=Contributors&logo=github)](https://github.com/JulianWww/BlockHighlighting-fabric/graphs/contributors)

## Setup

```gradle
repositories {
    maven {
        name = 'Denanu Mods'
        url = 'https://wandhoven.ddns.net/maven/'
    }
}

dependencies {
    modImplementation "net.denanu.BlockHighlighting:BlockHighlighting-<Minecraft_Version>:<StoppableSound_version>"
}
```

## Usage
### Registering a Highlighter
Server and Client Side, define Highlighters
```Java
public static final Identifier HIGHLIGHTER_ID = HighlightIds.register(Identifier.of(MOD_ID, "name"));
```
**Only on the client** for a generic highlighter, create it as follows. Note that the names must match.
```Java
public static HighlightType HIGHLIGHTER = HighlightTypes.register(MOD_ID, "name");
```
instead of registering a default highlighter, you can also define the highlighters default outline and fill color. These must be done in Hex codes using Alpha, Red, Green, Blue syntax. This can be done as follows:
```Java
public static HighlightType HIGHLIGHTER = BlockHighlightingAmaziaConfig.register(MOD_ID, "name", "#FFFFFFFF", "#20FFFFFF"); #outline , fill
```
### Highlight and Unhiglight Blocks
**Server Side**
To highlight or unhighlight blocks (at Position pos) in the world (world) use the following functions respectivly, where ID is the Highlighter ID (previously defined)
```
Highlighter.highlight(world, ID, pos);
Highlighter.unhighlight(world, ID, pos);
```
