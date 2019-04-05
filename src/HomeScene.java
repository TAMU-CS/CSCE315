import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/*
  The Home Menu
  Should display options to the user before starting the game
  1. Non-server client game 1-p
  2. Non-server client game 2-p
  3. Server-client 1-p
  4. Server-client 2-p
  5. Server 2-clients
*/


public class HomeScene extends Application {

  public static void main(String[] args) {
    //launch the Application
    launch(args);
  }//end main

  @Override
  public void start(Stage primaryStage) {

    // Testing purposes for now
    // First show a list of options and then segue to that specific scene

    // Set the scene
    primaryStage.setScene(BoardScene.create(primaryStage));

    // Show the window
    primaryStage.show();
    
  }
}
