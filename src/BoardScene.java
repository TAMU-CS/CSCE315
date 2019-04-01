import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class BoardScene {

  static int houses, seeds = 0;

  public static Scene create(Stage stage) {

    stage.setTitle("Kalah");

    Label textLabel1 = new Label("Enter the number of houses 4-9");
    Label textLabel2 = new Label("Enter the seeds 1-10");

    // Inputs for house and seed count
    TextField textField1 = new TextField();
    textField1.setMaxWidth(55);
    TextField textField2 = new TextField();
    textField2.setMaxWidth(55);

    Label test = new Label("test");
    test.setVisible(false);

    Button submitButton = new Button("Submit");
    // Submit handler will hide the input inquiries and show the board
    submitButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Integer feedback1, feedback2 = 0; // variable that tells us what is sent from the textfield

        if(textField1.getText() != null && !textField1.getText().isEmpty() &&
          textField2.getText() != null && !textField2.getText().isEmpty()) {
          feedback1 = Integer.valueOf(textField1.getText()); // houses
          feedback2 = Integer.valueOf(textField2.getText()); // seeds

          // Check for valid houses
          if(feedback1 >= 4 && feedback1 <= 9) {
            houses = feedback1;
            System.out.printf("houses: %d", houses);
          } else {
            textLabel1.setText("Enter valid houses 4-9");
          }

          // Check for valid seeds
          if(feedback2 >= 1 && feedback1 <= 10) {
            seeds = feedback2;
            System.out.printf("seeds: %d", seeds);
          } else {
            textLabel2.setText("Enter valid seeds 1-10");
          }
        }
        // Checks for empty input
        else {
          if(textField1.getText().isEmpty()){
            textLabel1.setText("Invalid. Enter the number of houses");
          }
          if(textField2.getText().isEmpty()){
            textLabel2.setText("Invalid. Enter the number of seeds");
          }
        }
      }
    });

    VBox vbox = new VBox(20, textLabel1, textField1, textLabel2, textField2, submitButton, test);

    // The board
    HBox hbox = new HBox(20);

    vbox.setAlignment(Pos.CENTER);

    Scene boardScene = new Scene(vbox, 600, 300);

    return boardScene;
  }
}
