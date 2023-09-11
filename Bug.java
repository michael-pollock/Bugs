import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.animation.*;


public class Bug extends Group {
	private double xVel;
	private double yVel;
	private boolean click;
	private boolean side;
	private boolean helped;
	private boolean squashed;
	private long timeOfAccident;
	private long surgery;
	private boolean revive;
	private boolean terminal;
	private long TOD;

	//Wanted to access groups in method bugDoc and walk
	Group legs = new Group();
	Group core = new Group();
	Group headG = new Group();
	public Bug(double r, double g, double b, double t){

		//Continuously check for this method
		AnimationTimer timer = new AnimationTimer(){
			@Override
			public void handle(long time){
				if (!terminal && squashed && !helped){
					timeOfAccident = time;
					helped = true;
				}
				bugDoc(); //Bug Doctor
				if (!squashed && helped){
					surgery = time;
					double waitTime = ((surgery - timeOfAccident)/60000000);
					if (waitTime > 20){
						terminal = true;
						TOD = time;
					} else {
						revive = true;
					}
					bugDoc(); //Bug Doctor
				}
				bugMort();
			}
		};
		timer.start();

		//Left and Right Antenna
		Group antenna = new Group();
		Line lAntenna = new Line(-4, 0, 0, 20);
		antenna.getChildren().add(lAntenna);
		Line rAntenna = new Line(4, 0, 0, 20);
		antenna.getChildren().add(rAntenna);
		this.getChildren().add(antenna);

		//Head
		Circle head = new Circle(0, 30, 20);
		headG.getChildren().add(head);
		this.getChildren().add(headG);

		//Left legs
		Line ltLeg = new Line(0, -10, -48, -25);
		legs.getChildren().add(ltLeg);
		Line lmLeg = new Line(0, 0, -56, 0);
		legs.getChildren().add(lmLeg);
		Line lbLeg = new Line(0, 10, -48, 25);
		legs.getChildren().add(lbLeg);

		//Right legs
		Line rtLeg = new Line(0, -10, 48, -25);
		legs.getChildren().add(rtLeg);
		Line rmLeg = new Line(0, 0, 56, 0);
		legs.getChildren().add(rmLeg);
		Line rbLeg = new Line(0, 10, 48, 25);
		legs.getChildren().add(rbLeg);
		this.getChildren().add(legs);
		legs.setTranslateY(70);

		//Body
		Ellipse body = new Ellipse(32, 40);
		body.setFill(new Color(r,g,b,t));
		core.getChildren().add(body);

		//Add random sized spots to shell
		Circle spot1 = new Circle(-15, -20, (int)(Math.random()*10));//8
		spot1.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot1);

		Circle spot2 = new Circle(-17, 0, (int)(Math.random()*10));//4
		spot2.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot2);

		Circle spot3 = new Circle(-13, 20, (int)(Math.random()*10));//10
		spot3.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot3);

		Circle spot4 = new Circle(12, -23, 3+(int)(Math.random()*7));//9
		spot4.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot4);

		Circle spot5 = new Circle(15, 3, 3+(int)(Math.random()*7));//6
		spot5.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot5);

		Circle spot6 = new Circle(19, 17, 3+(int)(Math.random()*7));//2
		spot6.setFill(new Color(Math.random(),Math.random(),Math.random(),Math.random()));
		core.getChildren().add(spot6);


		//Add wingline
		Line wingLine = new Line(0, -40, 0, 40);
		core.getChildren().add(wingLine);
		this.getChildren().add(core);
		core.setTranslateY(70);

		//Create bug movement!
		EventHandler<MouseEvent> squashBug = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				if (e.getButton() == MouseButton.PRIMARY){
					squashed = true;
					headG.setTranslateY(headG.getTranslateY()-5);
					core.setScaleX(1.2);
					core.setRotate((Math.random()-.5)*180);
					//wingLine.setStrokeWidth(10);
					legs.setScaleX(1.2);
					legs.setScaleY(2);
				} else if (e.getButton() == MouseButton.SECONDARY){
					if (squashed){
						bugDoc();
					}
				}

			}
		};
		this.setOnMousePressed(squashBug);
	}

	//Rotates legs to left, center, and right, creating a walking appearance
	public void moveLegs(){
		click = !click;
		if (click && !side){
			legs.setRotate(10);
			side = !side;
		} else if (click && side){
			legs.setRotate(-10);
			side = !side;
		} else {
			legs.setRotate(0);
		}
	}

	//Move bug around on screen where it is allowed to go.
	//Prevent run off from sides, hospital entrance, and mortuary
	public void walk(){
		if (!squashed){
			double xPos = this.getTranslateX();
			double yPos = this.getTranslateY();
			double speedLim = 5;

			//Bounces bug off the left and right wall
			 if (yPos > 350 || yPos < 150){
				if (xPos < 50 || xPos > 650){
					xVel = -xVel;
				}
			} else if (xPos < 50 || xPos > 750){
				xVel = -xVel;
			} else {
				xVel += (Math.random() - .5);
			}

			//Prevent bug from getting a speeding ticket
			if (xVel > speedLim){ xVel = speedLim;}
			if (xVel < -speedLim){ xVel = -speedLim;}

			//Bounce bug off the top and bottom wall
			if (xPos > 650){
				if (yPos < 150 || yPos > 350){
					yVel = -yVel;
				}
			} else if (yPos < 0 || yPos > 500){
				yVel = -yVel;
			} else {
				yVel += (Math.random() - .5);
			}
			//No speeding in any direction
			if (yVel > speedLim){ yVel = speedLim;}
			if (yVel < -speedLim){ yVel = -speedLim;}

			moveLegs();
			xPos = xPos + xVel;
			yPos = yPos + yVel;
			double rotation = Math.atan2(yVel, xVel) * 180.0 / Math.PI + 90.0;
			this.setRotate(rotation);
			this.setTranslateX(xPos);
			this.setTranslateY(yPos);
		}
	}

	//Award one point for squashed bug
	public int getSquashed(){
		if (squashed){ return 1; }
		else { return 0; }
	}

	//Increase revive points by one
	public int getRevivePoint(){
		if (revive){ return 1; }
		else { return 0; }
	}

	//Increase revive points by one
	public int getTerminalPoint(){
		if (terminal){ return 1; }
		else { return 0; }
	}

	//Bug Doctor
	public void bugDoc(){
		if (squashed && this.getTranslateX() > 700 && this.getTranslateY() > 350){
			core.setRotate(0);
			headG.setTranslateY(0);
			core.setScaleX(1);
			legs.setScaleX(1);
			legs.setScaleY(1);
			squashed = false;
		} else if (!terminal && !squashed && this.getTranslateX() > 700 && this.getTranslateY() > 360){
			this.setTranslateX(300);
			this.setTranslateY(300);
		} else if (terminal && !squashed && this.getTranslateX() > 700 && this.getTranslateY() > 360){
			squashed = true;
			this.setTranslateX(750);
			this.setTranslateY(75);
			bugMort(); // Bug Mortician
		}
	}

	//Bug Mortician
	public void bugMort(){
		if (squashed && this.getTranslateX() > 700 && this.getTranslateY() < 140){
			this.setVisible(false);
		}
	}
}