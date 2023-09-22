package ch.makery.space_invaders.view

import ch.makery.space_invaders.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class WelcomeController(){
  def showHelp(): Unit = {
    MainApp.showPlayerGuide()
  }

  def showStory(): Unit = {
    MainApp.showBackgroundStory()
  }

  def backMain(): Unit = {
    MainApp.showWelcomePage()
  }

  def getStarted(): Unit = {
    MainApp.showPlayerPage()
  }
}