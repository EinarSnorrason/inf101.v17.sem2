package inf101.simulator.objects.examples;
import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimAnimal extends AbstractMovingObject {
	private static final double defaultSpeed = 1.0;
	private Habitat habitat;
	private Image img;
	
	/**
	 * Tracks energy of SimAnimal.
	 */
	private double energy;

	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		img = MediaHelper.getImage("pipp.png");
	}
	

	@Override
	public void draw(GraphicsContext context) {
		this.
		super.draw(context);
		// Draws the image (flips it if it would be upside-down)
		double angle = getDirection().toAngle();
		if (angle <90 && angle >-90){
			context.scale(1, -1);
			context.translate(0, -getHeight());
		}
		context.drawImage(img,0,0,getWidth(),getHeight());
	}

	public IEdibleObject getBestFood() {
		return getClosestFood();
	}

	public IEdibleObject getClosestFood() {
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius()+400)) {
			if(obj instanceof IEdibleObject)
				return (IEdibleObject) obj;
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
		
		// If you find food, go towards it
		IEdibleObject food = getBestFood();
		if (food != null && habitat.contains(food.getPosition())){
			dir = dir.turnTowards(directionTo(food), 2);
			if (distanceToTouch(food) <=0){
				// Stop moving when eating
				energy += food.eat(0.5);
				accelerateTo(0, 0.3);
			}
			else if (dir.equals(directionTo(food))){
				// Speed up when going towards food
				accelerateTo(2 * defaultSpeed, 0.3);
			}
		}
		


		accelerateTo(defaultSpeed, 0.1);
		
		// Lose energy over time
		energy -= 0.05;

		super.step();
	}
}
