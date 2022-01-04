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

public class ppSimParams {
			
	
	// 1. Paramters defined in screen coordinates (pixels, acm coordinates)
	public static final int WIDTH = 1280; // n.b. screen coordinates
	public static final int HEIGHT = 600; 
	public static final int OFFSET = 200;
	
	// 2. Ping-pong table parameters
	public static final double ppTableXlen = 2.74;			// Length
	public static final double ppTableHgt = 1.52;			// Ceiling
	public static final double XwallL = 0.05;				// Position of l wall
	public static final double XwallR = 2.69;				// Position of r wall	
	
	// 3. Parameters defined in simulation coordinates
	public static final double g = 9.8;						// gravitational constant
	public static final double k = 0.1316; 					// Vt constant
	public static final double Pi = 3.1416;					// pi value up to 4 decimals
	public static final double bSize = 0.02; 				// pp ball radius
	public static final double bMass = 0.0027; 				// pp ball mass
	public static final double TICK = 0.01; 				// Clock tick duration (sec)
	public static final double ETHR = 0.001;				// Minimum ball energy			

	public static final double Xmin = 0.0;					// Minimum value of X world (pp table)
	public static final double Xmax = ppTableXlen;			// Maximum value of X world
	public static final double Ymin = 0.0;					// Minimum value of Y world
	public static final double Ymax = ppTableHgt;			// Maximum value of Y world
	public static final int xmin = 0;						// Minimum value of x screen
	public static final int xmax = WIDTH;					// Maximum value of x screen
	public static final int ymin = 0;						// Minimum value of y screen
	public static final int ymax = HEIGHT;					// Maximum value of y screen
	public static final double Xs = (xmax-xmin)/(Xmax-Xmin);// Scale factor X 
	public static final double Ys = (ymax-ymin)/(Ymax-Ymin);// Scale factor Y
	public static final double Xinit = XwallL; 				// initial ball location X
	public static final double Yinit = Ymax/2; 				// initial ball location Y
	public static final double PD = 1;						// trace point diameter
	public static final double TSCALE = 2000;				// scaling parameter for pause
	
	// 4. Paddle Parameters
	
	static final double ppPaddleH = 8*2.54/100;				// paddle height
	static final double ppPaddleW = 0.5*2.54/100;			// paddle width
	static final double ppPaddleXinit = XwallR-ppPaddleW/2; // initial user paddle X
	static final double ppPaddleYinit = Yinit; 				// initial user paddle Y
	static final double ppPaddleXgain = 2.0; 				// Vx gain on user paddle hit
	static final double ppPaddleYgain = 2.0;				// Vy gain on user paddle hit
	static final double LPaddleXinit = XwallL - ppPaddleW/2;// initial agent paddle X
	static final double LPaddleYinit = Yinit;				// initial agent paddle Y
	static final double LPaddleXgain = 2.0;					// Vx gain on agent paddle hit
	static final double LPaddleYgain = 2.0;					// Vy gain on agent paddle hit
//	public static final double VoxMAX = 5.0;
	
	// 5. Parameters used by the ppSim class
	static final double YinitMAX = 0.75*Ymax; 				// Max inital height at 75% of range
	static final double YinitMIN = 0.25*Ymax; 				// Min inital height at 25% of range
	static final double EMIN = 0.2;							// Minimum loss coefficient
	static final double EMAX = 0.2;							// Maximum loss coefficient
	static final double VoMIN = 5.0; 						// Minimum velocity
	static final double VoMAX = 5.0; 						// Maximum velocity
	static final double ThetaMIN = 0.0; 					// Minimum launch angle
	static final double ThetaMAX = 20.0; 					// Maximum launch angle
	static final long RSEED = 8976232;						// Random number gen. seed value

	// 4. Miscellaneous
	public static final boolean DEBUG = false; 				// Debug msg. and single step if true
	public static final boolean MESG = true;				// Enable status messages on console
	public static final int STARTDELAY = 1000;				// Delay between setup and start
	public static final boolean TEST = true;				// When true, enables display of ball's X, Y, Vx, Vy, and time
	public static boolean isSelected = false;
	
	// 5. Agent lag
	public static int AgentLag = 3;						// the initial value of agent's lag, given as argument to JSlider 
}
