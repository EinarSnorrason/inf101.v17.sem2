package inf101.simulator.objects.pacman;


import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.objects.ISimObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Red ghost: Always follows pacman if he is visible
 * @author Einar Snorrason
 *
 */
public class RedGhost extends AbstractGhost {
	

	public RedGhost(Position pos, Habitat hab) {
		super(pos, hab,"redGhost");
		ghostColor = Color.RED;
	}
	
	
	@Override
	public void step(){
		pacman = findPacman();
		if (pacman != null){
			setTarget(directionTo(pacman));			
		}
		super.step();
	}

}
