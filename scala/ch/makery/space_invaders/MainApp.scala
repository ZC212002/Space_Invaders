package ch.makery.space_invaders

import ch.makery.space_invaders.view.GameController
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp {
  var player_name: String= ""
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val rootLoader = new FXMLLoader(rootResource, NoDependencyResolver)

  val roots: jfxs.layout.BorderPane = rootLoader.load[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Space Invaders"

    scene = new Scene {
      root = roots
    }
  }

  def showBackgroundStory(): Unit = {
    val welcomeResource = getClass.getResource("view/Story.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)

    welcomeLoader.load()

    val root = welcomeLoader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(root)
  }

  def showPlayerGuide(): Unit = {
    val welcomeResource = getClass.getResource("view/About.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)


    welcomeLoader.load()

    val root = welcomeLoader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(root)
  }




  def showGame(player_name:String): Unit = {
    this.player_name = player_name
    val welcomeResource = getClass.getResource("view/SpaceInvaders.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)

    // Load the FXML
    val welcomePane: jfxs.layout.AnchorPane = welcomeLoader.load[jfxs.layout.AnchorPane]

    // Get the controller
    val welcomeController: GameController#Controller = welcomeLoader.getController[GameController#Controller]

    // Start the game loop
    welcomeController.startGameLoop()

    // Add the pane to the center of the BorderPane
    roots.setCenter(welcomePane)
  }

  def showWelcomePage(): Unit = {
    val welcomeResource = getClass.getResource("view/Welcome.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)

    // Start the game loop
    welcomeLoader.load()

    val root = welcomeLoader.getRoot[jfxs.layout.AnchorPane]
    // Add the pane to the center of the BorderPane
    this.roots.setCenter(root)
  }

  def showPlayerPage(): Unit = {
    val welcomeResource = getClass.getResource("view/Player.fxml")
    val welcomeLoader = new FXMLLoader(welcomeResource, NoDependencyResolver)

    // Start the game loop
    welcomeLoader.load()

    val root = welcomeLoader.getRoot[jfxs.layout.AnchorPane]
    // Add the pane to the center of the BorderPane
    this.roots.setCenter(root)
  }


  showWelcomePage()
}
