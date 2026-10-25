package asteroids.game

import processing.core.PApplet
import engine.GameBase
import engine.graphics.Color

class AsteroidsGame extends GameBase {

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
        // setBackground(Color.Black)
        ellipse(mouseX, mouseY, 20, 20)
    }

}

object AsteroidsGame {

    def main(args: Array[String]): Unit = {
        // This is needed for Processing, using the name
        // of the class in a string is not very beautiful...
        PApplet.main("asteroids.game.AsteroidsGame")
    }
}
