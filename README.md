# FzzyMapz
<p align="left">
<a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-brightgreen.svg"></a>
</p>

FzzyMaps is a library for creating tile-based maps and mini-maps. On it's own it doesn't add any maps, but it can be used to create just about any tiled map you can think of! Packaged with the library are some basic pieces and parts that can be used to create a variety of maps "out of the box", but the systems within are very flexible so any other needed component can be added without worry.

### Features

#### Tile-based
* FzzyMapz is a tile-based mapping tool. It's designed to show tiles on a per-chunk basis to replicate the blocky feel of Minecraft and retro games. The default themes packaged with FzzyMaps showcase this tile-based approach in a "penciled-on" style.
* Tiles can be layered as needed
* Map data is gathered and stored in customizable layers that provide tiles to the map renderer. These layers can process anything a client WorldChunk has sotred in it, and the tiles can be customized to present that data as needed

#### Themed
* Map Layers specify a ThemeType that they pull tile textures from. FzzyMapz comes packaged with some built-in ThemeTypes and Themes, but any number of other themes and types can be registered and added. 
* Themes for pre-existing types are defined in resource packs, allowing for the addition of custom textures without overriding default themes
* Themes are selectable and swappable in-game!
* Different texture resolutions can be applied to most themes, if they utilize the built in scale parameter. All built-in themes and tiles that reference those themes utilize scaling.

#### Standard Screens
* Come with a built-in Screen system to allow for easy creation of World (full-screen) and Mini (HUD rendered in the corner of the screen) maps. 
* Secondary aspects of the map screen rendering are all customizable, and can be Themed too if desired.
  * Add rendered features like a compass rose, legend, rulers, etc
  * Rendered features are passed all of the context of the screen (on qorld map at least) so can handle mouse clicks, check the current tiles for context, and so on
* If a more custom impl is needed, the standard screens are laid out for easy extension and modification.

#### Customizable Mouse Interaction
* Define what right clicking on the map does, with some standard features available as standard
* Standard click functions include
  *  Changing the map theme
  *  setting a waypoint, or removing one
*  Any other function (even non-map related if you really want) can be added. For example, integration with Open-PAC could be added as a right click function.
