package asteroids.game

import java.awt.event.KeyEvent._

import engine.GameBase
import engine.graphics.{Color, Point, Rectangle, Triangle}
import processing.core.PApplet
import processing.event.KeyEvent
import asteroids.logic._
import asteroids.game.AsteroidsGame._

class AsteroidsGame extends GameBase {

    val gameLogic: AsteroidsLogic = new AsteroidsLogic()
    val updateTimer = new UpdateTimer(AsteroidsLogic.FramesPerSecond.toFloat)
    val dims: Dimensions = gameLogic.dims
    val widthInPixels: Int = (DrawSizeFactor * dims.width).ceil.toInt
    val heightInPixels: Int = (DrawSizeFactor * dims.height).ceil.toInt

    override def draw(): Unit = {
        updateState()
        scale(DrawSizeFactor.toFloat)
        setBackground(Color.Black)
        drawShip(gameLogic.ship)
    }

    def drawShip(ship: Ship): Unit = {
        if (gameLogic.isThrusting) {
            setFillColor(Color.Orange)
            drawTriangle(flameTriangle(ship))
        }
        setFillColor(Color.White)
        val Seq(nose, backRight, backLeft) = ship.outline.map(toPoint)
        drawTriangle(Triangle(nose, backRight, backLeft))
    }

    def flameTriangle(ship: Ship): Triangle = {
        val back = ship.position - ship.direction * (ship.radius * 1.8)
        val right = ship.position + ship.direction.rotatedBy(150) * (ship.radius * 0.8)
        val left = ship.position + ship.direction.rotatedBy(-150) * (ship.radius * 0.8)
        Triangle(toPoint(back), toPoint(right), toPoint(left))
    }

    def toPoint(v: Vec): Point = Point(v.x.toFloat, v.y.toFloat)

    override def keyPressed(event: KeyEvent): Unit = {
        event.getKeyCode match {
            case VK_LEFT | VK_A  => gameLogic.setTurningLeft(true)
            case VK_RIGHT | VK_D => gameLogic.setTurningRight(true)
            case VK_UP | VK_W    => gameLogic.setThrusting(true)
            case _               => ()
        }
    }

    override def keyReleased(event: KeyEvent): Unit = {
        event.getKeyCode match {
            case VK_LEFT | VK_A  => gameLogic.setTurningLeft(false)
            case VK_RIGHT | VK_D => gameLogic.setTurningRight(false)
            case VK_UP | VK_W    => gameLogic.setThrusting(false)
            case _               => ()
        }
    }

    override def settings(): Unit = {
        pixelDensity(displayDensity())
        size(widthInPixels, heightInPixels)
    }

    override def setup(): Unit = {
        text("", 0, 0)
        // called last: the game clock starts ticking here
        updateTimer.init()
    }

    def updateState(): Unit = {
        // catch up when drawing is slower than the game
        while (updateTimer.timeForNextFrame()) {
            gameLogic.step()
            updateTimer.advanceFrame()
        }
    }
}

object AsteroidsGame {

    val DrawSizeFactor: Double = AsteroidsLogic.DrawSizeFactor

    def main(args: Array[String]): Unit =
        PApplet.main("asteroids.game.AsteroidsGame")
}
