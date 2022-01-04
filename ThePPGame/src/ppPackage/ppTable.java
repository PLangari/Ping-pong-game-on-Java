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
import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppTable {
	GraphicsProgram GProgram;								// an instance of the GraphicsProgram, used to add objects to display
	
	/**
	 * This constructor adds a left wall, a right wall and a ground line to the display
	 * @param GProgram - is the GProgram class (subclass) that calls the constructor
	 */
	public ppTable(GraphicsProgram GProgram) {
		// sets the GProgram instance variable to GProgram parameter
		this.GProgram = GProgram;
		
		// destroys all objects on screen and creates ground line
		newScreen();
	}
	
	/***
	* Method to convert from world to screen coordinates.
	* @param P - a point object in world coordinates
	* @return p - the corresponding point object in screen coordinates */
	public GPoint W2S (GPoint P) {
	    return new GPoint((P.getX()-Xmin)*Xs,ymax-(P.getY()-Ymin)*Ys);
	}
	/***
	 * Method to convert from screen to world coordinates
	 * @param P - a point object in screen coordinates
	 * @return - new GPoint, corresponding point object, in world coordinates 
	 */
	public GPoint S2W(GPoint P) {
		return new GPoint((P.getX()/Xs)+Xmin, (ymax-P.getY())/Ys+Ymin);
	}
	/**
	 * This method erases all on screen, except for buttons, draws a new ground line.
	 * It is made public so that it is accessible by ppSim.
	 */
	public void newScreen() {
		GProgram.removeAll();
		drawGroundLine();
	}
	/**
	 * This method generates a GRect instance as the plane, fills it with the color black, and adds it to the display.
	 * It is made public so that it is accessible by ppSim.
	 */
	public void drawGroundLine() {
		GRect gPlane = new GRect(0, HEIGHT, WIDTH, 3);						// creating the rectangle, using GRect
		gPlane.setFilled(true);												// stating that it is filled
		gPlane.setColor(Color.BLACK);										// setting its color to black
		GProgram.add(gPlane);												// adding it to the screen
	}
}
