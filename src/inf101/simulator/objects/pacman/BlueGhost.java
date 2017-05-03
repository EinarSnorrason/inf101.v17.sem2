package inf101.simulator.objects.pacman;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Blue ghost. Switches between chasing pacman and moving to a random point on
 * the map
 * 
 * @author Einar Snorrason
 *
 */

public class BlueGhost extends AbstractGhost {

	private static final int CHASE_TIME = 1000;
	private boolean chasing = true;
	private int chaseTimer = 0;
	private Position targetPos;

	public BlueGhost(Position pos, Habitat hab) {
		super(pos, hab);
		chaseTimer = CHASE_TIME;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.setFill(Color.CYAN);
		context.fillOval(0, 0, getWidth(), getHeight());
	}

	@Override
	public void step() {
		// Chase pacman if in chase mode
		if (pacman != null && chasing) {
			if (canSee(pacman)) {
				setTarget(directionTo(pacman));
			}
			
		} 
		// Go to set point if not in chase mode
		else if (!chasing){
			
			if (targetPos == null){
				targetPos = new Position(Math.random()*habitat.getWidth(),Math.random()*habitat.getHeight());
			}
			setTarget(directionTo(targetPos));
		}

		// Decrement chase timer and swap behaviour if it reaches 0
		if (chaseTimer <= 0) {
			if (!chasing){
				targetPos = null;
			}
			chasing = !chasing;
			chaseTimer = CHASE_TIME;
		} else {
			chaseTimer--;
		}

		super.step();

	}

}
