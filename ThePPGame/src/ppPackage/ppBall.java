/*
 *  Part of this program's code was cited by: 
 *  
 *  - Solutions to Assignment 1, 2, 3 (on myCourses, by professor Frank P. Ferrie)
 *  
 *  - ECSE-202 F2021 Assignment 1, 2, 3 (on myCourses, by professor Frank P. Ferrie)
 *  
 *  - notes and codes from Katerina Poulin's tutorials (as a TA in ECSE-202 for Fall 2021), in-person and online, at McGill University
 *  
 *  - the following website: https://www.educative.io/edpresso/how-to-convert-an-integer-to-a-string-in-java?fbclid=IwAR3Bk7JrRtkxF2_ub5qgkCFbIP2Sekb7kD8_haKCV4B9Blc2J0ld6Maq1lg
 *  	To determine how to convert an integer to a string in java: solution was the toString() method.
 *  
 *  - the following website: https://stackoverflow.com/a/17456465
 *  	To determine how to update a JLabel's text: solution was the setText() method
 *  
 *  - code from my previous submission on myCourses's ECSE-202 was used.
 *  
 *  - some notes found in professor Ferrie's lectures, and in The Art & Science of Java: an introduction to computer science by Eric Roberts (2008)
 */

package ppPackage;
import static ppPackage.ppSimParams.*;

import static ppPackage.ppSim.*;

import java.awt.Color;

import acm.graphics.*;
import acm.program.*;

public class ppBall extends Thread {
	
	// Declaring the instance variables of an instance of the class : x and y start positions, start velocity, start angle,
	// loss parameter, color and GProgram (of the class that calls the constructor)
	private double Xinit;									// declaring initial x-position instance variable 
	private double Yinit;									// declaring initial y-position instance variable
	private double Vo;										// declaring velocity instance variable
	private double theta;									// declaring theta instance variable
	private double loss;									// declaring loss instance variable 
	private Color color;									// declaring color instance variable
	private GraphicsProgram GProgram;						// Instance of ppSim class (this)
	
	GOval myBall; 											// Graphics object representing ball itself 
	ppPaddle RPaddle;										// a ppPaddle instance variable, to which myPaddle will be assigned
	ppPaddle LPaddle;										// a ppPaddle (consequently also ppPaddleAgent) instance variable, to which Agent Paddle will be assigned
	ppTable myTable;										// ppTable instance variable, to which myTable is assigned in constructor, in ppSim
	double X, Xo, Y, Yo;									// declaring the position variables outside run(), to make them accessible across class (and other classes)
	double Vx, Vy;											// declaring the velocity variables outside run(), to make them accessible across class (and other classes)
	boolean running;										// when true, the while loop runs
	/**
	* The constructor for the ppBall class copies parameters to instance variables, creates an
	* instance of a GOval to represent the ping pong ball, and adds it to the display. 
	* @param Xinit - starting position of the ball X (meters)
	* @param Yinit - starting position of the ball Y (meters)
	* @param Vo - initial velocity (meters/second)
	* @param theta - initial angle to the horizontal (degrees)
	* @param loss - loss on collision ([0,1])
	* @param color - ball color (Color)
	* @param GProgram - a reference to the ppSim class used to manage the display */
	
	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss, Color color, GraphicsProgram GProgram, ppTable myTable) {
		/*
		 * We assign instance variables of the class to the constructor's parameters 
		 * we use 'this' on the left hand side to refer to the variable present in the CLASS
		 */
		this.Xinit = Xinit; 							
		this.Yinit = Yinit;
		this.Vo = Vo; 
		this.theta = theta; 
		this.loss = loss; 
		this.color = color; 
		this.GProgram = GProgram;
		this.myTable = myTable;
	}
	
	public void run() {
		
		GPoint p = myTable.W2S(new GPoint(Xinit,Yinit+bSize));
		double ScrX = p.getX();                     						// obtaining starting x value in screen coordinates (pixels)
		double ScrY = p.getY();												// obtaining starting y value in screen coordinates (pixels)
		GOval myBall = new GOval(ScrX,ScrY,2*bSize*Xs,2*bSize*Ys);			// creating the ball using GOval and setting its coordinates and diameter; (diameter = radius times 2); we multiply x and y values by Xs and Ys to convert to pixel
		myBall.setFilled(true);												// stating that the ball is filled
		myBall.setColor(color);												// setting the color of the ball to the color specified in constructor
		GProgram.add(myBall);												// adding  ball to display using GProgram class/subclass that calls the constructor
	
		
		Xo = Xinit;															// set initial X position instance variable
		Yo = Yinit;															// set initial Y position instance variable
		double time = 0;													// time starts and 0, and counts up
		double Vt = bMass*g/ (4*Pi*bSize*bSize*k);							// terminal velocity - taking air resistance into account
		double Vox = (Vo*Math.cos(theta*Pi/180));							// X component of velocity
		double Voy = (Vo*Math.sin(theta*Pi/180));							// Y component of velocity
		
		/*
		 * Declaring kinetic energies (KEx and KEy) and potential energy (PE).
		 * Initializing each of them to ETHR - an arbitrary value that results in the sum of the ball's energies to
		 * being larger than the threshold (ETHR). This prevents the while loop from ceasing at the start. 
		 */
		
		double KEx= ETHR; 													
	    double KEy= ETHR; 		
	    double PE = ETHR;
	    
	    running = true;														// set to true to run the while(running) loop
	    
	    System.out.printf("\t\t\t Ball Position and Velocity\n");			// print a header line
	   
	    
		while (running) {
			
			/*
			 *  X, Y, Vx and Vy positions are updated by the while loop
			 */
			
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));				// update relative x-position
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;	// update relative y-position
			Vx = Vox*Math.exp(-g*time/Vt);						// update relative x-velocity
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;				// update relative y-velocity
			
			// if statements, that checks if we are hitting the ceiling or not, if we hit, terminate program
			
			// what happens when ball collides with floor 
			
			if (Vy<0 && (Y+Yo)<=bSize) {								// ball hits floor when its y-velocity is negative, and it is on the ground: its y-position is equal/lower than the ball's size
			    	 
			    KEx=0.5*bMass*Vx*Vx*(1-loss); 							// the ball's x-dir. kinetic energy reduces by factor 1-loss upon collision
			    KEy=0.5*bMass*Vy*Vy*(1-loss); 							// the ball's y-dir. kinetic energy reduces by factor 1-loss upon collision
				PE=0;													// when ball is on the ground, its potential energy is 0 (since height = 0)
			     
			    Vox=Math.sqrt(2*KEx/bMass); 							// initial x-velocity is recalculated with reduced x-dir. kinetic energy
			    Voy=Math.sqrt(2*KEy/bMass);								// initial y-velocity is recalculated with reduced y-dir. kinetic energy
			    
			    // set a minimum x velocity
			    if (Vox < VoMIN) {
			    	Vox = VoMIN;										// if x velocity is less than threshold VoMIN, it is set equal to VoMIN
			    }
			    if (Vx<0) Vox=-Vox;										// if x-velocity before impact was negative, reinitialized initial x-velocity must also stay negative
			    
			    time=0;													// time is reset to zero
				Xo+=X;													// initial x-position is reset to x-position before collision
				Yo=bSize;												// initial y-position is reset to the height of the ball from the ground
				X=0;													// relative x-position is reset to zero
				Y=0;													// relative y-position is reset to zero
				
			}
			
			// programming what happens when ball collides with right wall
			
			// check whether the ball is going to the left, and it hit the right end of table (potentially the RPaddle)
			if (Vx>0 && (X+Xo)>=(RPaddle.getP().getX() - ppPaddleW/2 - bSize)) {						// ball hits right wall when its x-velocity is positive, and is on right wall: ball's center's x-position is equal/larger than right wall's x-position
			    
				// check whether the ball has hit the paddle
				if (RPaddle.contact(X + Xo, Y+Yo)) {						// if there is contact, the game continues
					KEx=0.5*bMass*Vx*Vx*(1-loss); 							// the ball's x-dir. kinetic energy reduces by factor 1-loss upon collision
				    KEy=0.5*bMass*Vy*Vy*(1-loss); 							// the ball's y-dir. kinetic energy reduces by factor 1-loss upon collision
				    PE = g*bMass*Y;											// the ball's potential energy is assigned - it is not zero
				    	 
				    Vox=-Math.sqrt(2*KEx/bMass); 							// initial x-velocity is recalculated with reduced x-dir. kinetic energy
				    Voy=Math.sqrt(2*KEy/bMass);								// initial y-velocity is recalculated with reduced y-dir. kinetic energy
				   
					Vox=Vox*ppPaddleXgain;                                  // Scale X component of velocity
					
					// Set a maximum X velocity
					if (Vox >= VoMAX) {
						Vox = VoMAX;										// if X velocity is higher than VoMAX threshold, set X velocity to VoMAX
					}
					
					Voy=Voy*ppPaddleYgain*RPaddle.getV().getY();            // Scale the Y velocity in the same direction as paddle's movement
				    
				    time=0;													// time is reset to zero
					Xo=RPaddle.getP().getX() - ppPaddleW/2 - bSize;			// initial x-position is equal to paddle's x-position minus the radius of the ball
					Yo+=Y;													// initial y-position is reset to the y-position before impact
					X=0;													// relative x-position is reset to zero
					Y=0;													// relative y-position is reset to zero
				}
				// if there was no contact, it means the user lost, hence agent wins, while loop stops and scores are updated
				else {														
					// Agent score is incremented
					AgentPoint++;
					// The while loop is terminated, and the score displays are updated using kill()
					kill();	
				}
			}
			
			
			// programming what happens when ball collides with agent paddle
			
			// check whether the ball is going to the left, and it hit the right end of table (potentially the LPaddle)
			if (Vx<0 && (X+Xo)<=(LPaddle.getP().getX() + ppPaddleW/2 + bSize)) {						// ball hits left wall when its x-velocity is negative, and is on left wall: ball's center's x-position is equal/larger than the left wall's x-position
				
				// check whether the ball has hit the paddle
				if (LPaddle.contact(X+Xo,  Y+Yo)) {
					KEx=0.5*bMass*Vx*Vx*(1-loss); 							// the ball's x-dir. kinetic energy reduces by factor 1-loss upon collision
				    KEy=0.5*bMass*Vy*Vy*(1-loss);							// the ball's y-dir. kinetic energy reduces by factor 1-loss upon collision
				    PE = g*bMass*Y;											// the ball's potential energy is assigned - it is not zero
				    	 
					Vox=Math.sqrt(2*KEx/bMass); 							// initial x-velocity is recalculated with reduced x-dir. kinetic energy
					Voy=Math.sqrt(2*KEy/bMass);								// initial y-velocity is recalculated with reduced y-dir. kinetic energy
					
					Vox=Vox*LPaddleXgain;                                   // Scale X component of velocity
					
					// Set a maximum X velocity
					if (Vox >= VoMAX) {
						Vox = VoMAX;										// if X velocity is higher than VoMAX threshold, set X velocity to VoMAX
					}
					
					
					Voy=Voy*LPaddleYgain*LPaddle.getV().getY();             // Scale the Y velocity in the same direction as paddle's movement
					
					if (Vy<0) Voy=-Voy;										// if y-velocity before impact was negative, reinitialized initial y-velocity must also stay negative
					
					time=0;													// time is reset to zero
					Xo=LPaddle.getP().getX() + ppPaddleW/2 + bSize;			// initial x-position is equal to left wall's x-position plus the radius of the ball
					Yo+=Y;													// initial y-position is reset to the y-position before impact
					X=0;													// relative x-position is reset to zero
					Y=0;													// relative y-position is reset to zero
				}
				// else, the ball did not hit LPaddle; hence, the player wins round
				else {
					// Player score is incremented
					PlayerPoint++;
					// The while loop is terminated, and the score displays are updated using kill()
					kill();
				}

			}
			
			
			
			p = myTable.W2S(new GPoint(X+Xo-bSize, Y+Yo+1.2*bSize));	/* get current position of the ball in screen coordinates - add 1.2 times bSize to the absolute y-position 
			 															* so that the ball appears right above the ground when it lands, instead of appearing inside the ground upon landing
			 															*/
			
			GProgram.pause(timeLag.getValue()); 						// pausing the program (to make it slow enough for human eye) using GProgram class/subclass that calls the constructor
			ScrX = p.getX();											// store ball's absolute x-position in pixels
			ScrY = p.getY();											// store ball's absolute y-position in pixels

			myBall.setLocation(ScrX, ScrY);								// set the location of the ball (pixels)
			
			if (traceButton.isSelected()) {
				trace(ScrX, ScrY);										// trace the trajectory of the ball using trace method
			}
			
			GProgram.add(myBall);										// add the ball to the screen using GProgram given as argument in constructor
			time += TICK;												// increment time
			
			// When TEST = true; display current values of time, absolute x-position, absolute y-position, updated x-velocity and updated y-velocity, at a rate of 1 time/seconds.

			if (TEST) {
				System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n", time,X+Xo,Y+Yo,Vx,Vy);	//display values
				}
			
			// if the ball is going to the right and it hits the ceiling, player wins round
			if (Vx>0 && (Y+Yo) > Ymax){
				// Player score is incremented
				PlayerPoint++;
				// The while loop is terminated, and the score displays are updated using kill()
				kill();
			}
			// if the ball is going to the left and it hits the ceiling, agent wins round
			if (Vx<0 && (Y+Yo) > Ymax){
				// Agent score is incremented
				AgentPoint++;
				// The while loop is terminated, and the score displays are updated using kill()
				kill();
			}	
		}
		
		}
	/***
	* A simple method to plot trace points of a ball's trajectory on the screen
	* @param X - X location of point (world coordinates)
	* @param Y - Y location of point
	*/
	private void trace(double ScrX, double ScrY) {									
		// the tracing points are circles, created by GOVal method
		GOval tracePoints = new GOval(ScrX+(bSize*Xs), ScrY+(bSize*Xs), PD, PD);	// the ball's radius IN PIXELS is added to the x and y coordinates so that the center of the ball is traced. A trace point's diameter is PD.
		tracePoints.setFilled(true);												// stating that the trace points are filled
		tracePoints.setColor(Color.black);											// WHEN MULTIPLE BALLS RUNNING : set the trace points' color to black
		GProgram.add(tracePoints);													// adding the trace points to the screen using GProgram class/subclass that calls the constructor
	}
	/**
	 * Assigns the Paddle instance variable of a class to a ppPaddle object, passed as argument
	 * @param myPaddle - the ppPaddle object assigned to the instance variable of a receiver class
	 */
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}
	/**
	 * This method assigns the LPaddle instance variable to a ppPaddle object passed as argument
	 * Used to set agent's paddle as LPaddle
	 * @param myPaddle - ppPaddle object object passed as argument
	 */
	public void setLeftPaddle(ppPaddle myPaddle) {
		this.LPaddle = myPaddle;
	}
	/**
	 * This method presents the x and y position of the ball in a GPoint
	 * @return A GPoint with the ball's x and y position as its x and y values
	 */
	public GPoint getP() {
		return new GPoint(X+Xo, Y+Yo);			// absolute x position is X + Xo, absolute y position is Y + Yo
	}
	/**
	 * This method presents the x and y velocities of the ball in a GPoint
	 * @return A GPoint with the ball's x and y velocities as its x and y values
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}
	/**
	 * This program stops the while loop (ball movement simulation) and updates the JLabels displaying the scores of the agent and the player
	 */
	public void kill() {
		running = false;
		// updating the JLabels containing displaying scores
		AgentP.setText(AgentPoint.toString());			
		PlayerP.setText(PlayerPoint.toString());
	}
}