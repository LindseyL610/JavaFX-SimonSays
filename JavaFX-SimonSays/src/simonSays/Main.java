package simonSays;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class Main extends Application {
	
	Label scoreLbl;
	
	Button startBtn = new Button("Start");

	Button yellowBtn = new Button();
	Button blueBtn = new Button();
	Button redBtn = new Button();
	Button greenBtn = new Button();
	
	final Button[] btns = { yellowBtn, blueBtn, redBtn, greenBtn };
	
	int count, seqIdx, userIdx, score = 0;
	
	ArrayList<Button> sequence = new ArrayList<Button>();
	SequentialTransition seq;
	
	Random rand = new Random();
	
	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage myStage) {
		
		// Set up score label.
		scoreLbl = new Label("Score: " + score);
		scoreLbl.setPrefSize(80, 40);
		scoreLbl.setFont(new Font("Serif", 18));
		
		// Format buttons.
		startBtn.setPrefSize(80, 40);
		startBtn.setFont(new Font("Serif", 18));
		
		yellowBtn.setPrefSize(230, 230);
		yellowBtn.setId("yellow");
		
		blueBtn.setPrefSize(230, 230);
		blueBtn.setId("blue");
		
		redBtn.setPrefSize(230, 230);
		redBtn.setId("red");
		
		greenBtn.setPrefSize(230, 230);
		greenBtn.setId("green");
		
		// Disable all buttons except the start button.
		setButtons(true, false);
		
		// Set event handlers.
		startBtn.setOnAction(this::handleButtonAction);
		for(Button button : btns) button.setOnAction(this::handleButtonAction);
		
		// Set layout for buttons.
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.add(yellowBtn, 0, 0);
		gridPane.add(blueBtn, 1, 0);
		gridPane.add(redBtn, 0, 1);
		gridPane.add(greenBtn, 1, 1);
		
		// Set layout for start button and score label.
		HBox top = new HBox();
		top.setAlignment(Pos.CENTER);
		top.setSpacing(100);
		top.setPadding(new Insets(20, 0, 0, 0));
		top.getChildren().addAll(startBtn, scoreLbl);
		
		// Set up root node.
		BorderPane root = new BorderPane();
		root.setTop(top);
		root.setCenter(gridPane);
		
		// Set up scene.
		Scene scene = new Scene(root, 640, 640);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		// Set up and display stage.
		myStage.setTitle("Simon Says");
		myStage.setScene(scene);
		myStage.show();
	}
	
	// Enables or disables buttons.
	void setButtons(Boolean colorB, Boolean startB) {
		for(Button button : btns) button.setDisable(colorB);
		
		startBtn.setDisable(startB);
	}
	
	// Handles button actions.
	void handleButtonAction(ActionEvent e) {
		if(e.getSource() == startBtn) {
			if(startBtn.getText() != "Restart") startBtn.setText(("Restart"));
			setGame();
			playFunc();
		}
		else checkUserInput((Button) e.getSource());
	}
	
	// Resets all the variables.
	void setGame() {
		score = 0;
		scoreLbl.setText("Score: " + score);
		seqIdx = 0;
		userIdx = 0;
		count = 1;
		sequence.clear();
		seq = new SequentialTransition();
		seq.setOnFinished(e -> setButtons(false, false)); // Enables all buttons after seq animation completes.
	}
	
	// Enables gameplay.
	void playFunc() {
			setButtons(true, true); // Disable all buttons so they can't be clicked while seq animation is running.
			setSequence();
			displaySequence();
	}
	
	// Adds a randomly selected color to the sequence.
	void setSequence() {
		for(int i = seqIdx; i < count; i++) sequence.add(btns[rand.nextInt(4)]);
	}
	
	// Adds new selections to the sequence and plays the sequence.
	void displaySequence() {	
		while(seqIdx < count) {
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), sequence.get(seqIdx++));
			fadeTransition.setFromValue(1.0);
			fadeTransition.setToValue(0.1);
			fadeTransition.setCycleCount(2);
			fadeTransition.setAutoReverse(true);
			seq.getChildren().add(fadeTransition);
		}
	
		seq.play();
	}
	
	// Checks that the user's selection matches the sequence.
	void checkUserInput(Button input) {
		if(sequence.get(userIdx++).getId() != input.getId()) incorrectInputFunc(); // Calls method to end game if input was incorrect.
		// Calls method to add another color to the sequence if the user's input matches the entire sequence.
		else if(userIdx == count) {
			userIdx = 0;
			count++;
			score++;
			scoreLbl.setText("Score: " + score);
			playFunc();
		}
	}
	
	// Ends the game if the user gave incorrect input.
	void incorrectInputFunc() {
		setButtons(true, false); // Enables the "Start/Restart" button, disables the color square buttons. 
		
		// Displays alert informing the user the game is over and displaying the user's score.
		Alert gameOver = new Alert(AlertType.ERROR);
		gameOver.setTitle("Game Over");
		gameOver.setHeaderText("Game Over");
		gameOver.setContentText("You scored " + score + ". Click 'Restart' to play again.");
		gameOver.show();
	}
}