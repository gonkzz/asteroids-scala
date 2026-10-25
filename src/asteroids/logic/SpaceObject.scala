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

    def thrusted: Ship =
        copy(velocity = (velocity + direction * ShipThrust).withMaxLength(ShipMaxSpeed))

    def moved(dims: Dimensions): Ship =
        copy(position = (position + velocity).wrappedInside(dims),
             velocity = velocity * ShipFriction)
}

object SpaceObject {
    val ShipRadius: Double = 12  // ship size
    val ShipThrust: Double = 0.15   // speed added per frame while thrusting
    val ShipMaxSpeed: Double = 7    // pixels per frame
    val ShipFriction: Double = 0.99 // fraction of speed kept each frame
}
