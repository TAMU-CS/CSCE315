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
import javafx.animation.*;
import javafx.util.Duration;

public class BoardScene {

	static int houses, seeds = 0; // input inquiries from user
	static Button[] houseButtons; // button array for houses
	static Label scorePlayer1, scorePlayer2, curPlayerLabel, notifLabel, timerLabel;
	static VBox vbox;
	static HBox topHBox;
	static HBox bottomHBox;
	static timerUI timerObj;
	static boolean isOnline; // is this 2player online/offline
	static boolean isServer; // is this server, if not , then client
	static boolean isAI; // should we take ai into account
	static int AIDepth;
	static int timeForMove;
	static boolean debugging = true; // displays stuff into cli
	static Board board; // this is board state

	// start local game will just run the game locally
	static private void startLocalGame() {

		Platform.runLater(new Runnable() {
			public void run() {
				timerLabel = new Label("");
				// display the board and wait for inputs
				initiateBoard(true, board.curMove);
			}
		});
	}

	// start online game will initiate a server
	static private void startOnlineGame() throws IOException {
		// check if server or client
		if (isServer) {
			Platform.runLater(new Runnable() {
				public void run() {
					// display the board and wait for inputs
					initiateBoard(false, 0);
					notifLabel.setText("Waiting On Clients To Join!");
				}
			});

			// set up server object
			Server server = new Server();
			int port = 6666;

			server.start(port);

			// give user info
			int notCurMove = board.curMove == 1 ? 0 : 1;
			String resp;

			// handle the first move
			if (!isAI) {
				resp = server.sendMsg(board.curMove, board.curMove + " " + board.toString());
				resp = server.sendMsg(notCurMove, notCurMove + " " + board.toString());
			} else {
				resp = server.sendMsg(notCurMove, notCurMove + " " + board.toString());
			}

			while (true) {
				if (!isAI) {
					if (board.curMove == 0) {
						server.sendMsg(1, board.toString());
						resp = server.sendMsg(0, board.toString());
					} else {
						server.sendMsg(0, board.toString());
						resp = server.sendMsg(1, board.toString());
					}

					board.nextTurn(Integer.parseInt(resp));

					// check if endgame
					if (board.endgame()) {
						// send crucial info
						server.sendMsg(0, board.toString());
						server.sendMsg(1, board.toString());
						break;
					}

					Platform.runLater(new Runnable() {
						public void run() {
							// display the board and wait for inputs
							initiateBoard(false, 0);
						}
					});
				} else {
					if (board.curMove == 0) { // ai response
						resp = MiniMax.getMove(board.toString(), AIDepth, 0) + "";
						server.sendMsg(1, board.toString());
					} else { // human response
						resp = server.sendMsg(1, board.toString());
					}

					board.nextTurn(Integer.parseInt(resp));

					// check if endgame
					if (board.endgame()) {
						// send crucial info
						server.sendMsg(1, board.toString());
						break;
					}

					Platform.runLater(new Runnable() {
						public void run() {
							// display the board and wait for inputs
							initiateBoard(false, 0);
						}
					});
				}
			}

		} else { // client
			// start up client handler
			Client.startConnection("127.0.0.1", 6666);

			// based off of the board, create board
			String resp = Client.in.readLine();
			System.out.println(resp);
			Client.out.println("Ready");

			// get ch id
			String tokens[] = resp.split(" ");

			// setup board
			int plrPersp = Integer.parseInt(tokens[0]);
			board = new Board(tokens);
			houses = board.getHouses();
			System.out.println(board);

			// display board
			Platform.runLater(new Runnable() {
				public void run() {
					initiateBoard(true, plrPersp);
				}
			});

			while (true) {
				// get message from server
				resp = Client.in.readLine();
				tokens = resp.split(" ");
				board = new Board(tokens);
				houses = board.getHouses();
				System.out.println(board);
				if (board.endgame()) {
					Client.out.println("OK");
					break;
				}

				if (board.curMove != plrPersp) {
					Client.out.println("OK");
				}

				// initiate board
				Platform.runLater(new Runnable() {
					public void run() {
						// display the board and wait for inputs
						initiateBoard(true, plrPersp);
					}
				});
			}

			// print winners/result
			// initiate board
			Platform.runLater(new Runnable() {
				public void run() {
					// display the board and wait for inputs
					initiateBoard(false, plrPersp);
					int outcome = board.getOutcome();
					notifLabel
							.setText(outcome == 0 ? "It's a Tie!" : (outcome == 1 ? "Player 1 won!" : "Player 2 won!"));
				}
			});
		}
	}

	// initiates the ui for the board, sets up button events based on playerInput
	static public void initiateBoard(boolean playerInput, int plrPerspective) {
		vbox.getChildren().clear();

		// player title label
		if (isOnline) {
			curPlayerLabel = new Label(plrPerspective == 0 ? "Player 1" : "Player 2");
		} else {

			//begin timer
			timerObj = new BoardScene.timerUI(timeForMove, timerLabel, 0);
			vbox.getChildren().add(timerLabel);
			curPlayerLabel = new Label(plrPerspective == 0 ? "Player 1 Move" : "Player 2 Move");
		}
		vbox.getChildren().add(curPlayerLabel);

		HBox hbox1 = new HBox(15); // bottom row of buttons
		HBox hbox2 = new HBox(15); // top row of buttons
		hbox1.setAlignment(Pos.CENTER);
		hbox2.setAlignment(Pos.CENTER);

		// handle scoring
		String textP1Score = "Score P1: " + board.getScore(0);
		String textP2Score = "Score P2: " + board.getScore(1);
		scorePlayer1 = new Label(plrPerspective == 0 ? textP2Score : textP1Score);
		scorePlayer2 = new Label(plrPerspective == 0 ? textP1Score : textP2Score);

		// create a new notif label if one hasn't already been made
		if (notifLabel == null) {
			notifLabel = new Label("");
		} else {
			// blank the notif label before anything happens
			notifLabel.setText("");
			if (isOnline) {
				notifLabel.setText("Waiting on player " + (board.curMove + 1));
			}
		}

		hbox1.getChildren().add(scorePlayer1);

		// Code for adding top buttons (from other player perspective)
		houseButtons = new Button[houses * 2];
		for (int i = houses; i < houses * 2; i++) {
			// flip the houses
			houseButtons[i] = new Button("" + board.getSeeds(2 * houses - i - 1, plrPerspective == 1 ? 0 : 1));
			hbox2.getChildren().add(houseButtons[i]);

		}
		vbox.getChildren().add(hbox2);

		// Code for adding bottom buttons
		for (int i = 0; i < houses; i++) {
			houseButtons[i] = new Button("" + board.getSeeds(i, plrPerspective));
			hbox1.getChildren().add(houseButtons[i]);

			// offline
			if (playerInput && !isOnline) {
				final int index = i;
				houseButtons[i].setOnAction(E -> {
					// check if index is legal move
					if (!board.checkMove(index)) {
						notifLabel.setText("Invalid House Choice! It is empty!");
						return;
					}

					// update the board
					board.nextTurn(index);
					
					if(timeForMove != 0) {
						timerObj.time.pause();
					}

					// print board if debug is true
					if (debugging) {
						System.out.println(board);
					}

					// check for endgame
					if (!board.endgame()) {

						initiateBoard(true, board.curMove);

					} else { // display winner stop timer

						timerObj.time.pause();
						initiateBoard(false, board.curMove);
						int outcome = board.getOutcome();
						notifLabel.setText(
								outcome == 0 ? "It's a Tie!" : (outcome == 1 ? "Player 1 won!" : "Player 2 won!"));
						if (debugging) {
							System.out.println("Endboard: " + board);
						}
					}
				});
			} else if (playerInput && !isServer) { // client
				final int index = i;
				houseButtons[i].setOnAction(E -> {
					// check if player can move
					if (plrPerspective != board.curMove) {
						return;
					}

					// check if index is legal move
					if (!board.checkMove(index)) {
						notifLabel.setText("Invalid House Choice! It is empty!");
						return;
					}

					// update the board
					board.nextTurn(index);
					initiateBoard(false, plrPerspective);

					// print board if debug is true
					if (debugging) {
						System.out.println(board);
					}

					// tell server the move made
					Client.out.println(index);
				});
			}

		}
		hbox1.getChildren().add(scorePlayer2);

		vbox.getChildren().add(hbox1);

		// setup notification label
		vbox.getChildren().add(notifLabel);

	}

	// Updates the UI for the board based on player view
	static public void updateBoard(Board inBoard, int plrView) {
		Platform.runLater(new Runnable() {
			public void run() {

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
		Label textLabel3 = new Label("Enter the AI Difficulty 1-9");
		CheckBox cb = new CheckBox("Random");
		CheckBox ai = new CheckBox("AI");

		// Inputs for house and seed count
		TextField textField1 = new TextField();
		textField1.setMaxWidth(55);
		TextField textField2 = new TextField();
		textField2.setMaxWidth(55);
		TextField textField3 = new TextField();
		textField3.setMaxWidth(55);

		Button submitButton = new Button("Submit");

		// online/offline, server/client buttons just change one state variable
		// and then move to next part of ui state
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
				vbox.getChildren().addAll(textLabel1, textField1, textLabel2, textField2, textLabel3, textField3, cb,
						submitButton);
				textLabel3.setText("Enter time for move in seconds (0 means infinite): ");
			}
		});

		serverButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isServer = true;
				vbox.getChildren().clear();
				vbox.getChildren().addAll(textLabel1, textField1, textLabel2, textField2, textLabel3, textField3, cb,
						ai, submitButton);
				vbox.setAlignment(Pos.CENTER);
			}
		});

		clientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				isServer = false;
				vbox.getChildren().clear();

				notifLabel = new Label("Waiting for other player!");
				vbox.getChildren().add(notifLabel);

				Thread thread = new Thread() {
					public void run() {
						try {
							startOnlineGame();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
		});

		// Takes input and designs board based off of input
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// feedback list:
				// 1- houses
				// 2- seeds
				// 3- random
				// 4- ai
				Integer feedback1, feedback2, feedback5 = 0;
				boolean feedback3 = cb.isSelected();
				boolean feedback4 = ai.isSelected();
				boolean validFeedback1, validFeedback2 = false;

				// get process feedback
				if (textField1.getText() != null && !textField1.getText().isEmpty() && textField2.getText() != null
						&& !textField2.getText().isEmpty()) {
					feedback1 = Integer.valueOf(textField1.getText()); // houses
					feedback2 = Integer.valueOf(textField2.getText()); // seeds

					// Check for valid houses
					if (feedback1 >= 4 && feedback1 <= 9) {
						houses = feedback1;
						validFeedback1 = true;
					} else {
						textLabel1.setText("Enter valid houses 4-9");
						validFeedback1 = false;
					}

					// Check for valid seeds
					if (feedback2 >= 1 && feedback1 <= 10) {
						seeds = feedback2;
						validFeedback2 = true;
					} else {
						textLabel2.setText("Enter valid seeds 1-10");
						validFeedback2 = false;
					}

					// Readjust the window with the board
					if (validFeedback1 && validFeedback2) {
						if (debugging) { // display to cli, feedback
							System.out.println(
									"Input: " + feedback1 + " " + feedback2 + " " + feedback3 + " " + feedback4 + " ");
						}

						// clear out ui
						vbox.getChildren().clear();

						// set timer
						timeForMove = Integer.parseInt(textField3.getText());

						// create the board based on input
						board = new Board(feedback1, feedback2, feedback3);
						isAI = feedback4;
						if (isAI) { // get difficulty
							AIDepth = Integer.parseInt(textField3.getText());
							;
						}

						// create server based off of feedback
						// run this in a new thread
						Thread thread = new Thread() {
							public void run() {
								if (isOnline) { // connecting two clients
									try {
										startOnlineGame();
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else { // just play locally
									startLocalGame();
								}
							}
						};
						thread.start();

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

		Scene boardScene = new Scene(vbox, 500, 500);

		return boardScene;
	}

	// timer class
	public static class timerUI {
		int timeLeft;
		Timeline time;

		timerUI(int t, Label ui, int moveCount) {
			if (t == 0) {
				return;
			} else {

				// setup the timeline to fire event every 1 second
				// event decrements the time on the Labels
				ui.setText(timeLeft + "");
				ui.setAlignment(Pos.CENTER);
				timeLeft = t * 1000;
				time = new Timeline(new KeyFrame(Duration.millis(1), e -> {
					timeLeft--;

					ui.setText("Time Left: " + ((double) timeLeft / (double) 1000));
					
					if (timeLeft <= 0) {
						System.out.println(timeLeft);
						time.pause();

						// based on player, nuke the entire field and say who won
						int curplr = board.curMove + 1;
						int oplr = curplr == 1 ? 2 : 1;
						vbox.getChildren().clear();
						vbox.getChildren().add(new Label("Player " + oplr + " Won because of time!"));
					}
					//
				}));
				time.setCycleCount(Timeline.INDEFINITE);
				time.play();
			}
		}
	}

}
