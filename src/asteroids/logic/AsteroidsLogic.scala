package asteroids.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import asteroids.logic.AsteroidsLogic._

class AsteroidsLogic(val randomGen: RandomGenerator,
                     val dims: Dimensions) {

    def this() =
        this(new ScalaRandomGen(), DefaultDims)

    private var gameState: GameState = newGame()

    private def newGame(): GameState =
        GameState.start(dims, randomWave(1))

    private def randomWave(wave: Int): Seq[Asteroid] =
        Seq.fill(FirstWaveSize + wave - 1)(randomAsteroid())

    // spawned on the left or top edge, so never on top of the ship in the center
    private def randomAsteroid(): Asteroid = {
        val position =
            if (randomGen.randomInt(2) == 0) Vec(0, randomGen.randomInt(dims.height.toInt))
            else Vec(randomGen.randomInt(dims.width.toInt), 0)

        val speed = MinAsteroidSpeed + randomGen.randomInt(SpeedVariation) / 10.0
        val velocity = Vec.fromAngle(randomGen.randomInt(360)) * speed

        Asteroid(position, velocity, Large)
    }

    def step(): Unit = {
        val nextState = gameState.stepped(dims)

        gameState =
            if (nextState.isWaveCleared) nextState.withNextWave(randomWave(nextState.wave + 1))
            else nextState
    }

    private def updateControls(change: Controls => Controls): Unit =
        gameState = gameState.withControls(change(gameState.controls))

    def setTurningLeft(turning: Boolean): Unit =
        updateControls(_.copy(turningLeft = turning))

    def setTurningRight(turning: Boolean): Unit =
        updateControls(_.copy(turningRight = turning))

    def setThrusting(thrusting: Boolean): Unit =
        updateControls(_.copy(thrusting = thrusting))

    def ship: Ship = gameState.ship
    def isThrusting: Boolean = gameState.controls.thrusting

    def shoot(): Unit =
        gameState = gameState.withBulletFired

    def bullets: Seq[Bullet] = gameState.bullets

    def asteroids: Seq[Asteroid] = gameState.asteroids
    def wave: Int = gameState.wave
}

object AsteroidsLogic {
    val FramesPerSecond: Int = 60 // logic steps per second
    val DrawSizeFactor: Double = 1.0 // increase this to make the game bigger (for high-res screens)
    val DefaultDims: Dimensions = Dimensions(width = 800, height = 600)


    val FirstWaveSize: Int = 4
    val MinAsteroidSpeed: Double = 1
    val SpeedVariation: Int = 10 // in tenths of a pixel per frame
}
