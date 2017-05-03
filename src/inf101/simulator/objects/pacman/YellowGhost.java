package inf101.simulator.objects.pacman;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class YellowGhost extends AbstractGhost {

	public YellowGhost(Position pos, Habitat hab) {
		super(pos, hab);
	}
	@Override
	public void draw(GraphicsContext context){
		super.draw(context);
		context.setFill(Color.YELLOW);
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
