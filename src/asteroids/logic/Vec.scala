package asteroids.logic

// position or velocity
// the y-axis increases downwards
case class Vec(x: Double, y: Double) {
    def +(other: Vec): Vec = Vec(x + other.x, y + other.y)
    def -(other: Vec): Vec = Vec(x - other.x, y - other.y)
    def *(factor: Double): Vec = Vec(x * factor, y * factor)

    def length: Double = math.hypot(x, y)   // √(x² + y²)
    def distanceTo(other: Vec): Double = (this - other).length

    // positive angles rotate clockwise, because the y-axis points down
    // 2D rotation matrix
    def rotatedBy(degrees: Double): Vec = {
        val radians = math.toRadians(degrees)
        Vec(x * math.cos(radians) - y * math.sin(radians),
            x * math.sin(radians) + y * math.cos(radians))
    }

    // cap ship speed
    def withMaxLength(maxLength: Double): Vec =
        if (length > maxLength) this * (maxLength / length)
        else this

    // leaving the screen on one side means entering it on the other side
    def wrappedInside(dims: Dimensions): Vec =
        Vec(wrap(x, dims.width), wrap(y, dims.height))

    private def wrap(value: Double, limit: Double): Double =
        ((value % limit) + limit) % limit
}

object Vec {
    val Zero: Vec = Vec(0, 0)

    // unit vector pointing in the given direction
    def fromAngle(degrees: Double): Vec =
        Vec(0, -1).rotatedBy(degrees)
}
