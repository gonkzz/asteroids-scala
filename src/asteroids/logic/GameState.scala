package asteroids.logic

import asteroids.logic.GameState._

// which keys are currently held down
case class Controls(turningLeft: Boolean, turningRight: Boolean, thrusting: Boolean) {
    // -1 is counter-clockwise, 1 is clockwise, 0 is not turning
    def turnDirection: Int =
        (if (turningRight) 1 else 0) - (if (turningLeft) 1 else 0)
}

object Controls {
    val Released: Controls = Controls(turningLeft = false, turningRight = false, thrusting = false)
}

case class GameState(ship: Ship, controls: Controls) {

    def withControls(newControls: Controls): GameState =
        copy(controls = newControls)

    def stepped(dims: Dimensions): GameState =
        copy(ship = steeredShip.moved(dims))

    private def steeredShip: Ship = {
        val turnedShip = ship.turnedBy(controls.turnDirection * TurnSpeed)

        if (controls.thrusting) turnedShip.thrusted
        else turnedShip
    }
}

object GameState {
    val TurnSpeed: Double = 5 // degrees per frame

    def start(dims: Dimensions): GameState =
        GameState(ship = Ship(dims.center, Vec.Zero, heading = 0),
                  controls = Controls.Released)
}
