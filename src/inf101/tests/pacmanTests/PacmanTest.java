package inf101.tests.pacmanTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.pacman.Pacman;
import inf101.simulator.objects.pacman.Pellet;
import inf101.simulator.objects.pacman.SuperPellet;
import inf101.tests.testHelpers.TestGhost;

public class PacmanTest {
	private SimMain main;
	private Habitat hab;

	@Before
	public void setUp() {
		this.main= new SimMain();
			
	}

	/**
	 * Test whether pacman eats pellets
	 */
	@Test
	public void eatPelletTest() {
		hab = new Habitat(main,1000,2000);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		Pellet pellet = new Pellet(new Position(600,500));
		hab.addObject(pacman);
		hab.addObject(pellet);
		for (int i=0;i<1000;i++){
			hab.step();
		}
		
		assertFalse("Pellet should be eaten",pellet.exists());
	}
	
	/**
	 * Pacman avoids ghosts
	 */
	@Test
	public void avoidGhost(){
		hab = new Habitat(main,1000,2000);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		TestGhost ghost = new TestGhost(new Position(600,600));
		hab.addObject(pacman);
		hab.addObject(ghost);
		for (int i=0;i<5000;i++){
			hab.step();
			// Pacman should never touch ghost
			assertFalse(pacman.contains(ghost.getPosition()));
		}
		
		
	}
	
	/**
	 * Pacman should seek out a scared ghost
	 */
	@Test
	public void seekScaredGhost(){
		hab = new Habitat(main,1000,2000);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		TestGhost ghost = new TestGhost(new Position(600,600));
		hab.addObject(pacman);
		hab.addObject(ghost);
		ghost.setScared(true);
		for (int i=0;i<5000;i++){
			hab.step();
			if (pacman.contains(ghost.getPosition())){
				return;
			}
		}
		fail("Pacman did not seek out scared ghost");
	}
	
	/**
	 * Pacman should identify best food
	 */
	@Test
	public void bestFoodTest(){
		hab = new Habitat(main,1000,2000);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		TestGhost ghost = new TestGhost(new Position(600,600));
		Pellet pellet = new Pellet(new Position(601,600));
		SuperPellet superPellet = new SuperPellet(new Position(602,600));
		hab.addObject(pacman);
		hab.addObject(ghost);
		hab.addObject(superPellet);
		hab.addObject(pellet);
		
		assertEquals(superPellet,pacman.getBestFood());
		
		// If ghost is scared, it is best food
		ghost.setScared(true);
		assertEquals(ghost,pacman.getBestFood());
	}
	
	/**
	 * Pacman cant see food behind him
	 */
	@Test
	public void cantSeeBehindTest(){
		hab = new Habitat(main,1000,2000);
		Pacman pacman = new Pacman(new Position(500,500),hab);
		Pellet pellet = new Pellet(new Position(300,500));
		hab.addObject(pacman);
		hab.addObject(pellet);
		
		assertEquals(null,pacman.getBestFood());
	}

}
