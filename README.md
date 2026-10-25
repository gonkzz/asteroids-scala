# Asteroids

A version of the arcade classic Asteroids, written in Scala 3 with Processing for the OOFP bonus assignment.

## How to run

```sh
./gradlew run
```

## Controls

| Key | Action |
|---|---|
| Left / A | Turn counter-clockwise |
| Right / D | Turn clockwise |
| Up / W | Thrust |
| Space | Shoot |
| R | Restart after game over |

## What the game does

- **Ship physics:** the ship turns and thrusts. It has inertia, so it keeps drifting after you let go, and it slowly loses speed to friction. Its top speed is capped.
- **Screen wrapping:** everything that leaves one edge of the screen comes back on the opposite edge.
- **Bullets:** they fire from the ship's nose and also carry the ship's own speed. Each bullet lives for 50 frames, and at most 5 can be on screen at once.
- **Asteroids:** they spawn at random positions on the edges and fly in random directions at random speeds.
- **Splitting:** a shot large asteroid breaks into two medium ones that fly apart, and a medium one into two small ones. Small asteroids are destroyed. Smaller asteroids are worth more points: 20, 50 and 100.
- **Waves:** clearing the screen starts a new wave with one more asteroid than the last.
- **Game over:** touching an asteroid ends the game, and R starts a new one.

## How the code is organised

The code has two layers, the same split as in the Snake and Tetris assignment:

- `asteroids.logic`:
  - `Vec`: 2D vector maths (adding, scaling, rotating, length, capping speed, screen wrapping).
  - `SpaceObject`: an abstract base class that treats every object (`Ship`, `Bullet` and `Asteroid`) as a circle for collisions.
  - `AsteroidSize`: a sealed type with three sizes, `Large`, `Medium` and `Small`. Each size knows its radius, its points (score) and the next smaller size.
  - `GameState`: one immutable snapshot of the game. `stepped` is a pure function that turns one frame into the next: steer, move, then resolve collisions.
  - `AsteroidsLogic`: the only mutable part, with a single `var` that holds the current `GameState`. It also handles randomness (spawning asteroids) and input.
- `asteroids.game`:
  - `AsteroidsGame` draws the state each frame and handles key presses.

## How I used AI

I used Claude Opus 5.5 as a tutor throughout the development of this game. My goal was to build and understand the game myself, rather than have AI generate the implementation.

Before starting, I established a few guidelines:

- Claude should guide me and not write any code, unless I would explicitly ask for it, which ended up on the later parts when trying to figure out the collision system.
- I also used it to suggest what to work on next, explain relevant concepts, and help me reason through problems, but I would always write the code myself.
- I worked step by step, starting with the project setup and an empty window, then moving to vector mathematics (`Vec`), ship movement, the game loop, bullets, asteroids, and collisions.
- I used my Snake project as a starting point, reusing its engine, Gradle setup, and separation between game logic and rendering. I also used a previous Asteroids implementation I did in Python sometime ago as a reference.
- After each step, I used Claude to check my understanding by asking questions and encouraging me to experiment with the code.

Basically I used AI as an interactive tutor and to aid me to understand how some of the harder parts worked.
