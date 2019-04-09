package test;

import javafx.application.Application;
import javafx.application.Platform;
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
	static Label scorePlayer1, scorePlayer2, curPlayer;
	static VBox vbox;
	static HBox topHBox;
	static HBox bottomHBox;
	static boolean isOnline; //is this 2player online/offline
	static boolean isServer; //is this server, if not , then client

	static public void initiateBoard(Board inboard) {
		vbox.getChildren().clear();

		HBox hbox1 = new HBox(15); // bottom row of buttons
		HBox hbox2 = new HBox(15); // top row of buttons
		hbox1.setAlignment(Pos.CENTER);
		hbox2.setAlignment(Pos.CENTER);

		scorePlayer1 = new Label("Score: ");
		hbox1.getChildren().add(scorePlayer1);

		houseButtons = new Button[houses * 2];

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
				//TODO
			});

		}
		vbox.getChildren().add(hbox1);

		scorePlayer2 = new Label("Score: ");
		hbox1.getChildren().add(scorePlayer2);
		
	}
	
	static public void updateBoard(Board inBoard) {
		Platform.runLater(new Runnable() {
			public void run() {
				initiateBoard(inBoard);
			}
		});

	}
	
	public static Scene create(Stage stage) {
		stage.setTitle("Kalah");

		// Input inquiries
		Label titleLabel = new Label("Welcome");
		Label menu1Label = new Label("Please Select Configuration");

		Button offlineButton = new Button("Offline");
		Button onlineButton = new Button("Online");
		Button serverButton = new Button("Server");
		Button onePlayerOff = new Button("1 Player");
		Button twoPlayerOff = new Button("2 PLayer");
		Button onePLayerOn = new Button("1 Player");
		Button twoPlayerOn = new Button("2 Player");

		Button clientButton = new Button("Client");
		// End of input inquiries

		Label textLabel1 = new Label("Enter the number of houses 4-9");
		Label textLabel2 = new Label("Enter the seeds 1-10");
		CheckBox cb = new CheckBox("Random");
		CheckBox ai = new CheckBox("AI");

		// Inputs for house and seed count
		TextField textField1 = new TextField();
		textField1.setMaxWidth(55);
		TextField textField2 = new TextField();
		textField2.setMaxWidth(55);

		Button submitButton = new Button("Submit");

		//online/offline, server/client buttons just change one state variable
		//and then move to next part of ui state
		onlineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isOnline = true;
				vbox.getChildren().clear();
				vbox.getChildren().addAll(titleLabel, serverButton, clientButton);
			}
		});

		offlineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isOnline = false;
				vbox.getChildren().clear();
				vbox.getChildren().addAll(textLabel1, textField1, textLabel2, textField2, cb, ai, submitButton);
			}
		});

		serverButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isServer = true;
				vbox.getChildren().clear();
				vbox.getChildren().addAll(textLabel1, textField1, textLabel2, textField2, cb, ai, submitButton);
				vbox.setAlignment(Pos.CENTER);
			}
		});

		clientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isServer = false;
				vbox.getChildren().clear();
				vbox.getChildren().addAll(bottomHBox, topHBox);
				vbox.setAlignment(Pos.CENTER);
			}
		});

		// Takes input and designs board based off of input
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
						//create server based off of feedback						
						initiateBoard(new Board());
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

		vbox = new VBox(20, titleLabel, onlineButton, offlineButton);
		vbox.setAlignment(Pos.CENTER);

		Scene boardScene = new Scene(vbox, 500, 300);

		return boardScene;
	}

}
