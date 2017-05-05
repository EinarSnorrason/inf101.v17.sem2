package inf101.simulator.objects.pacman;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Object that is placed in habitat at a given position and can display messages
 * @author Einar Snorrason
 *
 */


public class Scoreboard extends AbstractSimObject implements ISimScoreboard{

	private Habitat habitat;
	private boolean active = false;
	private String message;
	private int score;
	
	public Scoreboard(Position pos, Habitat hab) {
		super(new Direction(0), pos);
		this.habitat = hab;
		habitat.addListener(this, event -> handleEvent(event));
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public double getWidth() {
		return 0;
	}
	
	
	@Override
	public void display(String message) {
		active = true;
		this.message = message;
		
	}

	@Override
	public void stop() {
		active = false;
		
	}
	
	@Override 
	public void draw(GraphicsContext context){
		if (active) {
			context.save();
			context.scale(1.0, -1.0);
			context.setFill(Color.WHITE);
			context.setFont(new Font(40));
			context.fillText(message, 0, 0);
			
			context.restore();
		}
	}
	
	
	public void handleEvent(SimEvent event){
		if (event.getType().equals("Points")){
			score = (int) event.getData();
			display("Score: "+score);
		} else if (event.getType().equals("Dead")){
			display("Game Over! Final Score: "+score+". Press R to restart");
		}
	}

	@Override
	public void step() {
		
	}

}
