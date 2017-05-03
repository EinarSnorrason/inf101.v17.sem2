package inf101.simulator.objects.pacman;

import java.util.function.Consumer;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Red ghost: Always follows pacman if he is visible
 * @author Einar Snorrason
 *
 */
public class RedGhost extends AbstractGhost {
	

	public RedGhost(Position pos, Habitat hab) {
		super(pos, hab);
	}
	
	@Override
	public void draw(GraphicsContext context){
		super.draw(context);
		context.setFill(Color.RED);
		context.fillOval(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public void step(){
		if (pacman != null){
			if (canSee(pacman)){
				setTarget(directionTo(pacman));
			}
			
		}
		super.step();
	}

}
