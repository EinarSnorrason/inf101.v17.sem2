package inf101.simulator.objects.examples;

import java.util.function.Consumer;

import inf101.simulator.Direction;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.IEdibleObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimFeed extends AbstractSimObject implements IEdibleObject {
	private static final double NUTRITION_FACTOR = 10;
	private static final double DIAMETER = 25;
	public static final Consumer<GraphicsContext> PAINTER = (GraphicsContext context) -> {
		SimFeed obj = new SimFeed(new Position(0, 0), 1.0);
		obj.hideAnnotations = true;
		context.scale(1 / obj.getWidth(), 1 / obj.getHeight());
		obj.draw(context);
	};

	private double size = 1.0;

	public SimFeed(Position pos, double size) {
		super(new Direction(0), pos);
		this.size = size;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.setFill(Color.CHOCOLATE);
		context.fillOval(0, 0, getWidth(), getHeight());
	}

	@Override
	public double eat(double howMuch) {
		double deltaSize = Math.min(size, howMuch / NUTRITION_FACTOR);
		size -= deltaSize;
		if (size == 0)
			destroy();
		return deltaSize * NUTRITION_FACTOR;
	}

	@Override
	public double getHeight() {
		return DIAMETER * size;
	}

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
	}

	@Override
	public double getWidth() {
		return DIAMETER * size;
	}

	@Override
	public void step() {
	}
	
	
}
