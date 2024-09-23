# Lato - A downhill snowboarding game for Android

![screenshot](metadata/en-GB/images/featureGraphic.png)

# License
Lato - downhill snowboarding game for Android

Copyright (C) 2020-2023 Andreas Redmer

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

This full license is also in [LICENCE](LICENCE)

# Screenshots
<p float="left">
  <img src="metadata/en-GB/images/phoneScreenshots/lato_dawn.png" width="49%" />
  <img src="metadata/en-GB/images/phoneScreenshots/lato_day.png" width="49%" /> 
</p>

<p float="left">
  <img src="metadata/en-GB/images/phoneScreenshots/lato_dusk.png" width="49%" />
  <img src="metadata/en-GB/images/phoneScreenshots/lato_night.png" width="49%" /> 
</p>

# controls
* touch the screen: start the game
* touch the screen: jump
* touch the screen: roll in air

# international age ratings

*    ACB: G (general)
*    ClassInd: L
*    ESRB: E (everyone)
*    PEGI:3
*    USK: 0
*    IARC: 3

# Quickstart

Option 1: [![Get it on F-Droid](https://f-droid.org/wiki/images/3/31/F-Droid-button_get-it-on_smaller.png)](https://f-droid.org/packages/ardash.lato/)

Option 2: Build it from source

	git clone https://gitlab.com/ar-/lato.git
	cd lato/p
	./gradlew assembleRelease
	# note: your APK file is now here: ./android/build/outputs/apk/android-release-unsigned.apk

# History

This is a 2020 remake of the Game Alto's Adventure (2015), which was a remake of Ski Safari (2012). The first release of this game is in 2023.

'Lato' is the name of the main character and describes someone who is always 'late' and that's why he has to hurry.

Unfortunately none of the previous endless runner games was FLOSS. There was TredGamerZ/Legendary (2017), it was open source but still based on Unity. Unity is not open source. The corona SDK engine has been made open source in Jan 2019 and they got a show case example called Endless Sk8boarder, which looks like crap and doesn't do good marketing for the engine.

The day and night cycles of Alto's adventure have also been remade a few times - most prominently in the game Fishing Life in 2019. However Fishing Life was also made in Unity. Lato is the first release of these day and night cycles into the pen source world, to be reused in other projects.

The only suitable way around the restrictions of closed source games and engines was to use a well established and well supported FLOSS engine (LibGDX) to make a complete new endless runner game. Unfortunately many problems are also not resolved yet in LibGDX and yet have to be pioneered. That's what this project attempts. Bring a showcase app to the FLOSS community that provides an example implementation for:
* endless procedural random terrain
* 2D and 3D mixture
* realistic weather and day night cycles
* physics without physics engine (Box2D was possible over overkill, so it was removed in 8e059919b01a148cc3303734567100079ae2bb18)
* chained shaders
* Scene3D - likely the first working version at all (only implemented to the necessary extend)

in LIBGDX.

If you are a game maker too, please feel free to copy everything you need from this project. If you copy the code and art: obey the license. If you copy the idea: no problem. If you are a gamer, please enjoy this game for free, no ads, no optional payments, pure fun.

All parts of this project are meant to be developed with FLOSS software.

Engine: LibGDX

Target Platforms: Android, Linux

Dev PC: Ubuntu Linux

IDE: Eclipse

Sprite drawing: Inkscape

Animations and rigging: Synfig Studio

Pixeling: Gimp

# Music

Music: Traveler (2019) by Alexander Nakarada (www.serpentsoundstudios.com)

Licensed under Creative Commons: By Attribution 4.0 License

http://creativecommons.org/licenses/by/4.0/

# Donate

Cash donations are not accepted. You can buy the author of this app a coffee if you have some spare cryptocurrencies.

* BTC/BCH/BTG/etc: 1J2bbhJYksSjeynGGhuSPN9aTEaxiGm4nR
* BTC Bech32: bc1qgshj3mtju02sg9ymse9cksfjdjh5gp0204w3zj
* DASH: XbLRt5imEHc72KmhvC7V9v8f9NmYrmvweS
* FIRO: a4tAW5vp8rzjFrAxhRaq24m6vFZ2AmHUYs
* ETH: 0x0a6604dc5000c57e80f824601535db216e77482f
* XMR: 4AffoFbFhfGZdBeMaQYSCMTURacM3qZYxKHQeLx8xkiLUjzk2GPzjCrNU5uquXEsEL6wcN8b5ULg5JdDaQfuQRkUJs6xx3f

*Note: These addresses are taken from the original authors website (http://andreas.redmer.super-sm.art/). They are cryptographically signed, with the same key, that signed the git commits of this software project. Feel free to verify the GPG signatures so you can be sure, that you donation goes to the person, who actually commited the code of this software.*

How much to donate? 🙂

1. Go to your nearest coffee shop (or bar [or cafe in the Netherlands]).
2. Get the price for a regular coffee (or beer). No sugar.
3. Optionally multiply by 2. Thanks.
4. Convert the price into a crypto currency mentioned above.
5. Donate the resulting value.

✌️

# Contact / Community
### Matrix room
https://matrix.to/#/#lato:abga.be

### email the developer
ar-lato@abga.be

### email to create a gitlab ticket (if you don't have a gitlab account)
incoming+ar-/lato@gitlab.com
