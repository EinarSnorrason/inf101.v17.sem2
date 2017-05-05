package inf101.simulator.objects.pacman;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Yellow ghost. Wanders around aimlessly unless pacman is in the sense range
 * 
 * @author Einar Snorrason
 *
 */

public class YellowGhost extends AbstractGhost {

	private static final int TARGET_TIME = 500;

	private Position targetPos;
	private int targetTimer;

	public YellowGhost(Position pos, Habitat hab) {
		super(pos, hab);
		targetTimer = TARGET_TIME;
		ghostColor = Color.YELLOW;
	}

	@Override
	public void step() {

		// Choose new target when timer reaches 0
		if (targetTimer <= 0 || targetPos == null) {
			targetPos = new Position(Math.random() * habitat.getWidth(), Math.random() * habitat.getHeight());
			targetTimer = TARGET_TIME;
		}

		if (pacman != null) {
			if (distanceTo(pacman) < SENSE_DISTANCE + getRadius()) {
				setTarget(directionTo(pacman));
			} else {
				setTarget(directionTo(targetPos));
			}

		}
		targetTimer--;

		super.step();
	}

}
