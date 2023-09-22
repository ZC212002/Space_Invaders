package ch.makery.space_invaders.model

class Bullet(var x: Double, var y: Double, val speed: Double) {
  def update(): Unit = {
    y += speed
  }

  def width: Double = 10.0
  def height: Double = 10.0

  def getX: Double = x
  def getY: Double = y
}
