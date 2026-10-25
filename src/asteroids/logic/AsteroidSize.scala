package asteroids.logic

sealed abstract class AsteroidSize(val radius: Double, val points: Int) {
    def smaller: Option[AsteroidSize]
}

case object Large extends AsteroidSize(radius = 40, points = 20) {
    def smaller: Option[AsteroidSize] = Some(Medium)
}

case object Medium extends AsteroidSize(radius = 20, points = 50) {
    def smaller: Option[AsteroidSize] = Some(Small)
}

case object Small extends AsteroidSize(radius = 10, points = 100) {
    def smaller: Option[AsteroidSize] = None
}
