package inf101.simulator.objects.pacman;

/**
 * Interface for objects in the simulation intended to display text
 * @author Einar Snorrason
 *
 */

public interface ISimScoreboard {
	
	/**
	 * Display given message on screen
	 * @param message
	 */
	void display(String message);
	
	
	/**
	 * Stop displaying message
	 */
	void stop();
	
	/**
	 * 
	 * @return the message the scoreboard is displaying
	 */
	String getMessage();

}
