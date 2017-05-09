package inf101.simulator.objects.pacman;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.objects.ISimObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Pink ghost. Aims at a point in front of pacman
 * @author Einar Snorrason
 *
 */

public class PinkGhost extends AbstractGhost {

	public PinkGhost(Position pos, Habitat hab) {
		super(pos, hab,"pinkGhost");
	}
	
	@Override
	public void step(){
		for (ISimObject obj: habitat.nearbyObjects(this,VIEW_DISTANCE)){
			pacman = findPacman();
			if (pacman != null){
				// Move to a point 200 units in front of pacman
				setTarget(directionTo(pacman.getPosition().move(pacman.getDirection(), 200)));			
			}
		}
		super.step();
	}

}
