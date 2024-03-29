package inf101.simulator.objects.examples;

import java.util.ArrayList;
import java.util.Collections;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.EdibleComparator;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimAnimal extends AbstractMovingObject {
	private static final double defaultSpeed = 1.0;
	private static final double VIEW_ANGLE = 210;
	private static final double VIEW_DISTANCE = 400;
	private Habitat habitat;
	private Image img;
	private Direction opposite;
	private EdibleComparator foodComparator = new EdibleComparator(); 

	/**
	 * Tracks energy of SimAnimal.
	 */
	private double energy;

	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		img = MediaHelper.getImage("pipp.png");
		habitat.addListener(this, event -> say(event.getType()));
	}

	@Override
	public void draw(GraphicsContext context) {

		super.draw(context);
		
		
		
		// Draw viewing angle
		context.setStroke(Color.GREEN.deriveColor(0.0, 1.0, 1.0, 0.5));
		GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);
		// Draws the image (flips it if it would be upside-down)
		double angle = getDirection().toAngle();
		if (angle < 90 && angle > -90) {
			context.scale(1, -1);
			context.translate(0, -getHeight());
		}
		context.drawImage(img, 0, 0, getWidth(), getHeight());
	}

	/**
	 * Returns most nutritious visible food
	 * @return
	 */
	public IEdibleObject getBestFood() {
		ArrayList<IEdibleObject> food = new ArrayList<>();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + VIEW_DISTANCE)){
			if (obj instanceof IEdibleObject && canSee(obj)){
				food.add((IEdibleObject) obj);
			}
		}
		Collections.sort(food,foodComparator);
		return food.size()>0 ? food.get(food.size()-1) : null;
	}
	
	/**
	 * Shows if given object is visible by the given view angle
	 * @param obj
	 * @return true if object is visible
	 */
	protected boolean canSee(ISimObject obj){
		return Math.abs(getDirection().toAngle() - directionTo(obj).toAngle())<VIEW_ANGLE/2;
	}

	/**
	 * Gives the closest food object in the animal's view cone
	 * 
	 * @return
	 */
	public IEdibleObject getClosestFood() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + VIEW_DISTANCE)) {
			if (obj instanceof IEdibleObject && canSee(obj) && habitat.contains(obj.getPosition())) {
				return (IEdibleObject) obj;
			}
		}

		return null;
	}
	
	

	@Override
	public double getHeight() {
		return 50;
		
	}

	@Override
	public double getWidth() {
		return 50;
	}

	private void goToFood() {
		// If you find food, go towards it
		IEdibleObject food = getBestFood();

		if (food != null) {
			dir = dir.turnTowards(directionTo(food), 2);
			if (distanceToTouch(food) <= 0) {
				// Stop moving when eating
				energy += food.eat(0.5);
				SimEvent event = new SimEvent(this,"Eating",null,null);
				habitat.triggerEvent(event);
				accelerateTo(0, 0.1);
			} else if (dir.equals(directionTo(food))) {
				// Speed up when going towards food
				accelerateTo(2 * defaultSpeed, 0.3);
			}
		}
	}

	private void avoidRepellants() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + VIEW_DISTANCE)) {
			if (obj instanceof SimRepellant && canSee(obj) && habitat.contains(obj.getPosition())) {
				opposite = directionTo(obj).turnBack();
				
				dir = dir.turnTowards(opposite, 1+4*(1-distanceTo(obj)/(VIEW_DISTANCE)));
			}
		}
	}

	@Override
	public void step() {
		// by default, move slightly towards center
		dir = dir.turnTowards(directionTo(habitat.getCenter()), 0.5);

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
			if (!habitat.contains(getPosition(), getRadius())) {
				// we're actually outside
				accelerateTo(5 * defaultSpeed, 0.3);
			}
		}

		goToFood();
		avoidRepellants();

		accelerateTo(defaultSpeed, 0.1);

		// Lose energy over time
		if (energy < 0) {
			energy -= 0.05;
		}

		super.step();
	}
}
