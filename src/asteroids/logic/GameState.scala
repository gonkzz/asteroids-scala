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
                     score: Int,
                     wave: Int,
                     isGameOver: Boolean) {

    def withControls(newControls: Controls): GameState =
        copy(controls = newControls)

    def stepped(dims: Dimensions): GameState =
        if (isGameOver) this
        else movedForward(dims).withCollisionsResolved

    private def movedForward(dims: Dimensions): GameState =
        copy(ship = steeredShip.moved(dims),
             asteroids = asteroids.map(_.moved(dims)),
             bullets = bullets.map(_.moved(dims)).filter(!_.isExpired))

    private def steeredShip: Ship = {
        val turnedShip = ship.turnedBy(controls.turnDirection * TurnSpeed)

        if (controls.thrusting) turnedShip.thrusted
        else turnedShip
    }

    private def withCollisionsResolved: GameState = {
        val (hitAsteroids, missedAsteroids) =
            asteroids.partition(asteroid => bullets.exists(_.collidesWith(asteroid)))
        val remainingBullets =
            bullets.filter(bullet => !hitAsteroids.exists(bullet.collidesWith))

        copy(asteroids = missedAsteroids ++ hitAsteroids.flatMap(_.fragments),
             bullets = remainingBullets,
             score = score + hitAsteroids.map(_.size.points).sum,
             isGameOver = missedAsteroids.exists(_.collidesWith(ship)))
    }

    def withBulletFired: GameState =
        if (isGameOver || bullets.length >= MaxBullets) this
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
                  score = 0,
                  wave = 1,
                  isGameOver = false)
}
