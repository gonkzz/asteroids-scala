package asteroids.logic

import asteroids.logic.AsteroidsLogic._

class AsteroidsLogic(val dims: Dimensions) {

    def this() = this(DefaultDims)

    private var gameState: GameState = GameState.start(dims)

    def step(): Unit =
        gameState = gameState.stepped(dims)

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
}

object AsteroidsLogic {
    val FramesPerSecond: Int = 60 // logic steps per second
    val DrawSizeFactor: Double = 1.0 // increase this to make the game bigger (for high-res screens)
    val DefaultDims: Dimensions = Dimensions(width = 800, height = 600)
}
