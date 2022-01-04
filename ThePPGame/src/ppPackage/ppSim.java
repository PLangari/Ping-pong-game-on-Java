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
import static ppPackage.ppBall.*;
import static ppPackage.ppSimParams.*;

import acm.graphics.GPoint;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;




//this is the starting point of our program 
public class ppSim extends GraphicsProgram{  			//we need to add this to specify which run method we are running, also because we're in package
	
	public static void main(String[] args) { 
		new ppSim().start(args);  // start method looks for the run method and executes it: looks for a run method
	}
	
	// we declare instance variables of myTable, myPaddle and myBall
	public ppTable myTable;														// instance of the ppTable 
	public ppPaddle RPaddle;													// instance of ppPaddle; user's paddle
	public ppPaddleAgent LPaddle;												// instance of ppPaddleAgent; agent's paddle
	public ppBall myBall;														// instance of ppBall (the ball)
	RandomGenerator rgen;														// instance of RandomGenerator
	
	public static Integer PlayerPoint = 0;										// Player's score variable; stored as Integer (object) so that it can turn into string (toString() method)
	public static Integer AgentPoint = 0;										// Agent's score variable; stored as Integer (object) so that it can turn into string (toString() method)
		
	public static JLabel AgentP = new JLabel(AgentPoint.toString());			// the agent's score JLabel; static and outside of run(), so that is is accessible across class
	public static JLabel PlayerP = new JLabel(PlayerPoint.toString());			// the player's score JLabel; static outside of run(), so that is is accessible across class (and ppBall)
	public static JSlider agentLag = new JSlider(0, 12, AgentLag);				// slider that controls agent's lag; static and outside of run(), so that it is accessible to ppBall
	public static JSlider timeLag = new JSlider(5, 50, (int) (TICK*TSCALE));	// slider that controls the time lag; static and outside of run(), so that it is accessible to ppBall
	
	public static JToggleButton traceButton;									// when selected, ball's path is traced. Placed outside run() and declared as static to make it accessible to ppBall class, which uses traceButton
	
	public void init() {
		this.resize(ppSimParams.WIDTH,ppSimParams.HEIGHT+OFFSET);				// specifying that WIDTH and HEIGHT refer to variables in ppSimParams
		
		// we initiate the buttons on the south of screen
		JButton newServeButton = new JButton("New Serve");						// serve button; initiates new serve in the game
		JButton quitButton = new JButton("Quit");								// the Quit button; quits the program
		traceButton = new JToggleButton("Trace");								// trace toggle button; when selected, traces the ball's path
		JButton clearButton = new JButton("Clear");								// clear button; initiates new serve and sets all scores to zero
		
		/*
		 * We add all buttons in the south of the screen, in the order desired:
		 * Clear button, new serve button, trace button and quit button.
		 * Then, we add the two sliders, with "+t" and "-t" beside the time lag slider
		 * and "-lag" and "+lag" beside the agent's lag slider.
		 */
		add(clearButton, SOUTH);
		add(newServeButton, SOUTH);
		add(traceButton, SOUTH);
		add(quitButton, SOUTH);
		add(new JLabel("+t"), SOUTH);											// lower time lag, a.k.a. harder
		add(timeLag, SOUTH);
		add(new JLabel("-t"), SOUTH);											// higher time lag, a.k.a. easier
		add(new JLabel("-lag"), SOUTH);											// lower agent lag, a.k.a. harder
		add(agentLag, SOUTH); 
		add(new JLabel("+lag"), SOUTH);											// higher agent lag, a.k.a. easier
		
		/*
		 * We add all the buttons in the north of the screen.
		 * We specify agent's score with "Agent: ", and show agent score
		 * We specify player's score with "Player: ", and show player score
		 */
		add(new JLabel("Agent: "), NORTH);
		add(AgentP, NORTH);
		add(new JLabel("Player: "), NORTH);
		add(PlayerP, NORTH);
		
		// We add mouse listeners for the buttons, and action listeners for mouse movement (to move the right paddle)
		addMouseListeners();
		addActionListeners();
		
		// we create instance of RandomGenerator to use (pseudo)randomness; seed will always make it start at a fixed point
		rgen = RandomGenerator.getInstance(); 
		rgen.setSeed(RSEED);
		
		// assigning myTable instance variable to a ppTable object; it will set the ping pong table
		myTable = new ppTable(this);
		
		// we start a new game 
		newGame();
	}
	ppBall newBall() {
		// using rgen (instance of RandomGenerator) sets parameters of the ball
		Color iColor = rgen.nextColor();													// random color 
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX); 								// random Y initial position
		double iLoss = rgen.nextDouble(EMIN,EMAX);											// random loss parameter
		double iVel = rgen.nextDouble(VoMIN,VoMAX); 										// random initial velocity
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);									// random launch angle
		// 4. Generate myBall
		return new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, this, myTable);		// creating instance of ppBall using above random parameters
	}
	
	public void newGame() {
		if (myBall != null) myBall.kill(); 														// stop current game
		// we add JLabels on top as well
		myTable.newScreen();																	// destroys what is on current screen (except buttons), and adds ground line
		myBall = newBall();																		// creates a new ball
		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this);		// creates right (user) paddle, using ppPaddle
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this); 	// creates left (agent) paddle, using ppPaddleAgent
		LPaddle.attachBall(myBall); 															// attaches the ball above to the agent paddle
		myBall.setRightPaddle(RPaddle);															// attaches right paddle to RPaddle instance of ball
		myBall.setLeftPaddle(LPaddle); 															// attaches left paddle to LPaddle instance of ball
		pause(STARTDELAY); 																		// sets a start delay, for user experience
		// start ball, right paddle and left paddle threads
		myBall.start();																			
		LPaddle.start();																		
		RPaddle.start();																		
	}
	
	/**
	 * This method sets the position of RPaddle to the position of the mouse, upon detecting mouse movement
	 * Mouse Handler - a moved event moves the paddle up and down in Y 
	 */
	public void mouseMoved(MouseEvent e) {
		if (myTable == null || RPaddle == null) return;
		// convert mouse position to a point in screen coordinates
		GPoint Pm = myTable.S2W(new GPoint(e.getX(),e.getY())); 				// Pm contains X and Y position of mouse in world coordinates, using S2W in ppTable
		// isolating current position of mouse X and Y
		double PaddleX = RPaddle.getP().getX();
		double PaddleY = Pm.getY();
		RPaddle.setP(new GPoint(PaddleX,PaddleY));								// setting the X and Y of mouse to the paddle
		}
	/**
	 * This method allows completion of a new serve, an exit or a reset of the players' respective scores, 
	 * upon the user clicking the "New Serve", "Quit" or "Clear" buttons.
	 * @param e - an action detected; a button clicked. 
	 */
	public void actionPerformed(ActionEvent e) { 
		String command = e.getActionCommand(); 									// obtaining the String associated with the button pressed
		// if "New Serve" was clicked, call newGame()
		if (command.equals("New Serve")) {										
			newGame(); 	
		}
		// if "Quit" was clicked, exit program
		else if (command.equals("Quit")) { 
			exit(); 															// the exit() method (for GraphicsProgram) closes the java program
		}
		// if "Clear" was clicked, call clear the scores
		else if (command.equals("Clear")) {
			// Agent and Player scores are set to zero
			AgentPoint = 0;
			PlayerPoint = 0;
			// the JLabels displaying scores are updated
			AgentP.setText(AgentPoint.toString());
			PlayerP.setText(PlayerPoint.toString());
		}
		}
}
