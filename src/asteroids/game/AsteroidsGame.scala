package asteroids.game

import processing.core.PApplet
import engine.GameBase
import engine.graphics.Color

import asteroids.logic._
import engine.graphics.{Color, Point, Rectangle}

class AsteroidsGame extends GameBase {
    var dot: Vec = Vec(400, 300)
    val dotVelocity: Vec = Vec.fromAngle(30) * 5
    val dims: Dimensions = Dimensions(800, 600)

    override def settings(): Unit = {
        pixelDensity(displayDensity())
        size(800, 600)
    }

    override def setup(): Unit = {
        // Fonts are loaded lazily, so when we call text()
        // for the first time, there is significant lag.
        // This prevents it from happening during gameplay.
        text("", 0, 0)
    }

    override def draw(): Unit = {
        setBackground(Color.Black)
        // ellipse(mouseX, mouseY, 20, 20)
        dot = (dot + dotVelocity).wrappedInside(dims)
        setFillColor(Color.Yellow)
        drawEllipse(Rectangle(toPoint(dot) - Point(5, 5), 10, 10))
    }

    def toPoint(v: Vec): Point = Point(v.x.toFloat, v.y.toFloat)

}

object AsteroidsGame {

    def main(args: Array[String]): Unit = {
        // This is needed for Processing, using the name
        // of the class in a string is not very beautiful...
        PApplet.main("asteroids.game.AsteroidsGame")
    }
}
