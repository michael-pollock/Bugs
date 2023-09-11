//Name: Michael Pollock
//Time: 9 Hours
//References: [1] Roommate Lauren helped me name first bug.

//[2] Audio clip from: http://soundbible.com/1984-Woah-Male.html
//Title: Woah Male
//Uploaded: 02.23.12
//License: Attribution 3.0
//Recorded by Sound Explorer

//[3] https://freesound.org/people/xtrgamr/sounds/257780/

//[4] https://freesound.org/people/fotoshop/sounds/47356/

//[5] https://soundcloud.com/alumomusic/vice
//[6] Dr Kow's code for creating a mouse drag event

//[7] Audio clip from http://soundbible.com/1496-Japanese-Temple-Bell-Small.html
//Title: Japanese Temple Bell Small
//Uploaded: 07.10.10
//License: Attribution 3.0
//Recorded by Mike Koenig

//[8] Audio clip from http://soundbible.com/666-Scream-Of-Joy.html
//Title: Scream Of Joy
//Uploaded: 06.27.09
//License: Attribution 3.0
//Recorded by Mike Koenig

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.scene.input.MouseDragEvent.*;

public class BugApp extends Application {
	int clickNum = 0;
	int newBugs = 5;
	int points = 0;
	int oldPoints = 0;
	int terminalPoints = 0;
	int oldTerminalPoints = 0;
	int revivePoints = 0;
	int oldRevivePoints = 0;
	int bugsLeft = 0;
	int bugsLetOut = 0;
	boolean stopped = false;
	private Node activeNode = null; //[6] Code from Dr. Kow

	@Override
	public void start(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600);
		stage.setTitle("Buggy App");
		stage.setScene(scene);
		stage.show();
		AudioClip woah = new AudioClip(getClass().getResource("woah.wav").toString()); //[2]
		AudioClip oof1 = new AudioClip(getClass().getResource("oof1.wav").toString()); //[3]
		AudioClip oof2 = new AudioClip(getClass().getResource("oof2.wav").toString()); //[4]
		AudioClip vice = new AudioClip(getClass().getResource("Alumo - Vice.wav").toString()); //[5]
		AudioClip bell = new AudioClip(getClass().getResource("bell.wav").toString()); //[7]
		AudioClip woohoo = new AudioClip(getClass().getResource("woohoo.wav").toString()); //[8]

		if (!stopped){
			vice.play();
		}

		//Grid
		for(int x = 0; x < 800; x = x + 20) {
			Line verticalLine = new Line(x, 0, x, 600);
			root.getChildren().add(verticalLine);
			verticalLine.setStroke(new Color(Math.random(), Math.random(), Math.random(), Math.random()));
		}

		for(int y = 0; y < 600; y = y + 20) {
			Line horizontalLine = new Line(0, y, 800, y);
			root.getChildren().add(horizontalLine);
			horizontalLine.setStroke(new Color(Math.random(), Math.random(), Math.random(), Math.random()));
		}

		//My original bugs for whom I care too much to delete
		Bug babe = new Bug(.5, .1, .3, 1); //[1]
		root.getChildren().add(babe);

		Bug hunk = new Bug(1, 0, 0, 1);
		root.getChildren().add(hunk);

		Bug randy = new Bug(Math.random(),Math.random(),Math.random(), Math.random());
		root.getChildren().add(randy);

		//Move bugs
		babe.setTranslateX(300);
		babe.setTranslateY(300);
		hunk.setTranslateX(300);
		hunk.setTranslateY(300);
		randy.setTranslateX(300);
		randy.setTranslateY(300);

		Bug[] bugs = new Bug[newBugs];
		for (int i = 0; i < newBugs; i++){
			bugs[i] = new Bug(Math.random(),Math.random(),Math.random(),(Math.random()/5 + .8));
			bugs[i].setTranslateX(300);
			bugs[i].setTranslateY(300);
		}
		Group myBugs = new Group();
		root.getChildren().add(myBugs);

		//Score text
		Text score = new Text("There are "+bugsLeft+" bugs left");
		score.setFont(Font.font("Impact", 30));
		score.setStroke(Color.GREY);
		root.getChildren().add(score);
		score.setTranslateY(50);
		score.setTranslateX(15);

		//Score text
		Text music = new Text("Press any key to pause or unpause the music.");
		music.setFont(Font.font("Impact", 20));
		music.setStroke(Color.GREY);
		root.getChildren().add(music);
		music.setTranslateX(200);
		music.setTranslateY(90);

		EventHandler<MouseEvent> bugClick = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				clickNum++;
				//Add bugs until no bugs left to add
				if (clickNum <= newBugs){
					myBugs.getChildren().add(bugs[clickNum-1]);
					bugsLetOut++;
				}

				//That's all the bugs Text
				Text bugLimit = new Text("That's all \nthe bugs!");
				bugLimit.setTranslateX(300);
				bugLimit.setTranslateY(300);
				bugLimit.setFill(Color.RED);
				if (clickNum > newBugs){
					root.getChildren().add(bugLimit);
				}
			}
		};
		scene.setOnMousePressed(bugClick);


		//[6] Start of code from Dr. Kow, adapted to create draggable bugs--------------
		EventHandler<MouseEvent> getMouse = new EventHandler <MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				activeNode = e.getPickResult().getIntersectedNode();
				//Now figure out who the "Thing1" ancestor is:
				while (activeNode != null && !(activeNode instanceof Bug)) {
					activeNode = activeNode.getParent();

				}
			}
		};
		scene.setOnDragDetected(getMouse);

		EventHandler<MouseEvent> getMouse2 = new EventHandler <MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				if (activeNode != null) {
					double nodeHeight = activeNode.getBoundsInParent().getHeight();
					activeNode.setTranslateX(e.getX());
					activeNode.setTranslateY(e.getY()-nodeHeight/2); //Click and drag bug from middle instead of top
				}
			}
		};
		scene.setOnMouseDragged(getMouse2);

		EventHandler<MouseEvent> getMouse3 = new EventHandler <MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				activeNode = null;
			}
		};
		scene.setOnMouseReleased(getMouse3);
		//[6] End of code from Dr. Kow------------------------------------------------

		EventHandler<KeyEvent> musicToggle = new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent e){
				if (!stopped){
					vice.stop();
					music.setVisible(false);
					stopped = !stopped;
				} else {
					vice.play();
					stopped = !stopped;
				}
			}
		};
		scene.setOnKeyPressed(musicToggle);

		AnimationTimer t = new AnimationTimer(){
			@Override
			public void handle(long time){
				//Add leg movement to bugs
				babe.walk();
				hunk.walk();
				randy.walk();
				for (int i = 0; i < clickNum && i < newBugs; i++){
					bugs[i].walk();
				}

				//Recieve one point when a bug is clicked on
				points = 0;
				for (int i = 0; i < newBugs; i++){
					points += bugs[i].getSquashed();
				}
				points += babe.getSquashed();
				points += hunk.getSquashed();
				points += randy.getSquashed();
				bugsLeft = bugsLetOut + 3 - points;

				//Print points depending on how many you have
				if (points == 1){
					score.setText("There are "+bugsLeft+" bugs left \nYou have squashed "+points+" BUG!");
				} else if (points > 1){
					score.setText("There are "+bugsLeft+" bugs left \nYou have squashed "+points+" BUGS!");
				} else {
					score.setText("There are "+bugsLeft+" bugs left");
				}

				//Play noise when bug is clicked on. Thought about adding bug noises in the bug class but didn't remember
				//how to avoid loading something in for every object
				if (oldPoints != points){
					if (Math.random() > .8){
						woah.play();
					} else if (Math.random() > .5){
						oof1.play();
					} else {
						oof2.play();
					}
				}
				oldPoints = points;

				//Play bell sound if a bug gets saved
				revivePoints = 0;
				for (int i = 0; i < newBugs; i++){
					revivePoints += bugs[i].getRevivePoint();
				}
				revivePoints += babe.getRevivePoint();
				revivePoints += hunk.getRevivePoint();
				revivePoints += randy.getRevivePoint();

				if (oldRevivePoints != revivePoints){
					woohoo.play();
				}
				oldRevivePoints = revivePoints;
			}
		};
		t.start();

		//Add hole to screen
		Circle hole = new Circle(300, 350, 100);
		hole.setFill(Color.BLACK);
		root.getChildren().add(hole);

		//Bug Hospital for squished bugs. In bug code, when within similar coordinates, bug essentially resets
		Rectangle bugHospital = new Rectangle(700, 450, 100, 150);
		bugHospital.setStroke(Color.RED);
		bugHospital.setFill(Color.WHITE);
		root.getChildren().add(bugHospital);

		Line vertCross = new Line(725, 525, 775, 525);
		vertCross.setStrokeWidth(15);
		vertCross.setStroke(Color.RED);
		root.getChildren().add(vertCross);

		Line horCross = new Line(750, 500, 750, 550);
		horCross.setStrokeWidth(15);
		horCross.setStroke(Color.RED);
		root.getChildren().add(horCross);

		//Bug morgue for terminally squished bugs
		Rectangle bugMorgue = new Rectangle(700, 0, 100, 150);
		bugMorgue.setStroke(Color.GREEN);
		bugMorgue.setFill(Color.BLACK);
		root.getChildren().add(bugMorgue);

		Group cross = new Group();
		Line vertCross1 = new Line(750, 50, 750, 100);
		vertCross1.setStrokeWidth(15);
		vertCross1.setStroke(Color.RED);
		cross.getChildren().add(vertCross1);

		Line horCross1 = new Line(725, 75, 775, 75);
		horCross1.setStrokeWidth(15);
		horCross1.setStroke(Color.RED);
		cross.getChildren().add(horCross1);
		cross.setRotate(45);
		root.getChildren().add(cross);

	}
}