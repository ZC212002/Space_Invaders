package ch.makery.space_invaders.model

class Enemy(var x: Double, var y: Double, val speed: Double) {
  def update(): Unit = {
    y += speed
  }

  def width: Double = 50.0
  def height: Double = 50.0

  def getX: Double = x
  def getY: Double = y
}
