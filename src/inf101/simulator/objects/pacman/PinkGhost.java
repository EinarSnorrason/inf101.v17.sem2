package inf101.simulator.objects.pacman;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Pink ghost. Aims at a point in front of pacman
 * @author Einar Snorrason
 *
 */

public class PinkGhost extends AbstractGhost {

	public PinkGhost(Position pos, Habitat hab) {
		super(pos, hab);
		ghostColor = Color.PINK;
	}
	
	@Override
	public void step(){
		if (pacman != null){
			if (canSee(pacman)){
				Position pos = pacman.getPosition().move(pacman.getDirection(), 200);
				setTarget(directionTo(pos));
			}
			
		}
		super.step();
	}

}
