package ch.makery.space_invaders.view

import ch.makery.space_invaders.MainApp
import scalafx.application.Platform
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ButtonType, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class PlayerController(private val textField: TextField){

  def backMain(): Unit = {
    MainApp.showWelcomePage()
  }

  def playButton(): Unit = {
    if(textField.text.value != ""){
      val player_name = textField.text.value
      MainApp.showGame(player_name)
    }
    else{
      Platform.runLater {
        val alert = new Alert(AlertType.Confirmation) {
          title = "Player Name"
          headerText = "Enter Name"
          contentText = "Please enter your name"
        }

        val ButtonType = new ButtonType("Ok")


        alert.buttonTypes = Seq(ButtonType)

        val result = alert.showAndWait()

        result match{
          case Some(`ButtonType`) =>
          case _ =>
        }

        }
      }
    }

}