package asteroids.logic

import asteroids.logic.GameState._

// which keys are currently held down
case class Controls(turningLeft: Boolean,
                    turningRight: Boolean,
                    thrusting: Boolean) {
    // -1 is counter-clockwise, 1 is clockwise, 0 is not turning
    def turnDirection: Int =
        (if (turningRight) 1 else 0) - (if (turningLeft) 1 else 0)
}

object Controls {
    val Released: Controls = Controls(turningLeft = false, turningRight = false, thrusting = false)
}

case class GameState(ship: Ship,
                     asteroids: Seq[Asteroid],
                     bullets: Seq[Bullet],
                     controls: Controls,
                     wave: Int) {

    def withControls(newControls: Controls): GameState =
        copy(controls = newControls)

    def stepped(dims: Dimensions): GameState =
        movedForward(dims)

    private def movedForward(dims: Dimensions): GameState =
        copy(ship = steeredShip.moved(dims),
             asteroids = asteroids.map(_.moved(dims)),
             bullets = bullets.map(_.moved(dims)).filter(!_.isExpired))

    private def steeredShip: Ship = {
        val turnedShip = ship.turnedBy(controls.turnDirection * TurnSpeed)

        if (controls.thrusting) turnedShip.thrusted
        else turnedShip
    }

    def withBulletFired: GameState =
        if (bullets.length >= MaxBullets) this
        else copy(bullets = ship.fired +: bullets)

    def isWaveCleared: Boolean = asteroids.isEmpty

    def withNextWave(newAsteroids: Seq[Asteroid]): GameState =
        copy(asteroids = newAsteroids, wave = wave + 1)

}

object GameState {
    val TurnSpeed: Double = 5 // degrees per frame
    val MaxBullets: Int = 5

    def start(dims: Dimensions, asteroids: Seq[Asteroid]): GameState =
        GameState(ship = Ship(dims.center, Vec.Zero, heading = 0),
                  asteroids = asteroids,
                  bullets = Seq(),
                  controls = Controls.Released,
                  wave = 1)
}
