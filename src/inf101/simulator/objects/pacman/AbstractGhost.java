package inf101.simulator.objects.pacman;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.ISimObjectFactory;
import inf101.simulator.objects.SimEvent;
import inf101.simulator.objects.examples.SimFeed;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Abstract class which gives the behaviour of the ghosts that chase Pacman
 * 
 * Ghosts have three states that they change between:
 * 
 * -normal: Follow normal behaviour and kill pacman if they catch him -scared:
 * Move slower and flee from pacman. If pacman eats them in this state, they
 * become dead -dead: Move very fast and flee from pacman. Cannot interact with
 * pacman in any way
 * 
 * @author Einar Snorrason
 *
 */

public class AbstractGhost extends AbstractMovingObject implements IGhost {
	private static final double SPEED = 1.0;
	private static final double TURN_SPEED = 1.0;
	/**
	 * Distance where objects become visible
	 */
	protected static final double VIEW_DISTANCE = 400.0;

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
	private static final int SCARED_TIME = 1000;

	/**
	 * We interact with pacman a lot, so we'll save him here
	 */
	protected IEdibleObject pacman;

	/**
	 * False if ghost has been eaten by pacman
	 */
	private boolean dead = false;

	/**
	 * Used to track how much time before
	 */
	private int respawnTimer = 0;
	private int scaredTimer = 0;
	private Direction target;

	/**
	 * True when pacman has a super pellet
	 */
	private boolean scared = false;
	private double currentSpeed;

	protected Image[] ghostImg = new Image[4];
	protected Image[] deadGhostImg = new Image[4];
	protected Image scaredGhostImg;

	public AbstractGhost(Position pos, Habitat hab, String imageName) {
		super(new Direction(Math.random() * 360), pos, SPEED);
		this.habitat = hab;
		habitat.addListener(this, event -> handleEvent(event));
		currentSpeed = SPEED;

		// Load images
		int n = 0;
		for (int i = 0; i < 4; i++) {
			/*
			 * Awful hack to make the images load in the order: 4 - 1 - 3 - 2
			 */
			n += Math.pow(-1, i) * (4 - i);
			deadGhostImg[i] = MediaHelper.getImage("deadGhost" + n + ".png");
			ghostImg[i] = MediaHelper.getImage(imageName + n + ".png");
		}
		scaredGhostImg = MediaHelper.getImage("scaredGhost.png");

	}

	@Override
	public double getHeight() {
		return SIZE;
	}

	@Override
	public double getWidth() {
		return SIZE;
	}

	/**
	 * Handles events from the listener
	 */
	public void handleEvent(SimEvent event) {

		if (event.getType().equals("PowerUp") && !dead) {
			scared = true;
			scaredTimer = SCARED_TIME;
		}
		
		checkState();

	}

	/* (non-Javadoc)
	 * @see inf101.simulator.objects.pacman.IGhost#isScared()
	 */
	@Override
	public boolean isScared() {
		return scared || dead;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		// // Draw viewing angle
		//
		// context.setStroke(Color.RED.deriveColor(0.0, 1.0, 1.0, 0.5));
		// GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2,
		// VIEW_DISTANCE, 0, VIEW_ANGLE);

		double angle = getDirection().toAngle();

		// Rotates and scales ghost-image so they always face up
		context.translate(getWidth() / 2, getHeight() / 2);
		context.rotate(-angle);
		context.scale(1, -1);
		context.translate(-getWidth() / 2, -getHeight() / 2);

		if (scared) {
			context.drawImage(scaredGhostImg, 0, 0, getWidth(), getHeight());
		} else if (dead) {
			// Determines which image to use based on the angle
			context.drawImage(deadGhostImg[(int) ((angle + 405) % 360) / 90], 0, 0, getWidth(), getHeight());
		} else {
			context.drawImage(ghostImg[(int) ((angle + 405) % 360) / 90], 0, 0, getWidth(), getHeight());
		}

	}

	/* (non-Javadoc)
	 * @see inf101.simulator.objects.pacman.IGhost#canSee(inf101.simulator.objects.ISimObject)
	 */
	@Override
	public boolean canSee(ISimObject obj) {
		if (!habitat.contains(obj.getPosition())) {
			return false;
		}
		if (canSense(obj)) {
			return true;
		}
		return Math.abs(getDirection().toAngle() - directionTo(obj).toAngle()) < VIEW_ANGLE / 2;
	}

	/* (non-Javadoc)
	 * @see inf101.simulator.objects.pacman.IGhost#canSense(inf101.simulator.objects.ISimObject)
	 */
	@Override
	public boolean canSense(ISimObject obj) {
		return obj.getPosition().distanceTo(getPosition()) < SENSE_DISTANCE;
	}

	/**
	 * Behaviour shared by all ghosts
	 */
	@Override
	public void step() {

		// by default, move slightly towards center
		this.turnTowards(directionTo(habitat.getCenter()), 0.5);

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			turnTowards(directionTo(habitat.getCenter()), 5);
		}

		// Move toward target or flee if not able to kill
		pacman = findPacman();
		if (scared || dead) {
			if (pacman != null) {
				Direction opposite = directionTo(pacman).turnBack();
				turnTowards(opposite, TURN_SPEED);
			}
		} else if (target != null) {
			dir = dir.turnTowards(target, TURN_SPEED);
			if (pacman != null && contains(pacman.getPosition())){
				pacman.eat(0);
			}
		}

		// Set speed depending on state
		if (dead) {
			accelerateTo(3 * currentSpeed, 0.3);
			if (respawnTimer-- <= 0) {
				dead = false;
				scared = false;
			}
		} else if (scared) {
			accelerateTo(0.7 * currentSpeed, 0.3);
			if (scaredTimer-- <= 0) {
				scared = false;
			}
		} else {
			accelerateTo(currentSpeed, 0.3);
		}

		// Slowly speed up!
		currentSpeed += 0.00005;

		super.step();
		checkState();
	}

	/**
	 * Sets given direction as target position of ghost
	 * 
	 * @param dir
	 */
	protected void setTarget(Direction dir) {
		target = dir;
	}

	/* (non-Javadoc)
	 * @see inf101.simulator.objects.pacman.IGhost#findPacman()
	 */
	@Override
	public Pacman findPacman() {
		for (ISimObject obj : habitat.nearbyObjects(this, VIEW_DISTANCE)) {
			if (obj instanceof Pacman && canSee(obj)) {
				return (Pacman) obj;

			}
		}

		return null;
	}

	/**
	 * Used if pacman eats the ghost. Sets ghost to "dead" state
	 * 
	 * @param howMuch
	 * @return
	 */
	@Override
	public double eat(double howMuch) {
		dead = true;
		scared = false;
		respawnTimer = RESPAWN_TIME;
		return SCORE;
	}

	/**
	 * Returns the score the ghost gives (if it is in the scared state)
	 */
	@Override
	public double getNutritionalValue() {
		return !dead && scared ? SCORE : 0;
	}

	/**
	 * Checks datainvariants of object
	 * 
	 * These are: 
	 * -currentSpeed cannot be less than 0 
	 * -cannot be dead and scared at the same time
	 * -respawntimer cannot be greater than 0 while not dead
	 * -scaredtimer cannot be greater than 0 while not scared
	 * @throws IllegalStateException
	 */
	private void checkState() {
		if (currentSpeed < 0) {
			throw new IllegalStateException("Speed cannot be negative");
		} else if (dead && scared) {
			throw new IllegalStateException("Cannot be scared and dead at the same time");
		}
	}

}
