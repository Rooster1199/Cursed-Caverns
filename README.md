# Cursed Caverns
by RuneCodes and Rooster1199


Cursed Caverns is an original, interactive, dungeon crawling video game. Venture through the winding caverns to fight foes, discover mystical secrets, and save the kingdom!

# Playing the Game

To play the game, first open the project in an IDE (preferably intelliJ). Then, navigate to lwjgl3 --> src --> main --> java --> io.github.Cursed-Caverns.lwjgl3. From there, run the Lwjgl3Launcher. LibGDX will install all dependencies (<5 mintues) and start the game.

# Controls

Controls for the game include WASD/Arrow Keys for movement, SPACE for attacking or starting the game, ENTER for entering the dungeon, and H for healing. More information about keybinds can be found in the settings screen, which is accessed through ESC.

# 2D Array Usage

In this program, 2D arrays are primarily used to facilitate the text and sprite animations. The Constants class is a great example of this implementation. It contains the 2D arrays for the Columns and Rows of the player and enemy animations. These 2D arrays are accessed and implemented in the Entity class within the entities package. There, the spritesheets are processed by the animationSplicer class and split into animation arraylists. Each instance of the Entity class stores its own 2D array of animations. 

# Credit

All sprites, animations, and music used in the game are created by RuneCodes.


---------------------------------------------------------------------------------------------

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.
