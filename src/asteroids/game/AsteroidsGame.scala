package asteroids.game

import processing.core.PApplet
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle, Triangle}

import asteroids.logic._

class AsteroidsGame extends GameBase {
    val dims: Dimensions = Dimensions(800, 600)
    // temporary: replaced by the game logic in step 4
    var ship: Ship = Ship(dims.center, Vec.Zero, heading = 0)

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

    def drawShip(ship: Ship): Unit = {
        setFillColor(Color.White)
        val Seq(nose, backRight, backLeft) = ship.outline.map(toPoint)
        drawTriangle(Triangle(nose, backRight, backLeft))
    }

    override def draw(): Unit = {
        setBackground(Color.Black)
        ship = ship.turnedBy(2)
        drawShip(ship)
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
