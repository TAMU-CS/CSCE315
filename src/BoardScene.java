import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class BoardScene {

	static int houses, seeds = 0; // input inquiries from user

	static Button[] houseButtons; // button array for houses

	static Label scorePlayer1, scorePlayer2;

	static VBox vbox;

	// input / socket variables
	static int time;
	static PrintWriter out;
	static boolean endgame;
	static boolean inputCounter;

	// call back function used by house buttons
	// used for get move
	public static void onSeedClick(int index) {
		if(!inputCounter) {
			return;
		}
		//check if input counter
		out.println(index + "");
		
		//reset the timer
		
		//reset input counter
		inputCounter = false;
		
	}

	//update all information from socket connection
	public static void updateInputConfiguration(int timeForMove, PrintWriter _out) {
		if (timeForMove == 0) {
			time = Integer.MAX_VALUE;
		} else {
			time = timeForMove;
		}
		
		out = _out;
		
		System.out.println("Input Configuration Updated!");
	}
	
	public static void updateBoard(Board board) {
		
	}

	public static Scene create(Stage stage) {

		stage.setTitle("Kalah");

		Label textLabel1 = new Label("Enter the number of houses 4-9");
		Label textLabel2 = new Label("Enter the seeds 1-10");

		// Inputs for house and seed count
		TextField textField1 = new TextField();
		textField1.setMaxWidth(55);
		TextField textField2 = new TextField();
		textField2.setMaxWidth(55);

		// houses.setVisible(false);

		Button submitButton = new Button("Submit");
		// Submit handler will hide the input inquiries and show the board
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Integer feedback1, feedback2 = 0; // variable that tells us what is sent from the textfield
				boolean validFeedback1, validFeedback2 = false;

				if (textField1.getText() != null && !textField1.getText().isEmpty() && textField2.getText() != null
						&& !textField2.getText().isEmpty()) {
					feedback1 = Integer.valueOf(textField1.getText()); // houses
					feedback2 = Integer.valueOf(textField2.getText()); // seeds

					// Check for valid houses
					if (feedback1 >= 4 && feedback1 <= 9) {
						houses = feedback1;
						System.out.printf("houses: %d", houses);
						validFeedback1 = true;
					} else {
						textLabel1.setText("Enter valid houses 4-9");
						validFeedback1 = false;
					}

					// Check for valid seeds
					if (feedback2 >= 1 && feedback1 <= 10) {
						seeds = feedback2;
						System.out.printf("seeds: %d", seeds);
						validFeedback2 = true;
					} else {
						textLabel2.setText("Enter valid seeds 1-10");
						validFeedback2 = false;
					}

					// Readjust the window with the board
					if (validFeedback1 && validFeedback2) {
						HBox hbox1 = new HBox(15); // bottom row of buttons
						HBox hbox2 = new HBox(15); // top row of buttons
						hbox1.setAlignment(Pos.CENTER);
						hbox2.setAlignment(Pos.CENTER);

						scorePlayer1 = new Label("Score: ");
						hbox1.getChildren().add(scorePlayer1);

						houseButtons = new Button[houses * 2];
						for (int i = 0; i < 5; i++) {
							vbox.getChildren().remove(0);
						}

						// Code for adding top buttons
						for (int i = houses; i < houses * 2; i++) {
							houseButtons[i] = new Button("" + seeds);
							hbox2.getChildren().add(houseButtons[i]);

						}
						vbox.getChildren().add(hbox2);

						// Code for adding bottom buttons
						for (int i = 0; i < houses; i++) {
							houseButtons[i] = new Button("" + seeds);
							hbox1.getChildren().add(houseButtons[i]);

							// set the event action
							final int index = i;
							houseButtons[i].setOnAction(E -> {
								// call on click handle
								onSeedClick(index);
							});

						}
						vbox.getChildren().add(hbox1);

						scorePlayer2 = new Label("Score: ");
						hbox1.getChildren().add(scorePlayer2);

						vbox.setAlignment(Pos.CENTER);
					}
				}
				// Checks for empty input
				else {
					if (textField1.getText().isEmpty()) {
						textLabel1.setText("Invalid. Enter the number of houses");
					}
					if (textField2.getText().isEmpty()) {
						textLabel2.setText("Invalid. Enter the number of seeds");
					}
				}
			}
		});

		vbox = new VBox(20, textLabel1, textField1, textLabel2, textField2, submitButton);

		vbox.setAlignment(Pos.CENTER);

		Scene boardScene = new Scene(vbox, 500, 300);

		return boardScene;
	}
}
