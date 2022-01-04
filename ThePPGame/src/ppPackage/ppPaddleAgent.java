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

import acm.graphics.*;
import acm.program.*;
import java.awt.*;


public class ppPaddleAgent extends ppPaddle {
	
	ppBall myBall;			 // generating an instance of the ball
	
	/**
	 * This constructor creates a ppPaddleAgent object with same parameters as ppPaddle class's constructor
	 * @param X - initial x position of paddle
	 * @param Y - initial y position of paddle
	 * @param myColor - color of paddle
	 * @param myTable - ppTable instance associated to paddle
	 * @param GProgram - GraphicsProgram instance associated to paddle
	 */
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		// creates an object with superclass constructor (ppPaddle's constructor)
		super(X, Y, myColor, myTable, GProgram);
		
	}
	
	public void run() {
		
		// variable that counts the number of iteration up to N; initial value of ballSkip is set to zero
		int ballSkip = 0;
		double lastX;
		double lastY;
		
		// this while loop creates a lag in the response time of ppPaddleAgent by iterating at every Nth term (N is the value of lag set by slider in ppSim)
		while (true) {
			// we are resetting the variable paddle position update just like in ppPaddle
			// the reset is necessary because this run() method overrides the superclass run() method
			int Lag = agentLag.getValue();
			int tick = timeLag.getValue();
			lastX = X;
			lastY = Y;
			Vx = (X-lastX)/tick;
			Vy = (Y-lastY)/tick;
			// if ballSkip is less than N, it is incremented until it reaches N
			if (ballSkip <= Lag) {				
				ballSkip++;
			}
			// when ball reaches N, the position of ppPaddleAgent is finally updated (after Nth iteration)
			else {
				// get the ball Y position
				double Y = myBall.getP().getY();
				
				// set the Paddle position to that Y position
				this.setP(new GPoint(this.X, this.myBall.getP().getY()));
				
				// ballSkip is set back to zero
				ballSkip=0;
				}
			this.GProgram.pause(tick);
		}
	}
	
	/**
	 * Setter method; assigns myBall instance variable of a ppPaddleAgent object to a ppBall instance, passed as argument
	 * @param myBall - ppBall argument
	 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}
