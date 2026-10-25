package asteroids.logic

case class Dimensions(width: Double, height: Double) {
    def center: Vec = Vec(width / 2, height / 2)
}
