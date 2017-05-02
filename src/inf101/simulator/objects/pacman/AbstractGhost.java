package inf101.simulator.objects.pacman;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;

/**
 * Abstract class which gives the behaviour of the ghosts that chase Pacman
 * @author Einar Snorrason
 *
 */

public class AbstractGhost extends AbstractMovingObject implements IEdibleObject{
	private static final double SPEED = 1.0;
	/**
	 * Distance where objects become visible
	 */
	private static final double VIEW_DISTANCE = 400.0;
	
	/**
	 * Angle of sight
	 */
	private static final double VIEW_ANGLE = 100.0;
	
	/**
	 * Distance where objects can be sensed from any direction
	 * (Simulates smell, hearing etc)
	 */
	private static final double SENSE_DISTANCE = 100.0;
	private static final double SIZE = 50.0;
	private Habitat habitat;
	
	/**
	 * The score pacman gets if he eats a ghost
	 */
	private static final double SCORE = 200.0;
	
	/**
	 * We interact with pacman a lot, so we'll save him here
	 */
	private Pacman pacman;
	
	/**
	 * False if ghost has been eaten by pacman
	 */
	private boolean alive = true;
	
	public AbstractGhost(Position pos, Habitat hab) {
		super(new Direction(Math.random()*360), pos, SPEED);
		this.habitat = hab;
		habitat.addListener(this, event -> say(event.getType()));
		
	}

	@Override
	public double getHeight() {
		return 50;
	}

	@Override
	public double getWidth() {
		return 50;
	}
	
	/**
	 * Fetches pacman from the habitat and saves him privately so we 
	 * don't have to search for him every step
	 * 
	 * (this assumes there's only one pacman, may have to change that later)
	 */
	private void fetchPacman(){
		pacman = (Pacman) habitat.filterObjects((ISimObject obj) -> obj instanceof Pacman).get(0);
			
	}
	
	/**
	 * Behaviour shared by all ghosts
	 */
	@Override
	public void step(){
		
		// Save pacman in the class if we haven't
		if (pacman == null){
			fetchPacman();
		}
		
		// If the ghost is touching pacman, kill him
		if (pacman.getPosition().distanceTo(getPosition())<getRadius() + pacman.getRadius() && alive){
			// Ghosts can't eat!
			pacman.destroy();
		}
	}

	/**
	 * Used if pacman eats the ghost. Sets ghost to "dead" state
	 * @param howMuch
	 * @return 
	 */
	@Override
	public double eat(double howMuch) {
		
		return SCORE;
	}

	/**
	 * Returns the score the ghost gives (if it hasn't already been eaten)
	 */
	@Override
	public double getNutritionalValue() {
		return alive ? SCORE : 0;
	}

}
