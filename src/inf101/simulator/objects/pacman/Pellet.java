package inf101.simulator.objects.pacman;

import java.util.function.Consumer;

import inf101.simulator.Direction;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.IEdibleObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Small pellets that pacman can eat
 * 
 * @author Einar Snorrason
 *
 */

public class Pellet extends AbstractSimObject implements IEdibleObject{
	
	private static final int DIAMETER = 10;
	private static final int POINTS = 10;
	public static final Consumer<GraphicsContext> PAINTER = (GraphicsContext context) -> {
		Pellet obj = new Pellet(new Position(0, 0));
		obj.hideAnnotations = true;
		context.scale(1 / obj.getWidth(), 1 / obj.getHeight());
		obj.draw(context);
	};

	public Pellet(Position pos) {
		super(new Direction(0), pos);
	}
	
	
	
	@Override
	public void draw(GraphicsContext context){
		super.draw(context);
		context.setFill(Color.WHITE);
		context.fillOval(0, 0, getWidth(), getHeight());
	}

	@Override
	public double getHeight() {
		return DIAMETER;
	}

	@Override
	public double getWidth() {
		return DIAMETER;
	}

	/**
	 * Do nothing
	 */
	@Override
	public void step() {
	}

	/**
	 * Each pellet gets eaten after only one munch and gives 10 points
	 */
	@Override
	public double eat(double howMuch) {
		destroy();
		return POINTS;
	}

	@Override
	public double getNutritionalValue() {
		return POINTS;
	}

}
