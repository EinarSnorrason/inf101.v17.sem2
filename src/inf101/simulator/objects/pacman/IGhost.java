package inf101.simulator.objects.pacman;

import inf101.simulator.objects.ISimObject;

public interface IGhost {

	/**
	 * 
	 * @return true if ghost is in "scared" or "dead" state
	 */
	boolean isScared();

	/**
	 * Checks if object within view distance is visible.
	 * 
	 * @param obj
	 * @return true if object is visible
	 */
	boolean canSee(ISimObject obj);

	/**
	 * Checks if object is within sensing distance
	 * 
	 * @param obj
	 * @return
	 */
	boolean canSense(ISimObject obj);

	/**
	 * Tries to find pacman in the habitat
	 * 
	 * @return Nearest pacman, or null if none was found
	 */
	Pacman findPacman();

}