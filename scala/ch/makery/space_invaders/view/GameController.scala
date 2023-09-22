package ch.makery.space_invaders.view

import ch.makery.space_invaders.MainApp
import ch.makery.space_invaders.model._
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.Pane
import scalafxml.core.macros.sfxml

import scala.collection.mutable
import scala.util.Random
import scala.collection.mutable.ListBuffer

@sfxml
class GameController(
                      private val imageView: ImageView,
                      private val gamePane: Pane
                    ) {
  private val player_name = MainApp.player_name
  private val score = new Score()
  val player = new Player(player_name, score)

  private val speed: Double = 30.0
  private var speedEnemy: Double = 3.0
  private val bullets = ListBuffer[Bullet]()
  private val bulletImageViews = mutable.Map[Bullet, ImageView]()
  private val enemies = ListBuffer[Enemy]()
  private val enemyImageViews = mutable.Map[Enemy, ImageView]()

  private val enemySpawnInterval = 2000000000L
  private var lastEnemySpawnTime = 0L
  private var lastSpeedIncreaseTime = 0L
  private var primaryStage: PrimaryStage = _

  private val screenX: Double = 1280
  private val screenY: Double = 960

  private val playerStartX: Double = screenX / 2
  private val playerStartY: Double = screenY - 100

  def setPrimaryStage(stage: PrimaryStage): Unit = {
    primaryStage = stage
  }

  def initialize(): Unit = {
    val image = new Image("ch/makery/space_invaders/view/images/SPACESHIP.png")
    imageView.setImage(image)

    imageView.setFocusTraversable(true)

    imageView.setOnKeyPressed { event: KeyEvent =>
      event.code match {
        case KeyCode.Left if imageView.getLayoutX > 10 =>
          imageView.setLayoutX(imageView.getLayoutX - speed)
        case KeyCode.Right if imageView.getLayoutX < screenX - imageView.getFitWidth - 10 =>
          imageView.setLayoutX(imageView.getLayoutX + speed)
        case KeyCode.Up if imageView.getLayoutY > 0 =>
          imageView.setLayoutY(imageView.getLayoutY - speed)
        case KeyCode.Down if imageView.getLayoutY < screenY - imageView.getFitHeight - 50 =>
          imageView.setLayoutY(imageView.getLayoutY + speed)
        case KeyCode.Space =>
          val bullet = new Bullet(imageView.getLayoutX + imageView.getFitWidth / 2, imageView.getLayoutY, -speed)
          bullets += bullet

          val bulletImageView = new ImageView("ch/makery/space_invaders/view/images/BULLETPAINT.png")
          bulletImageView.setX(bullet.getX)
          bulletImageView.setY(bullet.getY)
          gamePane.children.add(bulletImageView)
          bulletImageViews += (bullet -> bulletImageView)
        case _ =>
      }
    }
  }

  private def checkCollisions(): Unit = {
    val bulletsToRemove = ListBuffer[Bullet]()
    val enemiesToRemove = ListBuffer[Enemy]()

    bullets.foreach { bullet =>
      enemies.foreach { enemy =>
        if (bullet.getX < enemy.getX + enemy.width && bullet.getX + bullet.width > enemy.getX &&
          bullet.getY < enemy.getY + enemy.height && bullet.getY + bullet.height > enemy.getY) {

          bulletsToRemove += bullet
          enemiesToRemove += enemy

          player.score.increment()
        }
      }
    }

    bullets --= bulletsToRemove
    enemies --= enemiesToRemove

    bulletsToRemove.foreach { bullet =>
      bulletImageViews.get(bullet).foreach { bulletImageView =>
        gamePane.children.remove(bulletImageView)
        bulletImageViews -= bullet
      }
    }

    enemiesToRemove.foreach { enemy =>
      enemyImageViews.get(enemy).foreach { enemyImageView =>
        gamePane.children.remove(enemyImageView)
        enemyImageViews -= enemy
      }
    }

    if (enemiesToRemove.nonEmpty) {
      enemies --= enemiesToRemove
    }

    enemies.foreach { enemy =>
      if (enemy.getY >= screenY - 80) {
        gameLoop.stop()
        showReplayDialog()
      }
    }
  }

  private def showReplayDialog(): Unit = {
    player.score.setHighestScore(player.score.getScore.toInt)
    Platform.runLater {
      val alert = new Alert(AlertType.Confirmation) {
        initOwner(primaryStage)
        title = "Game Over"
        headerText = s"Congratulations ${player.name}! Your current score is ${player.score.getScore}, Highest Score ${player.score.HighestScore}"
        contentText = "An enemy reached the bottom. Do you want to replay?"
      }

      val replayButtonType = new ButtonType("Replay")
      val quitButtonType = new ButtonType("Quit")

      alert.buttonTypes = Seq(replayButtonType, quitButtonType)

      val dialog = alert.dialogPane().getScene().getWindow().asInstanceOf[javafx.stage.Stage]


      dialog.onCloseRequest = _ => Platform.exit()

      val result = alert.showAndWait()

      result match {
        case Some(`replayButtonType`) =>
          bulletImageViews.values.foreach(gamePane.children.remove(_))
          enemyImageViews.values.foreach(gamePane.children.remove(_))

          bullets.clear()
          enemies.clear()
          bulletImageViews.clear()
          enemyImageViews.clear()
          speedEnemy = 3.0
          imageView.setLayoutX(playerStartX)
          imageView.setLayoutY(playerStartY)

          player.score.resetScore()

          initialize()
          startGameLoop()

        case Some(`quitButtonType`) =>
          MainApp.showWelcomePage()
        case _ =>
          Platform.exit()
      }
    }
  }

  val gameLoop: AnimationTimer = AnimationTimer(t => {
    if (lastSpeedIncreaseTime == 0L) lastSpeedIncreaseTime = t

    bullets.foreach { bullet =>
      bullet.update()

      bulletImageViews.get(bullet).foreach { bulletImageView =>
        bulletImageView.setX(bullet.getX)
        bulletImageView.setY(bullet.getY)

        if (bullet.getY < 0) {
          gamePane.children.remove(bulletImageView)
          bullets -= bullet
          bulletImageViews -= bullet
        }
      }
    }

    if (t - lastEnemySpawnTime > enemySpawnInterval) {
      lastEnemySpawnTime = t
      val enemyX = Random.nextDouble() * (screenX - 100) // Make sure enemy appears within the screen
      val enemy = new Enemy(enemyX, 0, speedEnemy)
      enemies += enemy

      val enemyImageView = new ImageView("ch/makery/space_invaders/view/images/ENEMY1.png")
      enemyImageView.setX(enemy.getX)
      enemyImageView.setY(enemy.getY)
      gamePane.children.add(enemyImageView)
      enemyImageViews += (enemy -> enemyImageView)
    }

    enemies.foreach { enemy =>
      enemy.update()

      enemyImageViews.get(enemy).foreach { enemyImageView =>
        enemyImageView.setX(enemy.getX)
        enemyImageView.setY(enemy.getY)

        if (enemy.getY > screenY) {
          gamePane.children.remove(enemyImageView)
          enemies -= enemy
          enemyImageViews -= enemy
        }
      }
    }

    if (t - lastSpeedIncreaseTime >= 10000000000L) {
      speedEnemy += 1.0
      lastSpeedIncreaseTime = t
    }

    checkCollisions()
  })

  def startGameLoop(): Unit = {
    gameLoop.start()
  }

  initialize()
}
