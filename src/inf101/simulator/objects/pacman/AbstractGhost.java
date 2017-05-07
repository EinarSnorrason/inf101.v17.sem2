package inf101.simulator.objects.pacman;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Abstract class which gives the behaviour of the ghosts that chase Pacman
 * 
 * @author Einar Snorrason
 *
 */

public class AbstractGhost extends AbstractMovingObject implements IEdibleObject {
	private static final double SPEED = 1.0;
	private static final double TURN_SPEED = 1.0;
	/**
	 * Distance where objects become visible
	 */
	private static final double VIEW_DISTANCE = 400.0;

	/**
	 * Angle of sight
	 */
	private static final double VIEW_ANGLE = 100.0;

	/**
	 * Distance where objects can be sensed from any direction (Simulates smell,
	 * hearing etc)
	 */
	protected static final double SENSE_DISTANCE = 100.0;
	private static final double SIZE = 50.0;
	protected Habitat habitat;

	/**
	 * The score pacman gets if he eats a ghost
	 */
	private static final double SCORE = 200.0;
	private static final int RESPAWN_TIME = 500;

	/**
	 * We interact with pacman a lot, so we'll save him here
	 */
	protected ISimObject pacman;

	/**
	 * False if ghost has been eaten by pacman
	 */
	private boolean alive = true;

	/**
	 * Used to track how much time before
	 */
	private int respawnTimer = 0;
	private Direction target;

	/**
	 * True when pacman has a super pellet
	 */
	private boolean scared = false;
	private double currentSpeed;

	protected Color ghostColor = (Color.RED.deriveColor(0.0, 1.0, 1.0, 0.5));
	protected Color scaredGhostColor = (Color.BLUE.deriveColor(0.0, 1.0, 1.0, 0.5));
	
	protected Image ghostImg;
	protected Image deadGhostImg;
	protected Image scaredGhostImg;

	public AbstractGhost(Position pos, Habitat hab, String imageName) {
		super(new Direction(Math.random() * 360), pos, SPEED);
		this.habitat = hab;
		habitat.addListener(this, event -> handleEvent(event));
		currentSpeed = SPEED;
		scaredGhostImg = MediaHelper.getImage("scaredGhost.png");
		
			deadGhostImg = MediaHelper.getImage("deadGhost.png");
			ghostImg = MediaHelper.getImage(imageName+".png");
		

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
	 * Handles events from the listener
	 */
	public void handleEvent(SimEvent event) {

		switch (event.getType()) {
		case "PowerUp":
			scared = true;
			break;

		case "PowerDown":
			scared = false;
			break;
		case "Dead":
			// Causes ghost to kill itself
			pacman = this;
			destroy();
		default:
			break;
		}
	}
	
	public boolean isScared(){
		return scared || !alive;
	}

	/**
	 * Fetches pacman from the habitat and saves him privately so we don't have
	 * to search for him every step
	 * 
	 * (this assumes there's only one pacman, may have to change that later)
	 */
	private boolean fetchPacman() {
		pacman = habitat.filterObjects((ISimObject obj) -> obj instanceof Pacman).get(0);
		return pacman != null;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		// Draw viewing angle

		context.setStroke(Color.RED.deriveColor(0.0, 1.0, 1.0, 0.5));
		GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);

		// Flip ghost if upside down
		double angle = getDirection().toAngle();
		if (angle < 90 && angle > -90) {
			context.scale(1, -1);
			context.translate(0, -getHeight());
		}
		// Draw ghost
		if (scared) {
			context.drawImage(scaredGhostImg, 0, 0, getWidth(), getHeight());
		} else if (!alive) {
			context.drawImage(deadGhostImg, 0, 0, getWidth(), getHeight());
		} else {
			context.drawImage(ghostImg, 0, 0, getWidth(), getHeight());
		}

	}

	/**
	 * Checks if object within view distance is visible.
	 * 
	 * @param obj
	 * @return
	 */
	protected boolean canSee(ISimObject obj) {
		if (!habitat.contains(obj.getPosition())) {
			return false;
		}
		if (obj.getPosition().distanceTo(getPosition()) < SENSE_DISTANCE) {
			return true;
		}
		return Math.abs(getDirection().toAngle() - directionTo(obj).toAngle()) < VIEW_ANGLE / 2;
	}

	/**
	 * Behaviour shared by all ghosts
	 */
	@Override
	public void step() {

		// Save pacman in the class if we haven't
		if (pacman == null) {
			if (!fetchPacman()) {
				return;
			}

		}

		// If the ghost is touching pacman, kill him
		if (contains(pacman.getPosition()) && alive && !scared) {
			// Ghosts can't eat!
			System.out.println(currentSpeed);
			pacman.destroy();
		}

		// by default, move slightly towards center
		this.turnTowards(directionTo(habitat.getCenter()), 0.5);

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			turnTowards(directionTo(habitat.getCenter()), 5);
		}

		// Move toward target or flee if not able to kill
		if (scared || !alive) {
			if (canSee(pacman)) {
				Direction opposite = directionTo(pacman).turnBack();
				dir = dir.turnTowards(opposite, TURN_SPEED);
			} else if (target != null){
				turnTowards(target, TURN_SPEED);
			}
		} else if (target != null) {
			turnTowards(target, TURN_SPEED);
		}

		// Decrement respawn timer if neccesary
		if (!alive) {
			respawnTimer--;
			if (respawnTimer <= 0) {
				alive = true;
				scared = false;
			}
		}
		
		// Slowly speed up!
		currentSpeed += 0.00005;
		
		// If dead, go faster
		if (!alive){
			accelerateTo(3*currentSpeed, 0.3);
		} else {
			accelerateTo(currentSpeed, 0.3);
		}
		

		super.step();
	}

	/**
	 * Sets given direction as target position of ghost
	 * 
	 * @param dir
	 */
	protected void setTarget(Direction dir) {
		target = dir;
	}

	/**
	 * Used if pacman eats the ghost. Sets ghost to "dead" state
	 * 
	 * @param howMuch
	 * @return
	 */
	@Override
	public double eat(double howMuch) {
		alive = false;
		scared = false;
		respawnTimer = RESPAWN_TIME;
		return SCORE;
	}

	/**
	 * Returns the score the ghost gives (if it hasn't already been eaten)
	 */
	@Override
	public double getNutritionalValue() {
		return alive && scared ? SCORE : 0;
	}

}
