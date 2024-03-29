package inf101.simulator.objects.pacman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import inf101.simulator.objects.examples.SimRepellant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Class for pacman. 
 * 
 * Pacman will eat pellets and superpellets in the habitat and avoid ghosts. If
 * he eats a superpellet, he sends a message to the ghosts that puts them in the 
 * scared state. While a ghost is in the scared state, pacman will attempt to eat them.
 * 
 * If eaten, pacman enters the dead state, where he will play a short animation before disappearing
 * @author Einar Snorrason
 *
 * Datainvariants:
 * -Score cannot be <0
 * - death timer cannot be >0 while alive
 */
public class Pacman extends AbstractMovingObject implements IEdibleObject {

	private static final double SPEED = 1.5;
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
	private static final double SENSE_DISTANCE = 75.0;
	private static final double SIZE = 50.0;
	private static final double TURN_SPEED = 1.5;
	private Habitat habitat;
	private Image[] images = new Image[4];
	private Image[] deadImages = new Image[11];
	private int imageCounter = 0;
	private int score;
	/**
	 * Used to count down time between pacman is eaten and pacman is destroyed
	 */
	private int deathTimer;
	private boolean dead = false;
	private List<ISimObject> nearby = null;



	public Pacman(Position pos, Habitat hab) {
		super(new Direction(0), pos, SPEED);
		this.habitat = hab;
		for (int i = 1; i <= 4; i++) {
			images[i - 1] = MediaHelper.getImage("pacman" + i + ".png");
		}
		for (int i = 1; i <= 11; i++) {
			deadImages[i - 1] = MediaHelper.getImage("deadPacman" + i + ".png");
		}
	}

	@Override
	public double getHeight() {
		return SIZE;
	}

	@Override
	public double getWidth() {
		return SIZE;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);

		if (!dead) {
			// Draw viewing angle
			context.setStroke(Color.GREEN.deriveColor(0.0, 1.0, 1.0, 0.5));
			GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);
			// Draws the image
			context.drawImage(images[imageCounter / 2], 0, 0, getWidth(), getHeight());
			imageCounter = (imageCounter + 1) % 8;
		} else {
			context.drawImage(deadImages[10 - deathTimer / 20], 0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * Checks if object within view distance is visible.
	 * 
	 * @param obj
	 * @return true if object is visible
	 */
	public boolean canSee(ISimObject obj) {
		if (!habitat.contains(obj.getPosition())) {
			return false;
		}
		if (obj.getPosition().distanceTo(getPosition()) < SENSE_DISTANCE + getRadius()) {
			return true;
		}
		return Math.abs(getDirection().toAngle() - directionTo(obj).toAngle()) < VIEW_ANGLE / 2;
	}

	/**
	 * Finds closest best available food (including ghosts if pacman has eaten a
	 * super pellet)
	 * 
	 * @return
	 */
	public IEdibleObject getBestFood() {
		double bestNutrition = 0;
		double nutrition = 0;
		IEdibleObject bestFood = null;
		if (nearby == null){
			nearby = habitat.nearbyObjects(this, VIEW_DISTANCE + getRadius());
		}

		// Saves object as best food if it has higher nutrient content than
		// current best food
		for (ISimObject obj : nearby) {
			if (obj instanceof IEdibleObject && canSee(obj)) {
				nutrition = ((IEdibleObject) obj).getNutritionalValue();
				if (nutrition > bestNutrition) {
					bestNutrition = nutrition;
					bestFood = (IEdibleObject) obj;
				}
			}
		}

		return bestFood;
	}

	/**
	 * Sets pacmans state to powered and sends a message to the ghosts
	 */
	private void powerUp() {
		habitat.triggerEvent(new SimEvent(this, "PowerUp", null, null));
	}


	/**
	 * Go to the best food
	 */
	private void goToFood() {
		IEdibleObject food = getBestFood();
		if (food != null) {
			turnTowards(directionTo(food), TURN_SPEED);

			// Eat food
			if (contains(food.getPosition())) {
				if (food instanceof SuperPellet) {
					powerUp();
				}
				score += food.eat(1);
				// Update display board
				habitat.triggerEvent(new SimEvent(this, "Points", null, score));
			}
		}
	}

	/**
	 * Steer away from the ghosts
	 */
	private void avoidGhosts() {
		for (ISimObject obj : nearby) {
			if (canSee(obj) && obj instanceof IGhost) {
				if (!((IGhost) obj).isScared()) {
					Direction opposite = directionTo(obj).turnBack();
					// The closer the ghost, the faster pacman turns away
					turnTowards(opposite, 1 + 4 * (1 - distanceTo(obj) / (VIEW_DISTANCE)));
				}
			}
		}
	}

	

	/**
	 * Pacman behaviour logic
	 */
	@Override
	public void step() {

		// by default, move slightly towards center
		this.turnTowards(directionTo(habitat.getCenter()), 0.5);

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			turnTowards(directionTo(habitat.getCenter()), 10);
			if (!habitat.contains(getPosition(), getRadius())) {
				// we're actually outside
				accelerateTo(5 * SPEED, 0.3);
			}
		}
		// List objects in visible distance
		nearby = habitat.nearbyObjects(this, VIEW_DISTANCE);

		goToFood();
		avoidGhosts();

		accelerateTo(SPEED, 0.2);

		// If dead, stop moving and decrement the death timer
		if (!dead) {
			super.step();
		} else {
			turnTowards(new Direction(180), 90);
			deathTimer--;
			if (deathTimer <= 0) {
				destroy();
			}
		}
		
		checkState();

	}

	@Override
	public double eat(double howMuch) {
		if (!dead){
			// Lets scoreboard know pacman is dead
			habitat.triggerEvent(new SimEvent(this, "Dead", null, null));
			deathTimer = deadImages.length * 10 * 2 - 1;
			dead = true;	
		}
		return 0;
	}

	@Override
	public double getNutritionalValue() {
		return 0;
	}
	
	/**
	 * 
	 * @return true if pacman is dead
	 */
	public boolean isDead(){
		return dead;
	}
	
	/**
	 * Check datainvariants
	 * 
	 * @throws IllegalArgumentException if :
	 * -Score is <0
	 * - death timer is >0 while alive
	 */
	private void checkState(){
		if (score<0){
			throw new IllegalArgumentException("Score cannot be less than 0");
		} else if (!dead && deathTimer>0){
			throw new IllegalArgumentException("Death timer cannot be >0 while alive");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (dead ? 1231 : 1237);
		result = prime * result + deathTimer;
		result = prime * result + ((habitat == null) ? 0 : habitat.hashCode());
		result = prime * result + imageCounter;
		result = prime * result + ((nearby == null) ? 0 : nearby.hashCode());
		result = prime * result + score;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pacman other = (Pacman) obj;
		if (dead != other.dead)
			return false;
		if (deathTimer != other.deathTimer)
			return false;
		if (imageCounter != other.imageCounter)
			return false;
		if (nearby == null) {
			if (other.nearby != null)
				return false;
		} else if (!nearby.equals(other.nearby))
			return false;
		if (score != other.score)
			return false;
		return true;
	}
	

}
