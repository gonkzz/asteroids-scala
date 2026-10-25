package asteroids.logic

import asteroids.logic.SpaceObject._

// every object treated as a circle, for collisions logic
abstract class SpaceObject {
    def position: Vec
    def velocity: Vec
    def radius: Double

    def collidesWith(other: SpaceObject): Boolean =
        position.distanceTo(other.position) < radius + other.radius
}

case class Ship(position: Vec, velocity: Vec, heading: Double) extends SpaceObject {
    def radius: Double = ShipRadius

    def direction: Vec = Vec.fromAngle(heading)

    def nose: Vec = position + direction * (radius * 1.5)

    // nose, back right and back left corner of the ship
    def outline: Seq[Vec] =
        Seq(nose,
            position + direction.rotatedBy(140) * radius,
            position + direction.rotatedBy(-140) * radius)

    def turnedBy(degrees: Double): Ship =
        copy(heading = heading + degrees)
}

object SpaceObject {
    val ShipRadius: Double = 12
}
