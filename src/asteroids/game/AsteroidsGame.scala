package asteroids.game

import java.awt.event.KeyEvent._

import processing.core.{PApplet, PConstants}
import processing.event.KeyEvent

import engine.GameBase
import engine.graphics.{Color, Point, Rectangle, Triangle}

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

        gameLogic.asteroids.foreach(drawAsteroid)
        gameLogic.bullets.foreach(drawBullet)
        drawShip(gameLogic.ship)
        drawHud()
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

    // the square around an object's collision circle
    def circleArea(obj: SpaceObject): Rectangle = {
        val r = obj.radius.toFloat
        Rectangle(toPoint(obj.position) - Point(r, r), 2 * r, 2 * r)
    }

    def drawBullet(bullet: Bullet): Unit = {
        setFillColor(Color.Yellow)
        drawEllipse(circleArea(bullet))
    }

    def drawAsteroid(asteroid: Asteroid): Unit = {
        setFillColor(Color.Gray)
        drawEllipse(circleArea(asteroid))
    }

    def drawHud(): Unit = {
        setFillColor(Color.White)
        textAlign(PConstants.LEFT, PConstants.TOP)
        textSize(16)
        drawText(s"Wave: ${gameLogic.wave}", Point(10, 10))
    }

    def toPoint(v: Vec): Point = Point(v.x.toFloat, v.y.toFloat)

    override def keyPressed(event: KeyEvent): Unit = {
        event.getKeyCode match {
            case VK_LEFT | VK_A  => gameLogic.setTurningLeft(true)
            case VK_RIGHT | VK_D => gameLogic.setTurningRight(true)
            case VK_UP | VK_W    => gameLogic.setThrusting(true)
            case VK_SPACE        => gameLogic.shoot()
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
