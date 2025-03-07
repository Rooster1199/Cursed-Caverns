# KillTheInnocents
by RuneCodes and Rooster1199


Cursed Caverns is an original, interactive, dungeon crawling video game. Venture through the caverns to fight foes, discover mystical secrets, and save the kingdom.

To play the game, first open the project in an IDE (preferably intelliJ). Then, navigate to lwjgl3 --> src --> main --> java --> io.github.Cursed-Caverns.lwjgl3. From there, run the Lwjgl3Launcher. LibGDX will install all dependencies (<5 mintues) and start the game.

Controls for the game include WASD/Arrow Keys for movement, SPACE for attacking or starting the game, ENTER for entering the dungeon, and H for healing. More information about keybinds can be found in the settings screen, which is accessed through ESC.

All sprites, animations, and music used in the game are created by RuneCodes.


---------------------------------------------------------------------------------------------

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
