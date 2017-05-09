package inf101.tests.pacmanTests;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.SimEvent;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.pacman.Scoreboard;

public class ScoreboardTest {
	private SimMain main;
	
	@Test
	public void messageTest(){
		// Test that scoreboard gets messages
		Habitat hab = new Habitat(main, 2000, 500);
		Scoreboard board = new Scoreboard(new Position(250, 250),hab);
		SimFeed feed = new SimFeed(new Position(500,500),10);
		hab.addObject(board);
		hab.addObject(feed);
		
		assertEquals(board.getMessage(),null);
		
		// Give board message
		int points = (int)(Math.random()*1000);
		hab.triggerEvent(new SimEvent(feed,"Points",null,points));
		hab.step();
		assertEquals("Score: "+points,board.getMessage());
		
		// Give board end message
		hab.triggerEvent(new SimEvent(feed,"Dead",null,null));
		hab.step();
		assertEquals(board.getMessage(),"Game Over! Final Score: "+points+". Press R to restart");
	}
	
	@Before
	public void setup() {
		main = new SimMain();
	}
}
