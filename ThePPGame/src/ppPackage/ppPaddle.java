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
import acm.graphics.*;
import static ppPackage.ppSim.*;

import acm.program.*;

import static ppPackage.ppSimParams.*;

import java.awt.*;

public class ppPaddle extends Thread {
	
	// declaring variables that are needed for an object made in this class
	double X;		// X position of paddle
	double Y;		// Y position of paddle
	double Vx;		// X velocity of paddle
	double Vy;		// Y velocity of paddle
	private Color myColor; 					// instance of Color
	
	private GRect myPaddle; 				// GRect object representing paddle
	public GraphicsProgram GProgram;		// instance of GraphicsProgram used for adding objects to display, pausing the program, or anything GraphicsProgram allows one to do
	private ppTable myTable;				// instance of ppTable used for W2S and S2W, methods contained in ppTable
	
	/**
	 * Creates a ppPaddle object with X, Y, myTable and GProgram given by ppSim. Copies parameters to
	 * instance variables, creates a GRect to represent the paddle, and adds it to the display.
	 * @param X - position X of the paddle
	 * @param Y - position Y of the paddle
	 * @param myColor - color of paddle
	 * @param myTable - an instance of ppTable, to use methods contained in ppTable
	 * @param GProgram - an instance of GProgram, which is ppSim in our case
	 */
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		// we assign the parameters of constructor to the instance variables of class, using this. keyword
		this.X = X;
		this.Y = Y;
		this.myColor = myColor;
		this.myTable = myTable;
		this.GProgram = GProgram;	
		
		// we determine the upper left corner of the paddle object
		double upperLeftX = X - ppPaddleW/2;				
		double upperLeftY = Y + ppPaddleH/2;
		
		// setting the coordinates of the paddle in screen coordinates, using W2S method in ppTable; encapsulating them into GPoint p
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		
		// isolating x and y screen coordinates of the paddle
		double ScrX = p.getX();
		double ScrY = p.getY();
		
		// assigning the myPaddle variable to a GRect containing the paddle's x and y screen coordinates, as well as its x and y dimensions
		this.myPaddle = new GRect (ScrX, ScrY, ppPaddleW*Xs, ppPaddleH*Ys);
		
		// set the color of paddle to black
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		
		// add the paddle to display using the GProgram variable, assigned in constructor
		GProgram.add(myPaddle);
	}
	
	public void run() {
		// initiating last X and Y positions
		double lastX = X;			
		double lastY = Y;
		
		// the while loop measures the X and Y velocities of paddle, resets last X and Y positions, and pauses to make it slower
		while (true) {
			Vx = (X-lastX)/TICK;
			Vy = (Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			GProgram.pause(timeLag.getValue());					// the pause is done in accordance with the value set by the time lag slider, obtained with getValue() method
			
		}
	}
	/**
	 * Gets the X and Y coordinates of the paddle
	 * @return - GPoint containing corresponding X and Y positions
	 */
	public GPoint getP() {
		return new GPoint(X, Y);
	}
	/**
	 * Sets the location of the Paddle
	 * @param P - the current coordinates that we want the Paddle to be at
	 */
	public void setP(GPoint P) {
		// update X and Y to the x and y values of the GPoint P's argument (in world coordinates)
		this.X = P.getX(); 
		this.Y = P.getY();
		
		// the upper left corner position of paddle
		double upperLeftX = X - ppPaddleW/2; 
		double upperLeftY = Y + ppPaddleH/2;
			
		// p is in screen coordinates				
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
			
		// screen coordinates
		double ScrX = p.getX();
		double ScrY = p.getY();
		
		// move the GRect instance to the 
		this.myPaddle.setLocation(ScrX, ScrY);
	}
	/**
	 * Gets the velocities of Paddle
	 * @return - GPoint containing corresponding velocities (X and Y) of paddle
	 */
	public GPoint getV() {
		return new GPoint(Vx,Vy); // maybe?
	}
	/**
	 * Determines the direction of the Paddle's Y velocity
	 * @return - positive 1 or negative 1, according to the sign of Y velocity
	 */
	public double getSgnVy() {
		double o;
		return o = (Vy>=0) ? (1): (-1);				// this ?: operator returns +1 if Vy is positive, and -1 if Vy is negative
	}
	/**
	 * Called when there is possible contact; X+Xo >= Paddle's X position
	 * @param Sx - the X position of ball
	 * @param Sy - the Y position of ball
	 * @return - boolean, whether there is contact between Paddle and ball, or not
	 */
	public boolean contact(double Sx, double Sy) {
		// called whenever X+Xo >= myPaddle.getP.getX AND (between Yp + ppPaddleH/2 and  Yp - ppPaddleH/2)
		// true if ballY is in the paddleY range
		// false if not
		if ((Sy >= Y - ppPaddleH/2) && (Sy <= Y + ppPaddleH/2)) return true; 
		else return false;
	}
}
