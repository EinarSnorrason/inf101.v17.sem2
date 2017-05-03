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

public class Pacman extends AbstractMovingObject {

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
	private static final double SENSE_DISTANCE = 50.0;
	private static final double SIZE = 50.0;
	private static final double TURN_SPEED = 1.0;
	private static final int POWER_DURATION = 2000;
	private Habitat habitat;
	private Image img;
	private int score;
	private List<ISimObject> nearby = null;

	/**
	 * True when pacman eats a Super Pellet
	 */
	private boolean powered = false;
	/**
	 * Tracks how long pacman can be super
	 */
	private int powerTimer = 0;

	public Pacman(Position pos, Habitat hab) {
		super(new Direction(0), pos, SPEED);
		this.habitat = hab;
		img = MediaHelper.getImage("pipp.png");
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
		// Draw viewing angle
		context.setStroke(Color.GREEN.deriveColor(0.0, 1.0, 1.0, 0.5));
		GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);
		// Draws the image
		context.drawImage(img, 0, 0, getWidth(), getHeight());
	}

	/**
	 * Checks if object within view distance is visible.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean canSee(ISimObject obj) {
		if (!habitat.contains(obj.getPosition())) {
			return false;
		}
		if (obj.getPosition().distanceTo(getPosition()) < SENSE_DISTANCE) {
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

		// Saves object as best food if it has higher nutrient content than
		// current best food
		for (ISimObject obj : nearby) {
			if (obj instanceof IEdibleObject && canSee(obj) && (powered || !(obj instanceof AbstractGhost))) {
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
		powered = true;
		powerTimer = POWER_DURATION;
		// the "true" in the data field lets ghosts know to be scared
		habitat.triggerEvent(new SimEvent(this, "PowerUp", null, 0));
	}

	/**
	 * When the timer runs out, pacman is no longer super
	 */
	private void powerDown() {
		powered = false;
		// false in data field lets ghosts chase pacman again
		habitat.triggerEvent(new SimEvent(this, "PowerDown", null, 1));
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
			}
		}
	}

	/**
	 * Steer away from the ghosts
	 */
	private void avoidGhosts() {
		for (ISimObject obj : nearby) {
			if (canSee(obj) && obj instanceof AbstractGhost) {
				Direction opposite = directionTo(obj).turnBack();
				dir = dir.turnTowards(opposite, 1+4*(1-distanceTo(obj)/(VIEW_DISTANCE)));
			}
		}
	}
	
	@Override
	public void destroy(){
		// Lets ghosts know pacman is dead
		habitat.triggerEvent(new SimEvent(this, "Dead", null, 2));
		super.destroy();
	}
	
	

	/**
	 * Pacman behaviour logic
	 */
	@Override
	public void step() {

		// by default, move slightly towards center
		this.turnTowards(directionTo(habitat.getCenter()), 0.5);

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 2)) {
			turnTowards(directionTo(habitat.getCenter()), 10);
			if (!habitat.contains(getPosition(), getRadius())) {
				// we're actually outside
				accelerateTo(5 * SPEED, 0.3);
			}
		}
		// List objects in visible distance
		nearby = habitat.nearbyObjects(this, VIEW_DISTANCE);

		goToFood();

		if (!powered){
			avoidGhosts();
		}

		// Decrement timer if needed
		if (powerTimer > 0) {
			powerTimer--;
			// If timer runs out, stop powerup
			if (powerTimer <= 0) {
				powerDown();
			}
		}
		
		accelerateTo(SPEED,0.2);

		super.step();
	}

}
