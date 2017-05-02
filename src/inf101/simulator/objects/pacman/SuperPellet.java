package inf101.simulator.objects.pacman;

import java.util.function.Consumer;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.IEdibleObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Large pellets that pacman can eat to gain the power to eat ghosts
 * @author Einar Snorrason
 *
 */

public class SuperPellet extends AbstractSimObject implements IEdibleObject {

	private static final int DIAMETER = 25;
	private static final int POINTS = 50;
	public static final Consumer<GraphicsContext> PAINTER = (GraphicsContext context) -> {
		SuperPellet obj = new SuperPellet(new Position(0, 0));
		obj.hideAnnotations = true;
		obj.pulse = 0.3;
		context.scale(1 / obj.getWidth(), 1 / obj.getHeight());
		obj.draw(context);
	};
	
	private double pulse = 0.0;
	
	public SuperPellet(Position pos) {
		super(new Direction(0), pos);
	}

	@Override
	public double getHeight() {
		return DIAMETER;
	}

	@Override
	public double getWidth() {
		return DIAMETER;
	}
	
	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.setFill(Color.WHITE.deriveColor(0.0, 1.0, 1.0, 0.5));
		context.fillOval(0, 0, getWidth(), getHeight());

		context.setStroke(Color.WHITE);
		context.setLineWidth(4);
		context.setLineDashes(4);
		context.setLineDashOffset(pulse);
		GraphicsHelper.strokeOvalAt(context, getWidth() / 2, getHeight() / 2, pulse * getWidth(), pulse * getHeight());
	}

	@Override
	public void step() {
		if (pulse >= 1.5)
			pulse = 0.0;
		else
			pulse += 0.01;
	}
	/**
	 * The pellet gets eaten at once, and gives 50 points
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
