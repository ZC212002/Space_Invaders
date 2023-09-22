package ch.makery.space_invaders.model

class Score {
  private var score: Int = 0
  var HighestScore: Int = 0


  def increment(): Unit = {
    score += 1
  }


  def getScore: Int = score

  def setHighestScore(score: Int): Unit = {
    if (score > this.HighestScore){
      this.HighestScore = score
    }
  }


  def resetScore(): Unit = {
    score = 0
  }
}
